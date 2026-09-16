import { useEffect, useMemo, useState } from "react";
import type { FormEvent } from "react";
import {
    AlertTriangle,
    ArrowLeft,
    ArrowRight,
    Pencil,
    Plus,
    RefreshCw,
    Search,
    Trash2,
    UserCheck,
    Users as UsersIcon,
} from "lucide-react";
import {
    accountsApi,
    addressesApi,
    contactsApi,
    demographicsApi,
    lookupsApi,
    nextOfKinApi,
    userRolesApi,
    usersApi,
} from "../api/fitnova";
import { Modal, ConfirmDialog } from "../components/Modal";
import { Toast } from "../components/Toast";
import type { Gender, Race, RoleType, User } from "../types";

const ROLE_OPTIONS: RoleType[] = ["MEMBER", "TRAINER", "ADMIN"];

interface WizardState {
    firstName: string;
    lastName: string;
    dateOfBirth: string;
    email: string;
    password: string;
    streetNumber: string;
    streetName: string;
    suburbName: string;
    city: string;
    postalCode: string;
    province: string;
    country: string;
    cellphoneNumber: string;
    alternativeCellphoneNumber: string;
    emailAddress: string;
    genderId: string;
    raceId: string;
    kinFirstName: string;
    kinLastName: string;
    kinRelationship: string;
    kinCellphone: string;
    role: RoleType | "";
}

const emptyWizard: WizardState = {
    firstName: "",
    lastName: "",
    dateOfBirth: "2000-01-01",
    email: "",
    password: "",
    streetNumber: "",
    streetName: "",
    suburbName: "",
    city: "",
    postalCode: "",
    province: "",
    country: "South Africa",
    cellphoneNumber: "",
    alternativeCellphoneNumber: "",
    emailAddress: "",
    genderId: "",
    raceId: "",
    kinFirstName: "",
    kinLastName: "",
    kinRelationship: "",
    kinCellphone: "",
    role: "MEMBER",
};

const STEPS = ["Personal", "Account", "Address", "Contact", "Demographic", "Next of kin", "Role"];

interface EditForm {
    userId: string;
    firstName: string;
    lastName: string;
    dateOfBirth: string;
    accountId?: string;
    email: string;
    addressId?: string;
    streetNumber: string;
    streetName: string;
    suburbName: string;
    city: string;
    postalCode: string;
    province: string;
    country: string;
    contactId?: string;
    cellphoneNumber: string;
    alternativeCellphoneNumber: string;
    emailAddress: string;
    demographyId?: string;
    genderId: string;
    raceId: string;
}

function toEditForm(user: User): EditForm {
    return {
        userId: user.userId,
        firstName: user.firstName,
        lastName: user.lastName,
        dateOfBirth: user.dateOfBirth ?? "",
        accountId: user.account?.accountId,
        email: user.account?.email ?? "",
        addressId: user.address?.addressId,
        streetNumber: user.address?.streetNumber ?? "",
        streetName: user.address?.streetName ?? "",
        suburbName: user.address?.suburbName ?? "",
        city: user.address?.city ?? "",
        postalCode: user.address?.postalCode ?? "",
        province: user.address?.province ?? "",
        country: user.address?.country ?? "South Africa",
        contactId: user.contact?.contactId,
        cellphoneNumber: user.contact?.cellphoneNumber ?? "",
        alternativeCellphoneNumber: user.contact?.alternativeCellphoneNumber ?? "",
        emailAddress: user.contact?.emailAddress ?? "",
        demographyId: user.demographic?.demographyId,
        genderId: user.demographic?.gender?.genderId ?? "",
        raceId: user.demographic?.race?.raceId ?? "",
    };
}

