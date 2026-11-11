import { useEffect, useState, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../auth/AuthContext";
import { profileApi, lookupsApi } from "../../api/fitnova";
import { Modal } from "../../components/Modal";
import { PROVINCE_NAMES, SA_PROVINCES } from "../../data/southAfrica";
import type { Profile } from "../../types";
import "./ProfilePage.css";

const DEFAULT_GENDERS = ["Male", "Female", "Non-binary", "Other", "Prefer not to say"];
const DEFAULT_RACES = ["Black African", "Coloured", "Indian/Asian", "White", "Other"];
const RELATIONSHIPS = ["Parent", "Spouse / Partner", "Sibling", "Child", "Friend", "Other"];
const PHONE_PATTERN = /^\+?[0-9 ]{9,15}$/;

interface FormState {
    firstName: string;
    lastName: string;
    dateOfBirth: string;
    gender: string;
    race: string;
    streetNumber: string;
    streetName: string;
    suburbName: string;
    province: string;
    city: string;
    postalCode: string;
    cellphoneNumber: string;
    alternativeCellphoneNumber: string;
    kinFirstName: string;
    kinLastName: string;
    kinRelationship: string;
    kinCellphone: string;
}

function toForm(p: Profile): FormState {
    return {
        firstName: p.firstName ?? "",
        lastName: p.lastName ?? "",
        dateOfBirth: p.dateOfBirth ?? "",
        gender: p.gender ?? "",
        race: p.race ?? "",
        streetNumber: p.streetNumber ?? "",
        streetName: p.streetName ?? "",
        suburbName: p.suburbName ?? "",
        province: p.province ?? "",
        city: p.city ?? "",
        postalCode: p.postalCode ?? "",
        cellphoneNumber: p.cellphoneNumber ?? "",
        alternativeCellphoneNumber: p.alternativeCellphoneNumber ?? "",
        kinFirstName: p.nextOfKin?.firstName ?? "",
        kinLastName: p.nextOfKin?.lastName ?? "",
        kinRelationship: p.nextOfKin?.relationship ?? "",
        kinCellphone: p.nextOfKin?.cellphoneNumber ?? "",
    };
}

const dash = (value?: string | null) => (value && value.trim() ? value : "—");

export default function ProfilePage() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const [profile, setProfile] = useState<Profile | null>(null);
    const [form, setForm] = useState<FormState | null>(null);
    const [editing, setEditing] = useState(false);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState("");
    const [message, setMessage] = useState("");
    const [genderOptions, setGenderOptions] = useState<string[]>(DEFAULT_GENDERS);
    const [raceOptions, setRaceOptions] = useState<string[]>(DEFAULT_RACES);
    const [confirmingDelete, setConfirmingDelete] = useState(false);
    const [deletePassword, setDeletePassword] = useState("");
    const [deleting, setDeleting] = useState(false);

    useEffect(() => {
        let active = true;
        profileApi.get()
            .then((p) => { if (active) { setProfile(p); setForm(toForm(p)); } })
            .catch((e) => { if (active) setError(e instanceof Error ? e.message : "Could not load your profile."); })
            .finally(() => { if (active) setLoading(false); });
        lookupsApi.genderNames()
            .then((names) => {
                if (active && names.length) setGenderOptions(names);
            })
            .catch(() => undefined);
        lookupsApi.raceNames()
            .then((names) => {
                if (active && names.length) setRaceOptions(names);
            })
            .catch(() => undefined);
        return () => { active = false; };
    }, []);

    const set = <K extends keyof FormState>(key: K, value: FormState[K]) =>
        setForm((f) => (f ? { ...f, [key]: value } : f));

    const startEdit = () => {
        if (!profile) return;
        setForm(toForm(profile));
        setError("");
        setMessage("");
        setEditing(true);
    };

    const cancelEdit = () => {
        if (profile) setForm(toForm(profile));
        setError("");
        setEditing(false);
    };

    const validate = (f: FormState): string => {
        if (!f.firstName.trim() || !f.lastName.trim()) return "First name and last name are required.";
        if (!f.dateOfBirth) return "Enter your date of birth.";
        if (!f.gender) return "Select your gender.";
        if (!f.race) return "Select your race.";
        if (!f.streetName.trim() || !f.province || !f.city) return "Enter your street name and select a province and city.";
        if (f.postalCode.trim() && !/^\d{4}$/.test(f.postalCode.trim())) return "Postal code must be 4 digits.";
        if (!PHONE_PATTERN.test(f.cellphoneNumber.trim())) return "Enter a valid cellphone number.";
        if (f.alternativeCellphoneNumber.trim() && !PHONE_PATTERN.test(f.alternativeCellphoneNumber.trim()))
            return "Enter a valid alternative cellphone number.";
        if (!f.kinFirstName.trim() || !f.kinLastName.trim() || !f.kinRelationship) return "Complete the next of kin details.";
        if (!PHONE_PATTERN.test(f.kinCellphone.trim())) return "Enter a valid next of kin cellphone number.";
        return "";
    };

    const save = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (!form) return;
        const problem = validate(form);
        if (problem) {
            setError(problem);
            return;
        }
        setSaving(true);
        setError("");
        try {
            const updated = await profileApi.update({
                firstName: form.firstName.trim(),
                lastName: form.lastName.trim(),
                dateOfBirth: form.dateOfBirth,
                gender: form.gender,
                race: form.race,
                streetNumber: form.streetNumber.trim(),
                streetName: form.streetName.trim(),
                suburbName: form.suburbName.trim(),
                province: form.province,
                city: form.city,
                postalCode: form.postalCode.trim(),
                country: "South Africa",
                cellphoneNumber: form.cellphoneNumber.trim(),
                alternativeCellphoneNumber: form.alternativeCellphoneNumber.trim(),
                nextOfKin: {
                    firstName: form.kinFirstName.trim(),
                    lastName: form.kinLastName.trim(),
                    relationship: form.kinRelationship,
                    cellphoneNumber: form.kinCellphone.trim(),
                },
            });
            setProfile(updated);
            setForm(toForm(updated));
            setEditing(false);
            setMessage("Your profile has been updated.");
        } catch (e) {
            setError(e instanceof Error ? e.message : "Could not save your profile.");
        } finally {
            setSaving(false);
        }
    };

    const closeDelete = () => {
        setConfirmingDelete(false);
        setDeletePassword("");
    };

    const deleteProfile = async () => {
        if (!deletePassword) {
            setError("Enter your password to confirm.");
            return;
        }
        setDeleting(true);
        setError("");
        try {
            await profileApi.delete(deletePassword);
            logout();
            navigate("/", { replace: true });
        } catch (e) {
            setError(e instanceof Error ? e.message : "Could not delete your profile.");
            closeDelete();
        } finally {
            setDeleting(false);
        }
    };

    if (loading) return <div className="profile-page"><div className="profile-loading">Loading your profile…</div></div>;

    if (!profile || !form) {
        return (
            <div className="profile-page">
                <div className="profile-message error-message" role="alert">{error || "Profile not found."}</div>
            </div>
        );
    }

    const initials = `${profile.firstName?.[0] ?? ""}${profile.lastName?.[0] ?? ""}`.toUpperCase() || "?";
    const role = profile.roles?.[0] ?? user?.role ?? "MEMBER";

    const text = (id: string, label: string, key: keyof FormState, extra: Record<string, unknown> = {}) => (
        <div className="profile-field">
            <label htmlFor={id}>{label}</label>
            <input id={id} value={form[key]} onChange={(e) => set(key, e.target.value)} {...extra} />
        </div>
    );

    const view = (label: string, value?: string | null) => (
        <div className="profile-field">
            <label>{label}</label>
            <div className="profile-value">{dash(value)}</div>
        </div>
    );

    const addressLine = [profile.streetNumber, profile.streetName].filter(Boolean).join(" ");

    return (
        <div className="profile-page">
            <div className="profile-header">
                <div>
                    <h2>My profile</h2>
                    <p>All the information we hold about you.</p>
                </div>
                {!editing && <button type="button" className="edit-profile-btn" onClick={startEdit}>Edit profile</button>}
            </div>

            {message && <div className="profile-message success-message" role="status">{message}</div>}
            {error && <div className="profile-message error-message" role="alert">{error}</div>}

            <div className="profile-card">
                <div className="profile-card-header">
                    <div className="profile-avatar" aria-hidden="true">{initials}</div>
                    <div>
                        <h3>{profile.firstName} {profile.lastName}</h3>
                        <p>{profile.email}</p>
                        <span className="profile-role">{role}</span>
                    </div>
                </div>

                {editing ? (
                    <form onSubmit={save} noValidate>
                        <div className="profile-section">
                            <h3>Personal details</h3>
                            <div className="profile-form-grid">
                                {text("pf-first-name", "First name", "firstName")}
                                {text("pf-last-name", "Last name", "lastName")}
                                {text("pf-dob", "Date of birth", "dateOfBirth", { type: "date", max: new Date().toISOString().slice(0, 10) })}
                                <div className="profile-field">
                                    <label htmlFor="pf-gender">Gender</label>
                                    <select id="pf-gender" value={form.gender} onChange={(e) => set("gender", e.target.value)}>
                                        <option value="">Select gender</option>
                                        {form.gender && !genderOptions.includes(form.gender) && <option value={form.gender}>{form.gender}</option>}
                                        {genderOptions.map((g) => <option key={g} value={g}>{g}</option>)}
                                    </select>
                                </div>
                                <div className="profile-field">
                                    <label htmlFor="pf-race">Race</label>
                                    <select id="pf-race" value={form.race} onChange={(e) => set("race", e.target.value)}>
                                        <option value="">Select race</option>
                                        {form.race && !raceOptions.includes(form.race) && <option value={form.race}>{form.race}</option>}
                                        {raceOptions.map((r) => <option key={r} value={r}>{r}</option>)}
                                    </select>
                                </div>
                                <div className="profile-field">
                                    <label htmlFor="pf-email">Email address</label>
                                    <input id="pf-email" value={profile.email} disabled />
                                    <small>Your email is your login and can't be changed here.</small>
                                </div>
                            </div>
                        </div>

                        <div className="profile-section">
                            <h3>Address</h3>
                            <div className="profile-form-grid">
                                {text("pf-street-number", "Street number", "streetNumber")}
                                {text("pf-street-name", "Street name", "streetName")}
                                {text("pf-suburb", "Suburb", "suburbName")}
                                <div className="profile-field">
                                    <label htmlFor="pf-province">Province</label>
                                    <select id="pf-province" value={form.province} onChange={(e) => setForm({ ...form, province: e.target.value, city: "" })}>
                                        <option value="">Select province</option>
                                        {PROVINCE_NAMES.map((n) => <option key={n} value={n}>{n}</option>)}
                                    </select>
                                </div>
                                <div className="profile-field">
                                    <label htmlFor="pf-city">City</label>
                                    <select id="pf-city" value={form.city} onChange={(e) => set("city", e.target.value)} disabled={!form.province}>
                                        <option value="">{form.province ? "Select city" : "Select a province first"}</option>
                                        {form.city && form.province && !SA_PROVINCES[form.province]?.includes(form.city) && <option value={form.city}>{form.city}</option>}
                                        {(SA_PROVINCES[form.province] ?? []).map((n) => <option key={n} value={n}>{n}</option>)}
                                    </select>
                                </div>
                                {text("pf-postal-code", "Postal code", "postalCode", { inputMode: "numeric", maxLength: 4 })}
                                <div className="profile-field">
                                    <label htmlFor="pf-country">Country</label>
                                    <input id="pf-country" value="South Africa" disabled />
                                </div>
                            </div>
                        </div>

                        <div className="profile-section">
                            <h3>Contact details</h3>
                            <div className="profile-form-grid">
                                {text("pf-cellphone", "Cellphone number", "cellphoneNumber", { type: "tel" })}
                                {text("pf-alt-cellphone", "Alternative cellphone number", "alternativeCellphoneNumber", { type: "tel" })}
                            </div>
                        </div>

                        <div className="profile-section">
                            <h3>Next of kin</h3>
                            <div className="profile-form-grid">
                                {text("pf-kin-first-name", "First name", "kinFirstName")}
                                {text("pf-kin-last-name", "Last name", "kinLastName")}
                                <div className="profile-field">
                                    <label htmlFor="pf-kin-relationship">Relationship</label>
                                    <select id="pf-kin-relationship" value={form.kinRelationship} onChange={(e) => set("kinRelationship", e.target.value)}>
                                        <option value="">Select relationship</option>
                                        {form.kinRelationship && !RELATIONSHIPS.includes(form.kinRelationship) && <option value={form.kinRelationship}>{form.kinRelationship}</option>}
                                        {RELATIONSHIPS.map((r) => <option key={r} value={r}>{r}</option>)}
                                    </select>
                                </div>
                                {text("pf-kin-cellphone", "Cellphone number", "kinCellphone", { type: "tel" })}
                            </div>
                        </div>

                        <div className="profile-actions">
                            <button type="button" className="cancel-profile-btn" onClick={cancelEdit} disabled={saving}>Cancel</button>
                            <button type="submit" className="save-profile-btn" disabled={saving}>{saving ? "Saving…" : "Save changes"}</button>
                        </div>
                    </form>
                ) : (
                    <>
                        <div className="profile-section">
                            <h3>Personal details</h3>
                            <div className="profile-form-grid">
                                {view("First name", profile.firstName)}
                                {view("Last name", profile.lastName)}
                                {view("Date of birth", profile.dateOfBirth)}
                                {view("Gender", profile.gender)}
                                {view("Race", profile.race)}
                                {view("Email address", profile.email)}
                                {view("Member since", profile.memberSince)}
                            </div>
                        </div>

                        <div className="profile-section">
                            <h3>Address</h3>
                            <div className="profile-form-grid">
                                {view("Street", addressLine)}
                                {view("Suburb", profile.suburbName)}
                                {view("City", profile.city)}
                                {view("Province", profile.province)}
                                {view("Postal code", profile.postalCode)}
                                {view("Country", profile.country)}
                            </div>
                        </div>

                        <div className="profile-section">
                            <h3>Contact details</h3>
                            <div className="profile-form-grid">
                                {view("Cellphone number", profile.cellphoneNumber)}
                                {view("Alternative cellphone number", profile.alternativeCellphoneNumber)}
                            </div>
                        </div>

                        <div className="profile-section">
                            <h3>Next of kin</h3>
                            <div className="profile-form-grid">
                                {view("Name", profile.nextOfKin ? `${profile.nextOfKin.firstName} ${profile.nextOfKin.lastName}` : null)}
                                {view("Relationship", profile.nextOfKin?.relationship)}
                                {view("Cellphone number", profile.nextOfKin?.cellphoneNumber)}
                            </div>
                        </div>

                        <div className="profile-section profile-danger">
                            <h3>Delete profile</h3>
                            <p>Permanently delete your account, bookings and personal details. This cannot be undone.</p>
                            <button type="button" className="delete-profile-btn" onClick={() => { setError(""); setConfirmingDelete(true); }}>
                                Delete my profile
                            </button>
                        </div>
                    </>
                )}
            </div>

            {confirmingDelete && (
                <Modal title="Delete your profile?" onClose={closeDelete} width={440}>
                    <form onSubmit={(e) => { e.preventDefault(); void deleteProfile(); }}>
                        <p style={{ margin: "0 0 1rem" }}>
                            This permanently removes your account, bookings and personal details. Enter your password to confirm.
                        </p>
                        <div className="profile-field">
                            <label htmlFor="pf-delete-password">Password</label>
                            <input
                                id="pf-delete-password"
                                type="password"
                                autoComplete="current-password"
                                value={deletePassword}
                                onChange={(e) => setDeletePassword(e.target.value)}
                                autoFocus
                            />
                        </div>
                        <div className="profile-actions" style={{ marginTop: "1.25rem" }}>
                            <button type="button" className="cancel-profile-btn" onClick={closeDelete} disabled={deleting}>Cancel</button>
                            <button type="submit" className="delete-profile-btn" disabled={deleting}>{deleting ? "Deleting…" : "Delete forever"}</button>
                        </div>
                    </form>
                </Modal>
            )}
        </div>
    );
}
