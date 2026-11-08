import type { CSSProperties } from "react";
import { useEffect, useState, type FormEvent } from "react";
import { lookupsApi } from "../../api/fitnova";
import { ConfirmDialog } from "../../components/Modal";
import { useToast } from "../../components/Toast";
import type { Gender, Race } from "../../types";
import { ensureId } from "../../api/ids";

interface LookupItem {
  id: string;
  description: string;
}

interface LookupCardProps {
  title: string;
  noun: string;
  items: LookupItem[];
  onCreate: (description: string) => Promise<void>;
  onUpdate: (item: LookupItem) => Promise<void>;
  onDelete: (item: LookupItem) => Promise<void>;
}

/** One lookup list (gender / race) with add, rename and delete. */
function LookupCard({ title, noun, items, onCreate, onUpdate, onDelete }: LookupCardProps) {
  const [newValue, setNewValue] = useState("");
  const [editingId, setEditingId] = useState<string | null>(null);
  const [editValue, setEditValue] = useState("");
  const [deleting, setDeleting] = useState<LookupItem | null>(null);
  const [busy, setBusy] = useState(false);

  const add = async (e: FormEvent) => {
    e.preventDefault();
    const value = newValue.trim();
    if (!value) return;
    setBusy(true);
    try {
      await onCreate(value);
      setNewValue("");
    } finally {
      setBusy(false);
    }
  };

  const startEdit = (item: LookupItem) => {
    setEditingId(item.id);
    setEditValue(item.description);
  };

  const saveEdit = async (item: LookupItem) => {
    const value = editValue.trim();
    if (!value) return;
    if (value === item.description) {
      setEditingId(null);
      return;
    }
    setBusy(true);
    try {
      await onUpdate({ id: item.id, description: value });
      setEditingId(null);
    } finally {
      setBusy(false);
    }
  };

  const confirmDelete = async () => {
    if (!deleting) return;
    const target = deleting;
    setBusy(true);
    try {
      await onDelete(target);
    } finally {
      setDeleting(null);
      setBusy(false);
    }
  };

  return (
      <section className="card">
        <h2 style={{ fontSize: "1rem", marginBottom: "0.75rem" }}>{title}</h2>
        <ul style={{ listStyle: "none", marginBottom: "1rem", padding: 0 }}>
          {items.map((item) => (
              <li key={item.id} style={row}>
                {editingId === item.id ? (
                    <>
                      <input
                          aria-label={`Edit ${noun}`}
                          value={editValue}
                          autoFocus
                          onChange={(e) => setEditValue(e.target.value)}
                          onKeyDown={(e) => {
                            if (e.key === "Enter") void saveEdit(item);
                            if (e.key === "Escape") setEditingId(null);
                          }}
                          style={inputStyle}
                      />
                      <button type="button" className="btn" style={smallBtn} disabled={busy} onClick={() => void saveEdit(item)}>
                        Save
                      </button>
                      <button type="button" className="btn btn-ghost" style={smallBtn} onClick={() => setEditingId(null)}>
                        Cancel
                      </button>
                    </>
                ) : (
                    <>
                      <span style={{ flex: 1 }}>{item.description}</span>
                      <button type="button" className="btn btn-outline" style={smallBtn} disabled={busy} onClick={() => startEdit(item)}>
                        Edit
                      </button>
                      <button type="button" className="btn btn-danger" style={smallBtn} disabled={busy} onClick={() => setDeleting(item)}>
                        Delete
                      </button>
                    </>
                )}
              </li>
          ))}
          {items.length === 0 && <li style={{ color: "var(--fn-muted)" }}>No entries yet.</li>}
        </ul>

        <form onSubmit={add} style={{ display: "flex", gap: "0.5rem" }}>
          <input
              value={newValue}
              onChange={(e) => setNewValue(e.target.value)}
              placeholder={`New ${noun}`}
              aria-label={`New ${noun}`}
              style={inputStyle}
          />
          <button type="submit" className="btn" disabled={busy || !newValue.trim()}>
            Add
          </button>
        </form>

        {deleting && (
            <ConfirmDialog
                title={`Delete ${noun}`}
                message={`Delete "${deleting.description}"? This only works if no member profile still uses it.`}
                confirmLabel="Delete"
                onConfirm={() => void confirmDelete()}
                onCancel={() => setDeleting(null)}
            />
        )}
      </section>
  );
}

export function AdminLookupsPage() {
  const { toast } = useToast();
  const [genders, setGenders] = useState<Gender[]>([]);
  const [races, setRaces] = useState<Race[]>([]);
  const [loading, setLoading] = useState(true);

  const load = async () => {
    setLoading(true);
    try {
      const [g, r] = await Promise.all([
        lookupsApi.genders().catch(() => [] as Gender[]),
        lookupsApi.races().catch(() => [] as Race[]),
      ]);
      setGenders(g);
      setRaces(r);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void load();
  }, []);

  /** Runs an API call, toasts the outcome and refreshes the lists. */
  const run = async (action: () => Promise<unknown>, success: string) => {
    try {
      await action();
      toast(success, "success");
      await load();
    } catch (err) {
      toast(err instanceof Error ? err.message : "Request failed.", "error");
    }
  };

  return (
      <div>
        <h1 className="page-title">Lookups</h1>
        <p className="page-sub">Admin-only reference data used across member profiles.</p>

        {loading ? (
            <div className="skeleton" style={{ height: 100 }} />
        ) : (
            <div className="grid-2">
              <LookupCard
                  title="Genders"
                  noun="gender"
                  items={genders.map((g) => ({ id: g.genderId, description: g.description }))}
                  onCreate={(description) =>
                      run(
                          () => lookupsApi.createGender({ genderId: ensureId(""), description }),
                          "Gender added."
                      )
                  }
                  onUpdate={(item) =>
                      run(
                          () => lookupsApi.updateGender({ genderId: item.id, description: item.description }),
                          "Gender updated."
                      )
                  }
                  onDelete={(item) => run(() => lookupsApi.deleteGender(item.id), "Gender deleted.")}
              />
              <LookupCard
                  title="Races"
                  noun="race"
                  items={races.map((r) => ({ id: r.raceId, description: r.description }))}
                  onCreate={(description) =>
                      run(() => lookupsApi.createRace({ raceId: ensureId(""), description }), "Race added.")
                  }
                  onUpdate={(item) =>
                      run(
                          () => lookupsApi.updateRace({ raceId: item.id, description: item.description }),
                          "Race updated."
                      )
                  }
                  onDelete={(item) => run(() => lookupsApi.deleteRace(item.id), "Race deleted.")}
              />
            </div>
        )}
      </div>
  );
}

const row: CSSProperties = {
  display: "flex",
  alignItems: "center",
  gap: "0.5rem",
  padding: "0.5rem 0",
  borderBottom: "1px solid var(--fn-border)",
  fontSize: "0.92rem",
};

const smallBtn: CSSProperties = { padding: "0.3rem 0.7rem", fontSize: "0.8rem" };

const inputStyle: CSSProperties = {
  flex: 1,
  background: "var(--fn-bg)",
  border: "1px solid var(--fn-border)",
  borderRadius: 10,
  padding: "0.55rem 0.75rem",
};
