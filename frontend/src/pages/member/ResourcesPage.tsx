import {
  useEffect,
  useMemo,
  useState,
  type CSSProperties,
  type FormEvent,
} from "react";
import { resourcesApi } from "../../api/fitnova";
import { useAuth } from "../../auth/AuthContext";
import { ConfirmDialog, Modal } from "../../components/Modal";
import { useToast } from "../../components/ToastContext";
import { ensureId } from "../../api/ids";
import type { GymResource, GymResourceCategory } from "../../types";

const FILTERS: { id: GymResourceCategory | "all"; label: string }[] = [
  { id: "all", label: "All" },
  { id: "manual", label: "Manuals" },
  { id: "nutrition", label: "Nutrition" },
  { id: "tip", label: "Tips" },
];

const CATEGORY_OPTIONS: { id: GymResourceCategory; label: string }[] = [
  { id: "manual", label: "Manual" },
  { id: "nutrition", label: "Nutrition" },
  { id: "tip", label: "Tip" },
];

interface FormState {
  resourceId: string;
  category: GymResourceCategory;
  title: string;
  summary: string;
  body: string;
  tags: string;
}

const EMPTY_FORM: FormState = {
  resourceId: "",
  category: "manual",
  title: "",
  summary: "",
  body: "",
  tags: "",
};

function toForm(resource: GymResource): FormState {
  return {
    resourceId: resource.resourceId,
    category: resource.category,
    title: resource.title,
    summary: resource.summary ?? "",
    body: resource.body,
    tags: resource.tags.join(", "),
  };
}

