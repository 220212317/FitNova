/*
 * LookupPicker.tsx
 * Author: Inga Plati
 * 230126634
 */
import type { CSSProperties } from "react";
import { useState, type FormEvent } from "react";
import { ConfirmDialog } from "../../components/Modal";
import {
  CUSTOM_OPTION,
  cleanName,
  duplicateMessage,
  findDuplicate,
  remainingPresets,
  type LookupItem,
} from "./lookupNames";

interface LookupPickerProps {
  title: string;
  noun: string;
  presets: string[];
  items: LookupItem[];
  onCreate: (description: string) => Promise<void>;
  onUpdate: (item: LookupItem) => Promise<void>;
  onDelete: (item: LookupItem) => Promise<void>;
}

export function LookupPicker({
  title,
  noun,
  presets,
  items,
  onCreate,
  onUpdate,
  onDelete,
}: LookupPickerProps) {
  const [choice, setChoice] = useState("");
  const [customValue, setCustomValue] = useState("");
  const [addError, setAddError] = useState<string | null>(null);
  const [editingId, setEditingId] = useState<string | null>(null);
  const [editValue, setEditValue] = useState("");
  const [editError, setEditError] = useState<string | null>(null);
  const [deleting, setDeleting] = useState<LookupItem | null>(null);
  const [busy, setBusy] = useState(false);

  const available = remainingPresets(presets, items);
  const isCustom = choice === CUSTOM_OPTION;
  const pendingValue = cleanName(isCustom ? customValue : choice);

  const describe = (err: unknown) =>
    err instanceof Error ? err.message : `Could not save the ${noun}.`;

  const add = async (e: FormEvent) => {
    e.preventDefault();
    setAddError(null);

    const value = pendingValue;
    if (!value) {
      setAddError(`Choose or type a ${noun} first.`);
      return;
    }

    const clash = findDuplicate(items, value);
    if (clash) {
      setAddError(duplicateMessage(noun, clash));
      return;
    }

    setBusy(true);
    try {
      await onCreate(value);
      setChoice("");
      setCustomValue("");
    } catch (err) {
      setAddError(describe(err));
    } finally {
      setBusy(false);
    }
  };

  const startEdit = (item: LookupItem) => {
    setEditingId(item.id);
    setEditValue(item.description);
    setEditError(null);
  };

  const saveEdit = async (item: LookupItem) => {
    const value = cleanName(editValue);
    if (!value) {
      setEditError(`A ${noun} needs a name.`);
      return;
    }

    if (value === item.description) {
      setEditingId(null);
      setEditError(null);
      return;
    }

    const clash = findDuplicate(items, value, item.id);
    if (clash) {
      setEditError(duplicateMessage(noun, clash));
      return;
    }

    setBusy(true);
    try {
      await onUpdate({ id: item.id, description: value });
      setEditingId(null);
      setEditError(null);
    } catch (err) {
      setEditError(describe(err));
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
      setDeleting(null);
    } catch (err) {
      setDeleting(null);
      setAddError(describe(err));
    } finally {
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
                  onChange={(e) => {
                    setEditValue(e.target.value);
                    setEditError(null);
                  }}
                  onKeyDown={(e) => {
                    if (e.key === "Enter") void saveEdit(item);
                    if (e.key === "Escape") {
                      setEditingId(null);
                      setEditError(null);
                    }
                  }}
                  style={inputStyle}
                />
                <button
                  type="button"
                  className="btn"
                  style={smallBtn}
                  disabled={busy}
                  onClick={() => void saveEdit(item)}
                >
                  Save
                </button>
                <button
                  type="button"
                  className="btn btn-ghost"
                  style={smallBtn}
                  onClick={() => {
                    setEditingId(null);
                    setEditError(null);
                  }}
                >
                  Cancel
                </button>
              </>
            ) : (
              <>
                <span style={{ flex: 1 }}>{item.description}</span>
                <button
                  type="button"
                  className="btn btn-outline"
                  style={smallBtn}
                  disabled={busy}
                  onClick={() => startEdit(item)}
                >
                  Edit
                </button>
                <button
                  type="button"
                  className="btn btn-danger"
                  style={smallBtn}
                  disabled={busy}
                  onClick={() => setDeleting(item)}
                >
                  Delete
                </button>
              </>
            )}
          </li>
        ))}
        {items.length === 0 && <li style={{ color: "var(--fn-muted)" }}>No entries yet.</li>}
      </ul>

      {editingId !== null && editError && (
        <p role="alert" style={errorStyle}>
          {editError}
        </p>
      )}

      <form onSubmit={add} style={{ display: "flex", flexWrap: "wrap", gap: "0.5rem" }}>
        <select
          aria-label={`New ${noun}`}
          value={choice}
          onChange={(e) => {
            setChoice(e.target.value);
            setAddError(null);
          }}
          style={inputStyle}
        >
          <option value="">{`Choose a ${noun}...`}</option>
          {available.map((preset) => (
            <option key={preset} value={preset}>
              {preset}
            </option>
          ))}
          <option value={CUSTOM_OPTION}>Something else...</option>
        </select>

        {isCustom && (
          <input
            aria-label={`Custom ${noun}`}
            value={customValue}
            autoFocus
            placeholder={`New ${noun}`}
            onChange={(e) => {
              setCustomValue(e.target.value);
              setAddError(null);
            }}
            style={inputStyle}
          />
        )}

        <button type="submit" className="btn" disabled={busy || !pendingValue}>
          Add
        </button>
      </form>

      {addError && (
        <p role="alert" style={errorStyle}>
          {addError}
        </p>
      )}

      {available.length === 0 && !isCustom && (
        <p style={{ color: "var(--fn-muted)", fontSize: "0.82rem", marginTop: "0.5rem" }}>
          Every suggested {noun} has been added. Pick "Something else..." to add your own.
        </p>
      )}

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

const row: CSSProperties = {
  display: "flex",
  alignItems: "center",
  gap: "0.5rem",
  padding: "0.5rem 0",
  borderBottom: "1px solid var(--fn-border)",
  fontSize: "0.92rem",
};

const smallBtn: CSSProperties = {
  padding: "0.3rem 0.7rem",
  fontSize: "0.8rem",
};

const inputStyle: CSSProperties = {
  flex: 1,
  minWidth: 160,
  background: "var(--fn-bg)",
  border: "1px solid var(--fn-border)",
  borderRadius: 10,
  padding: "0.55rem 0.75rem",
};

const errorStyle: CSSProperties = {
  color: "var(--fn-danger, #e5484d)",
  fontSize: "0.82rem",
  marginTop: "0.5rem",
};
