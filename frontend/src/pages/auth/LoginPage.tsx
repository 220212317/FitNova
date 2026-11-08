import { useState, type FormEvent } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { Dumbbell } from "lucide-react";
import { useAuth } from "../../auth/AuthContext";
import { ApiError } from "../../api/client";
import "./LoginPage.css";

export default function LoginPage() {
    const { login } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const registered = Boolean((location.state as { registered?: boolean } | null)?.registered);
    const destination = (location.state as { from?: string } | null)?.from ?? "/dashboard";

    const submit = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setError("");
        if (!email.trim() || !password) {
            setError("Enter both your email address and password.");
            return;
        }

        setLoading(true);
        try {
            await login(email.trim(), password);
            navigate(destination, { replace: true });
        } catch (caught) {
            if (caught instanceof ApiError && caught.status === 429) {
                setError("Too many login attempts. Please wait before trying again.");
            } else {
                setError(caught instanceof Error ? caught.message : "Login failed. Check your credentials and try again.");
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <main className="auth-page">
            <section className="auth-card" aria-labelledby="login-title">
                <Link className="auth-brand" to="/">
                    <span><Dumbbell size={20} /></span> FitNova
                </Link>
                <div className="auth-heading">
                    <p className="auth-eyebrow">WELCOME BACK</p>
                    <h1 id="login-title">Sign in to your account</h1>
                    <p>Manage your sessions and keep moving toward your goals.</p>
                </div>

                {registered && <div className="auth-success" role="status">Account created. You can now sign in.</div>}
                {error && <div className="auth-error" role="alert">{error}</div>}

                <form onSubmit={submit} noValidate>
                    <label htmlFor="login-email">Email address</label>
                    <input
                        id="login-email"
                        type="email"
                        autoComplete="email"
                        value={email}
                        onChange={(event) => setEmail(event.target.value)}
                        disabled={loading}
                    />
                    <label htmlFor="login-password">Password</label>
                    <input
                        id="login-password"
                        type="password"
                        autoComplete="current-password"
                        value={password}
                        onChange={(event) => setPassword(event.target.value)}
                        disabled={loading}
                    />
                    <button className="auth-primary" type="submit" disabled={loading}>
                        {loading ? "Signing in…" : "Sign in"}
                    </button>
                </form>
                <p className="auth-switch">New to FitNova? <Link to="/signup">Create an account</Link></p>
            </section>
        </main>
    );
}
