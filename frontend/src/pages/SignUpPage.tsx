import { useState, type FormEvent } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Dumbbell } from "lucide-react";
import { api } from "../api/client";
import "./LoginPage.css";

export default function SignUpPage() {
    const navigate = useNavigate();
    const [step, setStep] = useState<1 | 2>(1);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [dateOfBirth, setDateOfBirth] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

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

    const register = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setError("");
        if (!firstName.trim() || !lastName.trim() || !dateOfBirth) {
            setError("Complete all profile fields.");
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
                    <p className="auth-eyebrow">STEP {step} OF 2</p>
                    <h1 id="signup-title">{step === 1 ? "Create your account" : "Tell us about yourself"}</h1>
                    <p>{step === 1 ? "Start with secure sign-in details." : "Add the profile details used for your membership."}</p>
                </div>
                <div className="auth-progress" aria-label={`Registration step ${step} of 2`}>
                    <span className="active" /><span className={step === 2 ? "active" : ""} />
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
                ) : (
                    <form onSubmit={register} noValidate>
                        <label htmlFor="signup-first-name">First name</label>
                        <input id="signup-first-name" autoComplete="given-name" value={firstName} onChange={(event) => setFirstName(event.target.value)} disabled={loading} />
                        <label htmlFor="signup-last-name">Last name</label>
                        <input id="signup-last-name" autoComplete="family-name" value={lastName} onChange={(event) => setLastName(event.target.value)} disabled={loading} />
                        <label htmlFor="signup-dob">Date of birth</label>
                        <input id="signup-dob" type="date" autoComplete="bday" max={new Date().toISOString().slice(0, 10)} value={dateOfBirth} onChange={(event) => setDateOfBirth(event.target.value)} disabled={loading} />
                        <div className="auth-actions">
                            <button className="auth-secondary" type="button" onClick={() => { setError(""); setStep(1); }} disabled={loading}>Back</button>
                            <button className="auth-primary" type="submit" disabled={loading}>{loading ? "Creating account…" : "Create account"}</button>
                        </div>
                    </form>
                )}
                <p className="auth-switch">Already have an account? <Link to="/login">Sign in</Link></p>
            </section>
        </main>
    );
}
