import { useEffect, useState } from "react";
import { useToast } from "../../components/Toast";
import { adminApi } from "../../api/fitnova";

interface StoredApp {
  applicationId: string;
  userId?: string;
  email?: string;
  motivation: string;
  experience: string;
  certifications?: string;
  status: string;
  submittedAt: string;
}

const STORAGE_KEY = "fitnova_trainer_applications";

/**
 * Applications are a frontend workflow helper.
 * Approval calls backend POST /admin/roles/{userId}/assign { role: "TRAINER" }.
 */
export function AdminApplicationsPage() {
  const { toast } = useToast();
  const [apps, setApps] = useState<StoredApp[]>([]);

  const reload = () => {
    setApps(JSON.parse(localStorage.getItem(STORAGE_KEY) || "[]") as StoredApp[]);
  };

  useEffect(() => {
    reload();
  }, []);

  const persist = (next: StoredApp[]) => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(next));
    setApps(next);
  };

  const approve = async (app: StoredApp) => {
    if (!app.userId) {
      toast("Application has no userId — cannot assign role.", "error");
      return;
    }
    try {
      await adminApi.assignRole(app.userId, "TRAINER");
      persist(
          apps.map((a) =>
              a.applicationId === app.applicationId ? { ...a, status: "APPROVED" } : a
          )
      );
      toast("TRAINER role granted via /admin/roles/.../assign.", "success");
    } catch (e) {
      toast(e instanceof Error ? e.message : "Approval failed.", "error");
    }
  };

  const reject = (app: StoredApp) => {
    persist(
        apps.map((a) =>
            a.applicationId === app.applicationId ? { ...a, status: "REJECTED" } : a
        )
    );
    toast("Application rejected.", "info");
  };

  return (
      <div>
        <h1 className="page-title">Trainer applications</h1>
        <p className="page-sub">
          Member applications (stored locally until a dedicated backend API exists). Approve calls the
          real admin role-assign endpoint.
        </p>

        {apps.length === 0 ? (
            <div className="card empty">No applications submitted yet.</div>
        ) : (
            <div className="grid-2">
              {apps.map((a) => (
                  <article key={a.applicationId} className="card">
                    <div style={{ display: "flex", justifyContent: "space-between", gap: "0.5rem" }}>
                      <strong>{a.email || a.userId || "Applicant"}</strong>
                      <span
                          className={`badge badge-${
                              a.status === "PENDING" ? "pending" : a.status === "APPROVED" ? "accepted" : "rejected"
                          }`}
                      >
                  {a.status}
                </span>
                    </div>
                    <p style={{ fontSize: "0.8rem", color: "var(--fn-muted)", margin: "0.35rem 0 0.75rem" }}>
                      Submitted {new Date(a.submittedAt).toLocaleString()}
                    </p>
                    <p style={{ fontSize: "0.9rem", marginBottom: "0.5rem" }}>
                      <strong>Motivation:</strong> {a.motivation}
                    </p>
                    <p style={{ fontSize: "0.9rem", marginBottom: "0.5rem" }}>
                      <strong>Experience:</strong> {a.experience}
                    </p>
                    {a.certifications && (
                        <p style={{ fontSize: "0.9rem", marginBottom: "0.75rem" }}>
                          <strong>Certifications:</strong> {a.certifications}
                        </p>
                    )}
                    {a.status === "PENDING" && (
                        <div style={{ display: "flex", gap: "0.5rem" }}>
                          <button type="button" className="btn" onClick={() => void approve(a)}>
                            Approve &amp; grant trainer
                          </button>
                          <button type="button" className="btn btn-outline" onClick={() => reject(a)}>
                            Reject
                          </button>
                        </div>
                    )}
                  </article>
              ))}
            </div>
        )}
      </div>
  );
}
