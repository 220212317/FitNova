import { useEffect, useState, type FormEvent } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Dumbbell } from "lucide-react";
import { api } from "../../api/client";
import { lookupsApi } from "../../api/fitnova";
import { PROVINCE_NAMES, SA_PROVINCES } from "../../data/southAfrica";
import "./LoginPage.css";

const DEFAULT_GENDERS = ["Male", "Female", "Non-binary", "Other", "Prefer not to say"];
const PHONE_PATTERN = /^\+?[0-9 ]{9,15}$/;
const RELATIONSHIPS = ["Parent", "Spouse / Partner", "Sibling", "Child", "Friend", "Other"];

export default function SignUpPage() {
    const navigate = useNavigate();
    const [step, setStep] = useState<1 | 2 | 3 | 4>(1);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [dateOfBirth, setDateOfBirth] = useState("");
    const [streetNumber, setStreetNumber] = useState("");
    const [streetName, setStreetName] = useState("");
    const [suburbName, setSuburbName] = useState("");
    const [postalCode, setPostalCode] = useState("");
    const [province, setProvince] = useState("");
    const [city, setCity] = useState("");
    const [gender, setGender] = useState("");
    const [genderOptions, setGenderOptions] = useState<string[]>(DEFAULT_GENDERS);
    const [cellphone, setCellphone] = useState("");
    const [altCellphone, setAltCellphone] = useState("");
    const [kinFirstName, setKinFirstName] = useState("");
    const [kinLastName, setKinLastName] = useState("");
    const [kinRelationship, setKinRelationship] = useState("");
    const [kinCellphone, setKinCellphone] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        let active = true;
        lookupsApi.genders()
            .then((list) => {
                const names = (Array.isArray(list) ? list : []).map((g) => g.description).filter(Boolean);
                if (active && names.length) setGenderOptions(names);
            })
            .catch(() => undefined);
        return () => { active = false; };
    }, []);

    const continueToProfile = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setError("");
        if (!/^\S+@\S+\.\S+$/.test(email.trim())) {
            setError("Enter a valid email address.");
        } else if (password.length < 8 || !/[a-z]/.test(password) || !/[A-Z]/.test(password) || !/\d/.test(password)) {
            setError("Password must be at least 8 characters and include uppercase, lowercase, and a number.");
        } else if (password !== confirmPassword) {
            setError("Passwords do not match.");
        } else {
            setStep(2);
        }
    };

    const continueToAddress = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setError("");
        if (!firstName.trim() || !lastName.trim() || !dateOfBirth || !gender) {
            setError("Complete all profile fields, including gender.");
            return;
        }
        setStep(3);
    };

    const continueToContact = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setError("");
        if (!streetName.trim() || !province || !city) {
            setError("Enter your street name and select a province and city.");
        } else if (postalCode.trim() && !/^\d{4}$/.test(postalCode.trim())) {
            setError("Postal code must be 4 digits.");
        } else {
            setStep(4);
        }
    };

    const register = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setError("");
        if (!PHONE_PATTERN.test(cellphone.trim())) {
            setError("Enter a valid cellphone number.");
            return;
        }
        if (altCellphone.trim() && !PHONE_PATTERN.test(altCellphone.trim())) {
            setError("Enter a valid alternative cellphone number.");
            return;
        }
        if (!kinFirstName.trim() || !kinLastName.trim() || !kinRelationship) {
            setError("Complete the next of kin details.");
            return;
        }
        if (!PHONE_PATTERN.test(kinCellphone.trim())) {
            setError("Enter a valid next of kin cellphone number.");
            return;
        }

        setLoading(true);
        try {
            await api.post("/auth/register", {
                firstName: firstName.trim(),
                lastName: lastName.trim(),
                email: email.trim(),
                password,
                dateOfBirth,
                streetNumber: streetNumber.trim() || undefined,
                streetName: streetName.trim(),
                suburbName: suburbName.trim() || undefined,
                postalCode: postalCode.trim() || undefined,
                province,
                city,
                country: "South Africa",
                gender,
                cellphoneNumber: cellphone.trim(),
                alternativeCellphoneNumber: altCellphone.trim() || undefined,
                nextOfKin: {
                    firstName: kinFirstName.trim(),
                    lastName: kinLastName.trim(),
                    relationship: kinRelationship,
                    cellphoneNumber: kinCellphone.trim(),
                },
            });
            navigate("/login", { replace: true, state: { registered: true } });
        } catch (caught) {
            setError(caught instanceof Error ? caught.message : "Registration failed. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <main className="auth-page">
            <section className="auth-card" aria-labelledby="signup-title">
                <Link className="auth-brand" to="/"><span><Dumbbell size={20} /></span> FitNova</Link>
                <div className="auth-heading">
                    <p className="auth-eyebrow">STEP {step} OF 4</p>
                    <h1 id="signup-title">{step === 1 ? "Create your account" : step === 2 ? "Tell us about yourself" : step === 3 ? "Where do you live?" : "Contact & next of kin"}</h1>
                    <p>{step === 1 ? "Start with secure sign-in details." : step === 2 ? "Add the profile details used for your membership." : step === 3 ? "Your full residential address." : "How we reach you, and who to call in an emergency."}</p>
                </div>
                <div className="auth-progress" aria-label={`Registration step ${step} of 4`}>
                    <span className="active" /><span className={step >= 2 ? "active" : ""} /><span className={step >= 3 ? "active" : ""} /><span className={step === 4 ? "active" : ""} />
                </div>
                {error && <div className="auth-error" role="alert">{error}</div>}

                {step === 1 ? (
                    <form onSubmit={continueToProfile} noValidate>
                        <label htmlFor="signup-email">Email address</label>
                        <input id="signup-email" type="email" autoComplete="email" value={email} onChange={(event) => setEmail(event.target.value)} />
                        <label htmlFor="signup-password">Password</label>
                        <input id="signup-password" type="password" autoComplete="new-password" value={password} onChange={(event) => setPassword(event.target.value)} />
                        <label htmlFor="signup-confirm-password">Confirm password</label>
                        <input id="signup-confirm-password" type="password" autoComplete="new-password" value={confirmPassword} onChange={(event) => setConfirmPassword(event.target.value)} />
                        <button className="auth-primary" type="submit">Continue</button>
                    </form>
                ) : step === 2 ? (
                        <form onSubmit={continueToAddress} noValidate>
                        <label htmlFor="signup-first-name">First name</label>
                        <input id="signup-first-name" autoComplete="given-name" value={firstName} onChange={(event) => setFirstName(event.target.value)} />
                        <label htmlFor="signup-last-name">Last name</label>
                        <input id="signup-last-name" autoComplete="family-name" value={lastName} onChange={(event) => setLastName(event.target.value)} />
                        <label htmlFor="signup-dob">Date of birth</label>
                        <input id="signup-dob" type="date" autoComplete="bday" max={new Date().toISOString().slice(0, 10)} value={dateOfBirth} onChange={(event) => setDateOfBirth(event.target.value)} />
                        <label htmlFor="signup-gender">Gender</label>
                        <select id="signup-gender" value={gender} onChange={(event) => setGender(event.target.value)}>
                            <option value="">Select gender</option>
                            {genderOptions.map((name) => <option key={name} value={name}>{name}</option>)}
                        </select>
                        <div className="auth-actions">
                            <button className="auth-secondary" type="button" onClick={() => { setError(""); setStep(1); }}>Back</button>
                            <button className="auth-primary" type="submit">Continue</button>
                        </div>
                        </form>
                    ) : step === 3 ? (
                        <form onSubmit={continueToContact} noValidate>
                        <label htmlFor="signup-street-number">Street number</label>
                        <input id="signup-street-number" autoComplete="off" value={streetNumber} onChange={(event) => setStreetNumber(event.target.value)} />
                        <label htmlFor="signup-street-name">Street name</label>
                        <input id="signup-street-name" autoComplete="address-line1" value={streetName} onChange={(event) => setStreetName(event.target.value)} />
                        <label htmlFor="signup-suburb">Suburb</label>
                        <input id="signup-suburb" autoComplete="address-line2" value={suburbName} onChange={(event) => setSuburbName(event.target.value)} />
                        <label htmlFor="signup-province">Province</label>
                        <select id="signup-province" autoComplete="address-level1" value={province} onChange={(event) => { setProvince(event.target.value); setCity(""); }}>
                            <option value="">Select province</option>
                            {PROVINCE_NAMES.map((name) => <option key={name} value={name}>{name}</option>)}
                        </select>
                        {province && (
                            <>
                                <label htmlFor="signup-city">City</label>
                                <select id="signup-city" autoComplete="address-level2" value={city} onChange={(event) => setCity(event.target.value)}>
                                    <option value="">Select city</option>
                                    {SA_PROVINCES[province].map((name) => <option key={name} value={name}>{name}</option>)}
                                </select>
                            </>
                        )}
                        <label htmlFor="signup-postal-code">Postal code</label>
                        <input id="signup-postal-code" inputMode="numeric" maxLength={4} autoComplete="postal-code" value={postalCode} onChange={(event) => setPostalCode(event.target.value)} />
                        <label htmlFor="signup-country">Country</label>
                        <input id="signup-country" value="South Africa" readOnly />
                        <div className="auth-actions">
                            <button className="auth-secondary" type="button" onClick={() => { setError(""); setStep(2); }}>Back</button>
                            <button className="auth-primary" type="submit">Continue</button>
                        </div>
                        </form>
                    ) : (
                        <form onSubmit={register} noValidate>
                        <label htmlFor="signup-cellphone">Cellphone number</label>
                        <input id="signup-cellphone" type="tel" autoComplete="tel" placeholder="e.g. 0821234567" value={cellphone} onChange={(event) => setCellphone(event.target.value)} disabled={loading} />
                        <label htmlFor="signup-alt-cellphone">Alternative cellphone number (optional)</label>
                        <input id="signup-alt-cellphone" type="tel" value={altCellphone} onChange={(event) => setAltCellphone(event.target.value)} disabled={loading} />
                        <p className="auth-eyebrow" style={{ marginTop: "1rem" }}>NEXT OF KIN</p>
                        <label htmlFor="signup-kin-first-name">Next of kin first name</label>
                        <input id="signup-kin-first-name" value={kinFirstName} onChange={(event) => setKinFirstName(event.target.value)} disabled={loading} />
                        <label htmlFor="signup-kin-last-name">Next of kin last name</label>
                        <input id="signup-kin-last-name" value={kinLastName} onChange={(event) => setKinLastName(event.target.value)} disabled={loading} />
                        <label htmlFor="signup-kin-relationship">Relationship</label>
                        <select id="signup-kin-relationship" value={kinRelationship} onChange={(event) => setKinRelationship(event.target.value)} disabled={loading}>
                            <option value="">Select relationship</option>
                            {RELATIONSHIPS.map((name) => <option key={name} value={name}>{name}</option>)}
                        </select>
                        <label htmlFor="signup-kin-cellphone">Next of kin cellphone number</label>
                        <input id="signup-kin-cellphone" type="tel" value={kinCellphone} onChange={(event) => setKinCellphone(event.target.value)} disabled={loading} />
                        <div className="auth-actions">
                            <button className="auth-secondary" type="button" onClick={() => { setError(""); setStep(3); }} disabled={loading}>Back</button>
                            <button className="auth-primary" type="submit" disabled={loading}>{loading ? "Creating account…" : "Create account"}</button>
                        </div>
                        </form>
                )}
                <p className="auth-switch">Already have an account? <Link to="/login">Sign in</Link></p>
            </section>
        </main>
    );
}