export function ResourcesPage() {
  const { user } = useAuth();
  const { toast } = useToast();
  const isAdmin = user?.role === "ADMIN";

  const [resources, setResources] = useState<GymResource[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [filter, setFilter] = useState<GymResourceCategory | "all">("all");
  const [openId, setOpenId] = useState<string | null>(null);

  const [form, setForm] = useState<FormState | null>(null);
  const [saving, setSaving] = useState(false);
  const [deleting, setDeleting] = useState<GymResource | null>(null);

  const fetchResources = async () => {
    try {
      setResources(await resourcesApi.getAll());
      setError(null);
    } catch (e) {
      setError(e instanceof Error ? e.message : "Could not load resources.");
    } finally {
      setLoading(false);
    }
  };

  const load = async () => {
    setLoading(true);
    await fetchResources();
  };

  useEffect(() => {
    let cancelled = false;

    resourcesApi
        .getAll()
        .then((data) => {
          if (cancelled) return;
          setResources(data);
          setError(null);
        })
        .catch((error: unknown) => {
          if (cancelled) return;
          setError(
              error instanceof Error ? error.message : "Could not load resources."
          );
        })
        .finally(() => {
          if (!cancelled) setLoading(false);
        });

    return () => {
      cancelled = true;
    };
  }, []);

  const items = useMemo(
      () =>
          filter === "all"
              ? resources
              : resources.filter((resource) => resource.category === filter),
      [filter, resources]
  );

  const save = async (e: FormEvent) => {
    e.preventDefault();
    if (!form) return;

    const payload: GymResource = {
      resourceId: form.resourceId,
      category: form.category,
      title: form.title.trim(),
      summary: form.summary.trim(),
      body: form.body.trim(),
      tags: form.tags
          .split(",")
          .map((tag) => tag.trim())
          .filter(Boolean),
    };

    setSaving(true);
    try {
      if (form.resourceId) {
        await resourcesApi.update(payload);
        toast("Resource updated.", "success");
      } else {
        await resourcesApi.create({ ...payload, resourceId: ensureId("") });
        toast("Resource added.", "success");
      }

      setForm(null);
      await load();
    } catch (err) {
      toast(err instanceof Error ? err.message : "Could not save resource.", "error");
    } finally {
      setSaving(false);
    }
  };

  const confirmDelete = async () => {
    if (!deleting) return;
    const target = deleting;

    try {
      await resourcesApi.delete(target.resourceId);
      toast("Resource deleted.", "success");
      await load();
    } catch (err) {
      toast(err instanceof Error ? err.message : "Could not delete resource.", "error");
    } finally {
      setDeleting(null);
    }
  };

  return (
      <div>
        <div
            style={{
              display: "flex",
              justifyContent: "space-between",
              gap: "1rem",
              flexWrap: "wrap",
            }}
        >
          <div>
            <h1 className="page-title">Gym resources</h1>
            <p className="page-sub">
              Manuals, nutrition guidance, and practical tips for members and trainers.
            </p>
          </div>
          {isAdmin && (
              <button type="button" className="btn" onClick={() => setForm({ ...EMPTY_FORM })}>
                New resource
              </button>
          )}
        </div>

        <div
            style={{
              display: "flex",
              gap: "0.5rem",
              flexWrap: "wrap",
              marginBottom: "1.25rem",
            }}
        >
          {FILTERS.map((filterItem) => (
              <button
                  key={filterItem.id}
                  type="button"
                  className={filter === filterItem.id ? "btn" : "btn btn-outline"}
                  style={{ padding: "0.45rem 0.95rem", fontSize: "0.85rem" }}
                  onClick={() => setFilter(filterItem.id)}
              >
                {filterItem.label}
              </button>
          ))}
        </div>

        {loading ? (
            <div className="skeleton" style={{ height: 140 }} />
        ) : error ? (
            <div className="card empty">
              <p style={{ marginBottom: "0.75rem" }}>{error}</p>
              <button type="button" className="btn btn-outline" onClick={() => void load()}>
                Try again
              </button>
            </div>
        ) : items.length === 0 ? (
            <div className="card empty">
              {resources.length === 0 && isAdmin
                  ? "No resources yet. Use New resource to add the first one."
                  : "No resources in this category yet."}
            </div>
        ) : (
            <div className="grid-2">
              {items.map((resource) => {
                const open = openId === resource.resourceId;

                return (
                    <article key={resource.resourceId} className="card">
                <span
                    style={{
                      fontSize: "0.72rem",
                      fontWeight: 700,
                      textTransform: "uppercase",
                      letterSpacing: "0.05em",
                      color: "var(--fn-primary)",
                    }}
                >
                  {resource.category}
                </span>

                      <h3 style={{ margin: "0.4rem 0 0.35rem", fontSize: "1.05rem" }}>
                        {resource.title}
                      </h3>

                      {resource.summary && (
                          <p
                              style={{
                                color: "var(--fn-muted)",
                                fontSize: "0.9rem",
                                marginBottom: "0.75rem",
                              }}
                          >
                            {resource.summary}
                          </p>
                      )}

                      {open && (
                          <p
                              style={{
                                fontSize: "0.92rem",
                                marginBottom: "0.75rem",
                                lineHeight: 1.6,
                              }}
                          >
                            {resource.body}
                          </p>
                      )}

                      <div
                          style={{
                            display: "flex",
                            gap: "0.4rem",
                            flexWrap: "wrap",
                            marginBottom: "0.75rem",
                          }}
                      >
                        {resource.tags.map((tag) => (
                            <span
                                key={tag}
                                style={{
                                  fontSize: "0.72rem",
                                  padding: "0.15rem 0.5rem",
                                  borderRadius: 999,
                                  background: "var(--fn-surface-2)",
                                  color: "var(--fn-muted)",
                                }}
                            >
                      {tag}
                    </span>
                        ))}
                      </div>

                      <div style={{ display: "flex", gap: "0.5rem", flexWrap: "wrap" }}>
                        <button
                            type="button"
                            className="btn btn-outline"
                            style={{ padding: "0.4rem 0.9rem", fontSize: "0.85rem" }}
                            onClick={() => setOpenId(open ? null : resource.resourceId)}
                        >
                          {open ? "Hide details" : "Read more"}
                        </button>

                        {isAdmin && (
                            <>
                              <button
                                  type="button"
                                  className="btn btn-outline"
                                  style={{ padding: "0.4rem 0.9rem", fontSize: "0.85rem" }}
                                  onClick={() => setForm(toForm(resource))}
                              >
                                Edit
                              </button>
                              <button
                                  type="button"
                                  className="btn btn-danger"
                                  style={{ padding: "0.4rem 0.9rem", fontSize: "0.85rem" }}
                                  onClick={() => setDeleting(resource)}
                              >
                                Delete
                              </button>
                            </>
                        )}
                      </div>
                    </article>
                );
              })}
            </div>
        )}

        {form && (
            <Modal
                title={form.resourceId ? "Edit resource" : "New resource"}
                subtitle="Shown to every member, trainer and admin."
                onClose={() => !saving && setForm(null)}
                width={560}
            >
              <form onSubmit={save} style={{ display: "grid", gap: "0.85rem" }}>
                <label style={labelStyle}>
                  Category
                  <select
                      value={form.category}
                      onChange={(e) =>
                          setForm({
                            ...form,
                            category: e.target.value as GymResourceCategory,
                          })
                      }
                      style={inputStyle}
                  >
                    {CATEGORY_OPTIONS.map((category) => (
                        <option key={category.id} value={category.id}>
                          {category.label}
                        </option>
                    ))}
                  </select>
                </label>

                <label style={labelStyle}>
                  Title
                  <input
                      value={form.title}
                      onChange={(e) => setForm({ ...form, title: e.target.value })}
                      required
                      maxLength={255}
                      style={inputStyle}
                  />
                </label>

                <label style={labelStyle}>
                  Summary
                  <input
                      value={form.summary}
                      onChange={(e) => setForm({ ...form, summary: e.target.value })}
                      maxLength={500}
                      style={inputStyle}
                  />
                </label>

                <label style={labelStyle}>
                  Details
                  <textarea
                      value={form.body}
                      onChange={(e) => setForm({ ...form, body: e.target.value })}
                      required
                      rows={6}
                      maxLength={4000}
                      style={{ ...inputStyle, resize: "vertical" }}
                  />
                </label>

                <label style={labelStyle}>
                  Tags (comma separated)
                  <input
                      value={form.tags}
                      onChange={(e) => setForm({ ...form, tags: e.target.value })}
                      placeholder="beginner, strength"
                      style={inputStyle}
                  />
                </label>

                <div style={{ display: "flex", justifyContent: "flex-end", gap: "0.5rem" }}>
                  <button
                      type="button"
                      className="btn btn-ghost"
                      disabled={saving}
                      onClick={() => setForm(null)}
                  >
                    Cancel
                  </button>
                  <button type="submit" className="btn" disabled={saving}>
                    {saving ? "Saving…" : form.resourceId ? "Save changes" : "Add resource"}
                  </button>
                </div>
              </form>
            </Modal>
        )}

        {deleting && (
            <ConfirmDialog
                title="Delete resource"
                message={`Delete "${deleting.title}"? Members and trainers will no longer see it.`}
                confirmLabel="Delete"
                onConfirm={() => void confirmDelete()}
                onCancel={() => setDeleting(null)}
            />
        )}
      </div>
  );
}

const labelStyle: CSSProperties = {
  display: "grid",
  gap: "0.3rem",
  fontSize: "0.85rem",
  color: "var(--fn-muted)",
};

const inputStyle: CSSProperties = {
  background: "var(--fn-bg)",
  border: "1px solid var(--fn-border)",
  borderRadius: 10,
  padding: "0.6rem 0.75rem",
  color: "var(--text)",
  font: "inherit",
};