export function UsersPage() {
    const [users, setUsers] = useState<User[]>([]);
    const [genders, setGenders] = useState<Gender[]>([]);
    const [races, setRaces] = useState<Race[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [message, setMessage] = useState<string | null>(null);
    const [search, setSearch] = useState("");

    const [showWizard, setShowWizard] = useState(false);
    const [step, setStep] = useState(0);
    const [wizard, setWizard] = useState<WizardState>(emptyWizard);
    const [saving, setSaving] = useState(false);
    const [wizardError, setWizardError] = useState<string | null>(null);

    const [viewing, setViewing] = useState<User | null>(null);
    const [editForm, setEditForm] = useState<EditForm | null>(null);
    const [editError, setEditError] = useState<string | null>(null);
    const [deleting, setDeleting] = useState<User | null>(null);

    async function load() {
        setLoading(true);
        setError(null);
        try {
            const [u, g, r] = await Promise.all([
                usersApi.getAll(),
                lookupsApi.genders(),
                lookupsApi.races(),
            ]);
            setUsers(Array.isArray(u) ? u : []);
            setGenders(Array.isArray(g) ? g : []);
            setRaces(Array.isArray(r) ? r : []);
        } catch (e) {
            setError(e instanceof Error ? e.message : "Failed to load people");
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        let cancelled = false;

        Promise.all([usersApi.getAll(), lookupsApi.genders(), lookupsApi.races()])
            .then(([u, g, r]) => {
                if (!cancelled) {
                    setUsers(Array.isArray(u) ? u : []);
                    setGenders(Array.isArray(g) ? g : []);
                    setRaces(Array.isArray(r) ? r : []);
                }
            })
            .catch((e) => {
                if (!cancelled) {
                    setError(e instanceof Error ? e.message : "Failed to load people");
                }
            })
            .finally(() => {
                if (!cancelled) {
                    setLoading(false);
                }
            });

        return () => {
            cancelled = true;
        };
    }, []);

    const filtered = useMemo(() => {
        const q = search.trim().toLowerCase();
        if (!q) return users;
        return users.filter((u) =>
            `${u.firstName} ${u.lastName} ${u.account?.email ?? ""}`.toLowerCase().includes(q)
        );
    }, [users, search]);

    function openWizard() {
        setWizard(emptyWizard);
        setStep(0);
        setWizardError(null);
        setShowWizard(true);
    }

    function closeWizard() {
        setShowWizard(false);
    }

    function validateStep(): string | null {
        switch (STEPS[step]) {
            case "Personal":
                if (!wizard.firstName.trim() || !wizard.lastName.trim())
                    return "First and last name are required.";
                if (!wizard.dateOfBirth) return "Date of birth is required.";
                return null;
            case "Account":
                if (!wizard.email.trim() || !wizard.password.trim())
                    return "Email and password are required.";
                return null;
            case "Address":
                if (!wizard.streetName.trim() || !wizard.city.trim() || !wizard.country.trim())
                    return "Street name, city, and country are required.";
                return null;
            case "Demographic":
                if (!wizard.genderId || !wizard.raceId)
                    return "Select a gender and race (add one under Lookups if the list is empty).";
                return null;
            default:
                return null;
        }
    }

    function next() {
        const err = validateStep();
        if (err) {
            setWizardError(err);
            return;
        }
        setWizardError(null);
        setStep((s) => Math.min(s + 1, STEPS.length - 1));
    }

    function back() {
        setWizardError(null);
        setStep((s) => Math.max(s - 1, 0));
    }

    async function submitWizard() {
        const err = validateStep();
        if (err) {
            setWizardError(err);
            return;
        }
        setSaving(true);
        setWizardError(null);
        try {
            const address = await addressesApi.create({
                addressId: "",
                streetNumber: wizard.streetNumber,
                streetName: wizard.streetName,
                suburbName: wizard.suburbName,
                city: wizard.city,
                postalCode: wizard.postalCode,
                province: wizard.province,
                country: wizard.country,
            });

            const contact = await contactsApi.create({
                contactId: "",
                cellphoneNumber: wizard.cellphoneNumber,
                alternativeCellphoneNumber: wizard.alternativeCellphoneNumber,
                emailAddress: wizard.emailAddress || wizard.email,
            });

            const gender = genders.find((g) => g.genderId === wizard.genderId);
            const race = races.find((r) => r.raceId === wizard.raceId);
            const demographic = await demographicsApi.create({
                demographyId: "",
                gender,
                race,
            });

            const account = await accountsApi.create({
                accountId: "",
                email: wizard.email,
                password: wizard.password,
                registrationDate: new Date().toISOString().slice(0, 10),
            });

            const createdUser = await usersApi.create({
                userId: "",
                firstName: wizard.firstName,
                lastName: wizard.lastName,
                dateOfBirth: wizard.dateOfBirth,
                account,
                demographic,
                address,
                contact,
            });

            if (!createdUser) {
                throw new Error("The API rejected the profile — check every step is complete.");
            }

            if (wizard.kinFirstName.trim() || wizard.kinLastName.trim()) {
                await nextOfKinApi.create({
                    nextOfKinContactId: "",
                    firstName: wizard.kinFirstName,
                    lastName: wizard.kinLastName,
                    relationship: wizard.kinRelationship,
                    cellphoneNumber: wizard.kinCellphone,
                    user: createdUser,
                });
            }

            if (wizard.role) {
                await userRolesApi.create({
                    userRoleId: "",
                    user: createdUser,
                    roleId: wizard.role,
                    description: `${wizard.role} role for ${wizard.firstName} ${wizard.lastName}`,
                });
            }

            setMessage(`Created profile for ${wizard.firstName} ${wizard.lastName}.`);
            setShowWizard(false);
            await load();
        } catch (e) {
            setWizardError(
                e instanceof Error ? e.message : "Could not finish creating this profile."
            );
        } finally {
            setSaving(false);
        }
    }

    function openEdit(user: User) {
        setEditForm(toEditForm(user));
        setEditError(null);
    }

    async function submitEdit(e: FormEvent) {
        e.preventDefault();
        if (!editForm) return;
        setSaving(true);
        setEditError(null);
        try {
            const hasAddress =
                editForm.streetName.trim() || editForm.city.trim() || editForm.country.trim();
            const address = editForm.addressId
                ? await addressesApi.update({
                    addressId: editForm.addressId,
                    streetNumber: editForm.streetNumber,
                    streetName: editForm.streetName,
                    suburbName: editForm.suburbName,
                    city: editForm.city,
                    postalCode: editForm.postalCode,
                    province: editForm.province,
                    country: editForm.country,
                })
                : hasAddress
                    ? await addressesApi.create({
                        addressId: "",
                        streetNumber: editForm.streetNumber,
                        streetName: editForm.streetName,
                        suburbName: editForm.suburbName,
                        city: editForm.city,
                        postalCode: editForm.postalCode,
                        province: editForm.province,
                        country: editForm.country,
                    })
                    : undefined;

            const hasContact =
                editForm.cellphoneNumber.trim() ||
                editForm.alternativeCellphoneNumber.trim() ||
                editForm.emailAddress.trim();
            const contact = editForm.contactId
                ? await contactsApi.update({
                    contactId: editForm.contactId,
                    cellphoneNumber: editForm.cellphoneNumber,
                    alternativeCellphoneNumber: editForm.alternativeCellphoneNumber,
                    emailAddress: editForm.emailAddress,
                })
                : hasContact
                    ? await contactsApi.create({
                        contactId: "",
                        cellphoneNumber: editForm.cellphoneNumber,
                        alternativeCellphoneNumber: editForm.alternativeCellphoneNumber,
                        emailAddress: editForm.emailAddress,
                    })
                    : undefined;

            const gender = genders.find((g) => g.genderId === editForm.genderId);
            const race = races.find((r) => r.raceId === editForm.raceId);
            const demographic = editForm.demographyId
                ? await demographicsApi.update({
                    demographyId: editForm.demographyId,
                    gender,
                    race,
                })
                : gender || race
                    ? await demographicsApi.create({
                        demographyId: "",
                        gender,
                        race,
                    })
                    : undefined;

            const account = editForm.accountId
                ? await accountsApi.update({ accountId: editForm.accountId, email: editForm.email })
                : undefined;

            await usersApi.update({
                userId: editForm.userId,
                firstName: editForm.firstName,
                lastName: editForm.lastName,
                dateOfBirth: editForm.dateOfBirth,
                account,
                address,
                contact,
                demographic,
            });

            setMessage(`Updated ${editForm.firstName} ${editForm.lastName}.`);
            setEditForm(null);
            await load();
        } catch (e) {
            setEditError(e instanceof Error ? e.message : "Update failed");
        } finally {
            setSaving(false);
        }
    }

    async function confirmDelete() {
        if (!deleting) return;
        setSaving(true);
        try {
            await usersApi.delete(deleting.userId);
            setMessage(`Deleted ${deleting.firstName} ${deleting.lastName}.`);
            setDeleting(null);
            await load();
        } catch (e) {
            setError(e instanceof Error ? e.message : "Delete failed");
        } finally {
            setSaving(false);
        }
    }

    function roleBadges(user: User) {
        if (!user.userRoles || user.userRoles.length === 0) return "—";
        return user.userRoles.map((r) => r.roleId).join(", ");
    }

    function profileGaps(user: User): string[] {
        const gaps: string[] = [];
        if (!user.address) gaps.push("address");
        if (!user.contact) gaps.push("contact");
        if (!user.demographic) gaps.push("demographic");
        return gaps;
    }

    return (
        <div>
            <div className="toolbar" style={{ justifyContent: "space-between" }}>
                <div>
                    <h1 className="page-title">People</h1>
                    <p className="page-sub">Members and trainers, with full profiles from the FitNova API.</p>
                </div>
                <button className="btn" type="button" onClick={openWizard}>
                    <Plus size={16} /> New profile
                </button>
            </div>

            {error && (
                <div className="alert alert-error">
                    <AlertTriangle />
                    <span>{error}</span>
                </div>
            )}
            <Toast message={message} onDismiss={() => setMessage(null)} />

            <div className="toolbar">
        <span className="search-field">
          <Search />
          <input
              style={{ maxWidth: 280 }}
              placeholder="Search by name or email…"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
          />
        </span>
                <button className="btn btn-ghost" type="button" onClick={load} disabled={loading}>
                    <RefreshCw size={15} /> Refresh
                </button>
            </div>

            <div className="card table-wrap">
                {loading ? (
                    <>
                        <div className="skeleton skeleton-row" />
                        <div className="skeleton skeleton-row" />
                        <div className="skeleton skeleton-row" />
                    </>
                ) : filtered.length === 0 ? (
                    <div className="empty-state">
                        <UsersIcon />
                        <strong>No people match yet</strong>
                        <span>Create the first profile above to get started.</span>
                    </div>
                ) : (
                    <table>
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>DOB</th>
                            <th>Email</th>
                            <th>City</th>
                            <th>Roles</th>
                            <th>Profile</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        {filtered.map((u) => {
                            const gaps = profileGaps(u);
                            return (
                                <tr key={u.userId}>
                                    <td>
                                        <button className="link-btn" type="button" onClick={() => setViewing(u)}>
                                            {u.firstName} {u.lastName}
                                        </button>
                                    </td>
                                    <td>{u.dateOfBirth ?? "—"}</td>
                                    <td>{u.account?.email ?? "—"}</td>
                                    <td>{u.address?.city ?? "—"}</td>
                                    <td>{roleBadges(u)}</td>
                                    <td>
                                        {gaps.length === 0 ? (
                                            <span className="badge available">Complete</span>
                                        ) : (
                                            <span className="badge cancelled" title={`Missing: ${gaps.join(", ")}`}>
                          Incomplete
                        </span>
                                        )}
                                    </td>
                                    <td className="row-actions">
                                        <button className="btn-icon" type="button" onClick={() => openEdit(u)} title="Edit">
                                            {gaps.length > 0 ? (
                                                <>
                                                    <UserCheck /> Complete
                                                </>
                                            ) : (
                                                <>
                                                    <Pencil /> Edit
                                                </>
                                            )}
                                        </button>
                                        <button
                                            className="btn-icon danger"
                                            type="button"
                                            onClick={() => setDeleting(u)}
                                            title="Delete"
                                        >
                                            <Trash2 /> Delete
                                        </button>
                                    </td>
                                </tr>
                            );
                        })}
                        </tbody>
                    </table>
                )}
            </div>

            {showWizard && (
                <Modal
                    title="New profile"
                    subtitle={`Step ${step + 1} of ${STEPS.length}: ${STEPS[step]}`}
                    onClose={closeWizard}
                    width={560}
                >
                    <div className="step-pills">
                        {STEPS.map((s, i) => (
                            <span
                                key={s}
                                className={`step-pill ${i === step ? "active" : ""} ${i < step ? "done" : ""}`}
                            >
                {s}
              </span>
                        ))}
                    </div>

                    {wizardError && <div className="alert alert-error">{wizardError}</div>}

                    {STEPS[step] === "Personal" && (
                        <div>
                            <div className="form-row">
                                <label>First name</label>
                                <input
                                    value={wizard.firstName}
                                    onChange={(e) => setWizard({ ...wizard, firstName: e.target.value })}
                                />
                            </div>
                            <div className="form-row">
                                <label>Last name</label>
                                <input
                                    value={wizard.lastName}
                                    onChange={(e) => setWizard({ ...wizard, lastName: e.target.value })}
                                />
                            </div>
                            <div className="form-row">
                                <label>Date of birth</label>
                                <input
                                    type="date"
                                    value={wizard.dateOfBirth}
                                    onChange={(e) => setWizard({ ...wizard, dateOfBirth: e.target.value })}
                                />
                            </div>
                        </div>
                    )}

                    {STEPS[step] === "Account" && (
                        <div>
                            <div className="form-row">
                                <label>Login email</label>
                                <input
                                    type="email"
                                    value={wizard.email}
                                    onChange={(e) => setWizard({ ...wizard, email: e.target.value })}
                                />
                            </div>
                            <div className="form-row">
                                <label>Password</label>
                                <input
                                    type="password"
                                    value={wizard.password}
                                    onChange={(e) => setWizard({ ...wizard, password: e.target.value })}
                                />
                            </div>
                        </div>
                    )}

                    {STEPS[step] === "Address" && (
                        <div>
                            <div className="grid-2">
                                <div className="form-row">
                                    <label>Street number</label>
                                    <input
                                        value={wizard.streetNumber}
                                        onChange={(e) => setWizard({ ...wizard, streetNumber: e.target.value })}
                                    />
                                </div>
                                <div className="form-row">
                                    <label>Street name</label>
                                    <input
                                        value={wizard.streetName}
                                        onChange={(e) => setWizard({ ...wizard, streetName: e.target.value })}
                                    />
                                </div>
                            </div>
                            <div className="form-row">
                                <label>Suburb</label>
                                <input
                                    value={wizard.suburbName}
                                    onChange={(e) => setWizard({ ...wizard, suburbName: e.target.value })}
                                />
                            </div>
                            <div className="grid-2">
                                <div className="form-row">
                                    <label>City</label>
                                    <input
                                        value={wizard.city}
                                        onChange={(e) => setWizard({ ...wizard, city: e.target.value })}
                                    />
                                </div>
                                <div className="form-row">
                                    <label>Postal code</label>
                                    <input
                                        value={wizard.postalCode}
                                        onChange={(e) => setWizard({ ...wizard, postalCode: e.target.value })}
                                    />
                                </div>
                            </div>
                            <div className="grid-2">
                                <div className="form-row">
                                    <label>Province</label>
                                    <input
                                        value={wizard.province}
                                        onChange={(e) => setWizard({ ...wizard, province: e.target.value })}
                                    />
                                </div>
                                <div className="form-row">
                                    <label>Country</label>
                                    <input
                                        value={wizard.country}
                                        onChange={(e) => setWizard({ ...wizard, country: e.target.value })}
                                    />
                                </div>
                            </div>
                        </div>
                    )}

                    {STEPS[step] === "Contact" && (
                        <div>
                            <div className="form-row">
                                <label>Cellphone number</label>
                                <input
                                    value={wizard.cellphoneNumber}
                                    onChange={(e) => setWizard({ ...wizard, cellphoneNumber: e.target.value })}
                                />
                            </div>
                            <div className="form-row">
                                <label>Alternative cellphone</label>
                                <input
                                    value={wizard.alternativeCellphoneNumber}
                                    onChange={(e) =>
                                        setWizard({ ...wizard, alternativeCellphoneNumber: e.target.value })
                                    }
                                />
                            </div>
                            <div className="form-row">
                                <label>Contact email (defaults to login email)</label>
                                <input
                                    type="email"
                                    value={wizard.emailAddress}
                                    onChange={(e) => setWizard({ ...wizard, emailAddress: e.target.value })}
                                    placeholder={wizard.email}
                                />
                            </div>
                        </div>
                    )}

                    {STEPS[step] === "Demographic" && (
                        <div>
                            {genders.length === 0 || races.length === 0 ? (
                                <p className="muted">
                                    No genders or races configured yet. Add them on the Lookups page, then come
                                    back to finish this profile.
                                </p>
                            ) : (
                                <>
                                    <div className="form-row">
                                        <label>Gender</label>
                                        <select
                                            value={wizard.genderId}
                                            onChange={(e) => setWizard({ ...wizard, genderId: e.target.value })}
                                        >
                                            <option value="">Select…</option>
                                            {genders.map((g) => (
                                                <option key={g.genderId} value={g.genderId}>
                                                    {g.description}
                                                </option>
                                            ))}
                                        </select>
                                    </div>
                                    <div className="form-row">
                                        <label>Race</label>
                                        <select
                                            value={wizard.raceId}
                                            onChange={(e) => setWizard({ ...wizard, raceId: e.target.value })}
                                        >
                                            <option value="">Select…</option>
                                            {races.map((r) => (
                                                <option key={r.raceId} value={r.raceId}>
                                                    {r.description}
                                                </option>
                                            ))}
                                        </select>
                                    </div>
                                </>
                            )}
                        </div>
                    )}

                    {STEPS[step] === "Next of kin" && (
                        <div>
                            <p className="muted" style={{ marginBottom: "0.75rem", fontSize: "0.85rem" }}>
                                Optional — leave blank to skip.
                            </p>
                            <div className="grid-2">
                                <div className="form-row">
                                    <label>First name</label>
                                    <input
                                        value={wizard.kinFirstName}
                                        onChange={(e) => setWizard({ ...wizard, kinFirstName: e.target.value })}
                                    />
                                </div>
                                <div className="form-row">
                                    <label>Last name</label>
                                    <input
                                        value={wizard.kinLastName}
                                        onChange={(e) => setWizard({ ...wizard, kinLastName: e.target.value })}
                                    />
                                </div>
                            </div>
                            <div className="form-row">
                                <label>Relationship</label>
                                <input
                                    value={wizard.kinRelationship}
                                    onChange={(e) => setWizard({ ...wizard, kinRelationship: e.target.value })}
                                />
                            </div>
                            <div className="form-row">
                                <label>Cellphone number</label>
                                <input
                                    value={wizard.kinCellphone}
                                    onChange={(e) => setWizard({ ...wizard, kinCellphone: e.target.value })}
                                />
                            </div>
                        </div>
                    )}

                    {STEPS[step] === "Role" && (
                        <div>
                            <div className="form-row">
                                <label>Role</label>
                                <select
                                    value={wizard.role}
                                    onChange={(e) => setWizard({ ...wizard, role: e.target.value as RoleType })}
                                >
                                    <option value="">No role</option>
                                    {ROLE_OPTIONS.map((r) => (
                                        <option key={r} value={r}>
                                            {r}
                                        </option>
                                    ))}
                                </select>
                            </div>
                        </div>
                    )}

                    <div className="toolbar" style={{ justifyContent: "space-between", marginTop: "1.25rem" }}>
                        <button className="btn btn-ghost" type="button" onClick={back} disabled={step === 0 || saving}>
                            <ArrowLeft size={15} /> Back
                        </button>
                        {step < STEPS.length - 1 ? (
                            <button className="btn" type="button" onClick={next}>
                                Next <ArrowRight size={15} />
                            </button>
                        ) : (
                            <button className="btn" type="button" onClick={submitWizard} disabled={saving}>
                                {saving ? "Creating…" : "Create profile"}
                            </button>
                        )}
                    </div>
                </Modal>
            )}

            {viewing && (
                <Modal title={`${viewing.firstName} ${viewing.lastName}`} onClose={() => setViewing(null)}>
                    <dl className="detail-list">
                        <dt>Date of birth</dt>
                        <dd>{viewing.dateOfBirth ?? "—"}</dd>
                        <dt>Email</dt>
                        <dd>{viewing.account?.email ?? "—"}</dd>
                        <dt>Address</dt>
                        <dd>
                            {viewing.address
                                ? `${viewing.address.streetNumber ?? ""} ${viewing.address.streetName}, ${
                                    viewing.address.suburbName ?? ""
                                } ${viewing.address.city}, ${viewing.address.province ?? ""} ${
                                    viewing.address.postalCode ?? ""
                                }, ${viewing.address.country}`
                                : "— missing"}
                        </dd>
                        <dt>Contact</dt>
                        <dd>
                            {viewing.contact
                                ? `${viewing.contact.cellphoneNumber ?? "—"}${
                                    viewing.contact.alternativeCellphoneNumber
                                        ? ` / ${viewing.contact.alternativeCellphoneNumber}`
                                        : ""
                                }`
                                : "— missing"}
                        </dd>
                        <dt>Demographic</dt>
                        <dd>
                            {viewing.demographic
                                ? `${viewing.demographic.gender?.description ?? "—"} · ${
                                    viewing.demographic.race?.description ?? "—"
                                }`
                                : "— missing"}
                        </dd>
                        <dt>Roles</dt>
                        <dd>{roleBadges(viewing)}</dd>
                        <dt>Next of kin</dt>
                        <dd>
                            {viewing.nextOfKinContacts && viewing.nextOfKinContacts.length > 0
                                ? viewing.nextOfKinContacts
                                    .map((k) => `${k.firstName} ${k.lastName} (${k.relationship ?? "—"})`)
                                    .join(", ")
                                : "—"}
                        </dd>
                    </dl>
                    {profileGaps(viewing).length > 0 && (
                        <div className="alert alert-error" style={{ marginTop: "1rem" }}>
                            This profile is missing {profileGaps(viewing).join(", ")}. Use Edit to fill it in.
                        </div>
                    )}
                </Modal>
            )}

            {editForm && (
                <Modal
                    title={`Edit ${editForm.firstName} ${editForm.lastName}`}
                    subtitle="Any section left blank stays empty; fill one in to add it to this profile."
                    onClose={() => setEditForm(null)}
                >
                    <form onSubmit={submitEdit}>
                        {editError && <div className="alert alert-error">{editError}</div>}

                        <h3 className="form-section-title">Personal</h3>
                        <div className="grid-2">
                            <div className="form-row">
                                <label>First name</label>
                                <input
                                    value={editForm.firstName}
                                    onChange={(e) => setEditForm({ ...editForm, firstName: e.target.value })}
                                />
                            </div>
                            <div className="form-row">
                                <label>Last name</label>
                                <input
                                    value={editForm.lastName}
                                    onChange={(e) => setEditForm({ ...editForm, lastName: e.target.value })}
                                />
                            </div>
                        </div>
                        <div className="form-row">
                            <label>Date of birth</label>
                            <input
                                type="date"
                                value={editForm.dateOfBirth}
                                onChange={(e) => setEditForm({ ...editForm, dateOfBirth: e.target.value })}
                            />
                        </div>

                        {editForm.accountId && (
                            <>
                                <h3 className="form-section-title">Account</h3>
                                <div className="form-row">
                                    <label>Email</label>
                                    <input
                                        type="email"
                                        value={editForm.email}
                                        onChange={(e) => setEditForm({ ...editForm, email: e.target.value })}
                                    />
                                </div>
                            </>
                        )}

                        <h3 className="form-section-title">
                            Address {!editForm.addressId && <span className="muted">(missing — fill in to add)</span>}
                        </h3>
                        <div className="grid-2">
                            <div className="form-row">
                                <label>Street number</label>
                                <input
                                    value={editForm.streetNumber}
                                    onChange={(e) => setEditForm({ ...editForm, streetNumber: e.target.value })}
                                />
                            </div>
                            <div className="form-row">
                                <label>Street name</label>
                                <input
                                    value={editForm.streetName}
                                    onChange={(e) => setEditForm({ ...editForm, streetName: e.target.value })}
                                />
                            </div>
                        </div>
                        <div className="grid-2">
                            <div className="form-row">
                                <label>City</label>
                                <input
                                    value={editForm.city}
                                    onChange={(e) => setEditForm({ ...editForm, city: e.target.value })}
                                />
                            </div>
                            <div className="form-row">
                                <label>Province</label>
                                <input
                                    value={editForm.province}
                                    onChange={(e) => setEditForm({ ...editForm, province: e.target.value })}
                                />
                            </div>
                        </div>
                        <div className="grid-2">
                            <div className="form-row">
                                <label>Postal code</label>
                                <input
                                    value={editForm.postalCode}
                                    onChange={(e) => setEditForm({ ...editForm, postalCode: e.target.value })}
                                />
                            </div>
                            <div className="form-row">
                                <label>Country</label>
                                <input
                                    value={editForm.country}
                                    onChange={(e) => setEditForm({ ...editForm, country: e.target.value })}
                                />
                            </div>
                        </div>

                        <h3 className="form-section-title">
                            Contact {!editForm.contactId && <span className="muted">(missing — fill in to add)</span>}
                        </h3>
                        <div className="grid-2">
                            <div className="form-row">
                                <label>Cellphone number</label>
                                <input
                                    value={editForm.cellphoneNumber}
                                    onChange={(e) => setEditForm({ ...editForm, cellphoneNumber: e.target.value })}
                                />
                            </div>
                            <div className="form-row">
                                <label>Alternative cellphone</label>
                                <input
                                    value={editForm.alternativeCellphoneNumber}
                                    onChange={(e) =>
                                        setEditForm({ ...editForm, alternativeCellphoneNumber: e.target.value })
                                    }
                                />
                            </div>
                        </div>
                        <div className="form-row">
                            <label>Contact email</label>
                            <input
                                type="email"
                                value={editForm.emailAddress}
                                onChange={(e) => setEditForm({ ...editForm, emailAddress: e.target.value })}
                            />
                        </div>

                        <h3 className="form-section-title">
                            Demographic{" "}
                            {!editForm.demographyId && <span className="muted">(missing — fill in to add)</span>}
                        </h3>
                        {genders.length === 0 || races.length === 0 ? (
                            <p className="muted" style={{ marginBottom: "0.85rem" }}>
                                No genders or races configured yet — add them on the Lookups page first.
                            </p>
                        ) : (
                            <div className="grid-2">
                                <div className="form-row">
                                    <label>Gender</label>
                                    <select
                                        value={editForm.genderId}
                                        onChange={(e) => setEditForm({ ...editForm, genderId: e.target.value })}
                                    >
                                        <option value="">Select…</option>
                                        {genders.map((g) => (
                                            <option key={g.genderId} value={g.genderId}>
                                                {g.description}
                                            </option>
                                        ))}
                                    </select>
                                </div>
                                <div className="form-row">
                                    <label>Race</label>
                                    <select
                                        value={editForm.raceId}
                                        onChange={(e) => setEditForm({ ...editForm, raceId: e.target.value })}
                                    >
                                        <option value="">Select…</option>
                                        {races.map((r) => (
                                            <option key={r.raceId} value={r.raceId}>
                                                {r.description}
                                            </option>
                                        ))}
                                    </select>
                                </div>
                            </div>
                        )}

                        <div className="toolbar" style={{ justifyContent: "flex-end", marginTop: "0.5rem" }}>
                            <button className="btn btn-ghost" type="button" onClick={() => setEditForm(null)}>
                                Cancel
                            </button>
                            <button className="btn" type="submit" disabled={saving}>
                                {saving ? "Saving…" : "Save changes"}
                            </button>
                        </div>
                    </form>
                </Modal>
            )}

            {deleting && (
                <ConfirmDialog
                    title="Delete profile"
                    message={`Delete ${deleting.firstName} ${deleting.lastName}? This cannot be undone.`}
                    confirmLabel="Delete"
                    onConfirm={confirmDelete}
                    onCancel={() => setDeleting(null)}
                />
            )}
        </div>
    );
}

export default UsersPage;
