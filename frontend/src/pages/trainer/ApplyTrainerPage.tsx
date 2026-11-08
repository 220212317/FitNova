import type { CSSProperties } from "react";
import { useState, type FormEvent } from "react";
import { useToast } from "../../components/Toast";
import { useAuth } from "../../auth/AuthContext";

/** Local persistence until backend trainer-application endpoints exist */
const STORAGE_KEY = "fitnova_trainer_applications";

export function ApplyTrainerPage() {
  const { user } = useAuth();
  const { toast } = useToast();
  const [motivation, setMotivation] = useState("");
  const [experience, setExperience] = useState("");
  const [certifications, setCertifications] = useState("");
  const [submitted, setSubmitted] = useState(false);

  const submit = (e: FormEvent) => {
    e.preventDefault();
    if (!motivation.trim() || !experience.trim()) {
      toast("Please complete motivation and experience.", "error");
      return;
    }
    const existing = JSON.parse(localStorage.getItem(STORAGE_KEY) || "[]") as unknown[];
    const app = {
      applicationId: crypto.randomUUID(),
      userId: user?.userId,
      email: user?.email,
      motivation,
      experience,
      certifications,
      status: "PENDING",
      submittedAt: new Date().toISOString(),
    };
    localStorage.setItem(STORAGE_KEY, JSON.stringify([app, ...existing]));
    setSubmitted(true);
    toast("Application submitted for admin review.", "success");
  };

  if (submitted) {
    return (
      <div className="card" style={{ maxWidth: 520 }}>
        <h1 className="page-title">Application received</h1>
        <p className="page-sub">
          An admin will vet your experience and credentials. You will keep your member access
          until a trainer role is granted.
        </p>
      </div>
    );
  }

  return (
    <div style={{ maxWidth: 560 }}>
      <h1 className="page-title">Apply to become a trainer</h1>
      <p className="page-sub">
        All trainers start as regular accounts. Admins review applications, verify you, then
        assign trainer privileges.
      </p>
      <form className="card" onSubmit={submit}>
        <label style={labelStyle}>Why do you want to train members?</label>
        <textarea
          value={motivation}
          onChange={(e) => setMotivation(e.target.value)}
          rows={3}
          style={areaStyle}
          required
        />
        <label style={labelStyle}>Relevant experience</label>
        <textarea
          value={experience}
          onChange={(e) => setExperience(e.target.value)}
          rows={3}
          style={areaStyle}
          required
        />
        <label style={labelStyle}>Certifications (optional)</label>
        <textarea
          value={certifications}
          onChange={(e) => setCertifications(e.target.value)}
          rows={2}
          style={areaStyle}
          placeholder="e.g. NASM-CPT, CSCS…"
        />
        <button type="submit" className="btn" style={{ marginTop: "1rem" }}>
          Submit application
        </button>
      </form>
    </div>
  );
}

const labelStyle: CSSProperties = {
  display: "block",
  fontSize: "0.82rem",
  fontWeight: 600,
  color: "var(--fn-muted)",
  marginTop: "0.75rem",
  marginBottom: "0.35rem",
};

const areaStyle: CSSProperties = {
  width: "100%",
  background: "var(--fn-bg)",
  border: "1px solid var(--fn-border)",
  borderRadius: 10,
  padding: "0.7rem 0.85rem",
  resize: "vertical",
};
