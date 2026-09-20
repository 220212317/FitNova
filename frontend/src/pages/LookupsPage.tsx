import { useEffect, useState } from "react";
import type { FormEvent } from "react";
import { AlertTriangle, ListTree, Plus, Trash2 } from "lucide-react";
import { lookupsApi, userRolesApi, usersApi } from "../api/fitnova";
import { ConfirmDialog } from "../components/Modal";
import { Toast } from "../components/Toast";
import type { Gender, Race, RoleType, User, UserRole } from "../types";

type Tab = "gender" | "race" | "roles";
const ROLE_OPTIONS: RoleType[] = ["MEMBER", "TRAINER", "ADMIN"];

export function LookupsPage() {
  const [tab, setTab] = useState<Tab>("gender");
  const [genders, setGenders] = useState<Gender[]>([]);
  const [races, setRaces] = useState<Race[]>([]);
  const [roles, setRoles] = useState<UserRole[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [message, setMessage] = useState<string | null>(null);
  const [saving, setSaving] = useState(false);

  const [newGender, setNewGender] = useState("");
  const [newRace, setNewRace] = useState("");
  const [roleUserId, setRoleUserId] = useState("");
  const [roleType, setRoleType] = useState<RoleType>("MEMBER");

  const [deleteGender, setDeleteGender] = useState<Gender | null>(null);
  const [deleteRace, setDeleteRace] = useState<Race | null>(null);
  const [deleteRole, setDeleteRole] = useState<UserRole | null>(null);

  async function fetchLookups() {
    const [g, r, ur, u] = await Promise.all([
      lookupsApi.genders(),
      lookupsApi.races(),
      userRolesApi.getAll(),
      usersApi.getAll(),
    ]);

    setGenders(Array.isArray(g) ? g : []);
    setRaces(Array.isArray(r) ? r : []);
    setRoles(Array.isArray(ur) ? ur : []);
    setUsers(Array.isArray(u) ? u : []);
  }

  async function load() {
    setLoading(true);
    setError(null);

    try {
      await fetchLookups();
    } catch (e) {
      setError(e instanceof Error ? e.message : "Failed to load lookups");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    let cancelled = false;

    async function initialLoad() {
      try {
        const [g, r, ur, u] = await Promise.all([
          lookupsApi.genders(),
          lookupsApi.races(),
          userRolesApi.getAll(),
          usersApi.getAll(),
        ]);

        if (cancelled) return;

        setGenders(Array.isArray(g) ? g : []);
        setRaces(Array.isArray(r) ? r : []);
        setRoles(Array.isArray(ur) ? ur : []);
        setUsers(Array.isArray(u) ? u : []);
      } catch (e) {
        if (!cancelled) {
          setError(e instanceof Error ? e.message : "Failed to load lookups");
        }
      } finally {
        if (!cancelled) {
          setLoading(false);
        }
      }
    }

    void initialLoad();

    return () => {
      cancelled = true;
    };
  }, []);

  async function addGender(e: FormEvent) {
    e.preventDefault();
    if (!newGender.trim()) return;
    setSaving(true);
    setError(null);
    try {
      await lookupsApi.createGender({ genderId: "", description: newGender.trim() });
      setNewGender("");
      setMessage("Gender added.");
      await load();
    } catch (e) {
      setError(e instanceof Error ? e.message : "Could not add gender");
    } finally {
      setSaving(false);
    }
  }

  async function addRace(e: FormEvent) {
    e.preventDefault();
    if (!newRace.trim()) return;
    setSaving(true);
    setError(null);
    try {
      await lookupsApi.createRace({ raceId: "", description: newRace.trim() });
      setNewRace("");
      setMessage("Race added.");
      await load();
    } catch (e) {
      setError(e instanceof Error ? e.message : "Could not add race");
    } finally {
      setSaving(false);
    }
  }

  async function addRole(e: FormEvent) {
    e.preventDefault();
    const user = users.find((u) => u.userId === roleUserId);
    if (!user) return;
    setSaving(true);
    setError(null);
    try {
      await userRolesApi.create({
        userRoleId: "",
        user,
        roleId: roleType,
        description: `${roleType} role for ${user.firstName} ${user.lastName}`,
      });
      setRoleUserId("");
      setMessage("Role assigned.");
      await load();
    } catch (e) {
      setError(e instanceof Error ? e.message : "Could not assign role");
    } finally {
      setSaving(false);
    }
  }

  async function doDeleteGender() {
    if (!deleteGender) return;
    setSaving(true);
    try {
      await lookupsApi.deleteGender(deleteGender.genderId);
      setDeleteGender(null);
      setMessage("Gender removed.");
      await load();
    } catch (e) {
      setError(e instanceof Error ? e.message : "Delete failed");
    } finally {
      setSaving(false);
    }
  }

  async function doDeleteRace() {
    if (!deleteRace) return;
    setSaving(true);
    try {
      await lookupsApi.deleteRace(deleteRace.raceId);
      setDeleteRace(null);
      setMessage("Race removed.");
      await load();
    } catch (e) {
      setError(e instanceof Error ? e.message : "Delete failed");
    } finally {
      setSaving(false);
    }
  }

  async function doDeleteRole() {
    if (!deleteRole) return;
    setSaving(true);
    try {
      await userRolesApi.delete(deleteRole.userRoleId);
      setDeleteRole(null);
      setMessage("Role removed.");
      await load();
    } catch (e) {
      setError(e instanceof Error ? e.message : "Delete failed");
    } finally {
      setSaving(false);
    }
  }

  return (
      <div>
        <h1 className="page-title">Lookups</h1>
        <p className="page-sub">Reference data used across profiles: genders, races, and role assignments.</p>

        {error && (
            <div className="alert alert-error">
              <AlertTriangle />
              <span>{error}</span>
            </div>
        )}
        <Toast message={message} onDismiss={() => setMessage(null)} />

        <div className="tabs">
          <button className={tab === "gender" ? "tab active" : "tab"} onClick={() => setTab("gender")} type="button">
            Gender
          </button>
          <button className={tab === "race" ? "tab active" : "tab"} onClick={() => setTab("race")} type="button">
            Race
          </button>
          <button className={tab === "roles" ? "tab active" : "tab"} onClick={() => setTab("roles")} type="button">
            Roles
          </button>
        </div>

        {tab === "gender" && (
            <div className="card">
              <form onSubmit={addGender} className="inline-form">
                <input
                    placeholder="e.g. Female"
                    value={newGender}
                    onChange={(e) => setNewGender(e.target.value)}
                />
                <button className="btn" type="submit" disabled={saving}>
                  <Plus size={16} /> Add gender
                </button>
              </form>
              <div className="table-wrap" style={{ marginTop: "1rem" }}>
                {loading ? (
                    <div className="skeleton skeleton-row" />
                ) : genders.length === 0 ? (
                    <div className="empty-state">
                      <ListTree />
                      <strong>No genders yet</strong>
                      <span>Add one above so it's available when building a profile.</span>
                    </div>
                ) : (
                    <table>
                      <thead>
                      <tr>
                        <th>Description</th>
                        <th></th>
                      </tr>
                      </thead>
                      <tbody>
                      {genders.map((g) => (
                          <tr key={g.genderId}>
                            <td>{g.description}</td>
                            <td className="row-actions">
                              <button className="btn-icon danger" type="button" onClick={() => setDeleteGender(g)}>
                                <Trash2 /> Delete
                              </button>
                            </td>
                          </tr>
                      ))}
                      </tbody>
                    </table>
                )}
              </div>
            </div>
        )}

        {tab === "race" && (
            <div className="card">
              <form onSubmit={addRace} className="inline-form">
                <input
                    placeholder="e.g. Black African"
                    value={newRace}
                    onChange={(e) => setNewRace(e.target.value)}
                />
                <button className="btn" type="submit" disabled={saving}>
                  <Plus size={16} /> Add race
                </button>
              </form>
              <div className="table-wrap" style={{ marginTop: "1rem" }}>
                {loading ? (
                    <div className="skeleton skeleton-row" />
                ) : races.length === 0 ? (
                    <div className="empty-state">
                      <ListTree />
                      <strong>No races yet</strong>
                      <span>Add one above so it's available when building a profile.</span>
                    </div>
                ) : (
                    <table>
                      <thead>
                      <tr>
                        <th>Description</th>
                        <th></th>
                      </tr>
                      </thead>
                      <tbody>
                      {races.map((r) => (
                          <tr key={r.raceId}>
                            <td>{r.description}</td>
                            <td className="row-actions">
                              <button className="btn-icon danger" type="button" onClick={() => setDeleteRace(r)}>
                                <Trash2 /> Delete
                              </button>
                            </td>
                          </tr>
                      ))}
                      </tbody>
                    </table>
                )}
              </div>
            </div>
        )}

        {tab === "roles" && (
            <div className="card">
              <form onSubmit={addRole} className="inline-form">
                <select value={roleUserId} onChange={(e) => setRoleUserId(e.target.value)} required>
                  <option value="">Select a person…</option>
                  {users.map((u) => (
                      <option key={u.userId} value={u.userId}>
                        {u.firstName} {u.lastName}
                      </option>
                  ))}
                </select>
                <select value={roleType} onChange={(e) => setRoleType(e.target.value as RoleType)}>
                  {ROLE_OPTIONS.map((r) => (
                      <option key={r} value={r}>
                        {r}
                      </option>
                  ))}
                </select>
                <button className="btn" type="submit" disabled={saving}>
                  <Plus size={16} /> Assign role
                </button>
              </form>
              <div className="table-wrap" style={{ marginTop: "1rem" }}>
                {loading ? (
                    <div className="skeleton skeleton-row" />
                ) : roles.length === 0 ? (
                    <div className="empty-state">
                      <ListTree />
                      <strong>No roles assigned yet</strong>
                      <span>Assign MEMBER, TRAINER, or ADMIN to a person above.</span>
                    </div>
                ) : (
                    <table>
                      <thead>
                      <tr>
                        <th>Person</th>
                        <th>Role</th>
                        <th></th>
                      </tr>
                      </thead>
                      <tbody>
                      {roles.map((r) => (
                          <tr key={r.userRoleId}>
                            <td>{r.user ? `${r.user.firstName} ${r.user.lastName}` : "—"}</td>
                            <td>
                              <span className={`badge ${r.roleId.toLowerCase()}`}>{r.roleId}</span>
                            </td>
                            <td className="row-actions">
                              <button className="btn-icon danger" type="button" onClick={() => setDeleteRole(r)}>
                                <Trash2 /> Delete
                              </button>
                            </td>
                          </tr>
                      ))}
                      </tbody>
                    </table>
                )}
              </div>
            </div>
        )}

        {deleteGender && (
            <ConfirmDialog
                title="Delete gender"
                message={`Delete "${deleteGender.description}"? Profiles using it may be affected.`}
                confirmLabel="Delete"
                onConfirm={doDeleteGender}
                onCancel={() => setDeleteGender(null)}
            />
        )}
        {deleteRace && (
            <ConfirmDialog
                title="Delete race"
                message={`Delete "${deleteRace.description}"? Profiles using it may be affected.`}
                confirmLabel="Delete"
                onConfirm={doDeleteRace}
                onCancel={() => setDeleteRace(null)}
            />
        )}
        {deleteRole && (
            <ConfirmDialog
                title="Remove role"
                message={`Remove ${deleteRole.roleId} from ${deleteRole.user?.firstName ?? "this person"}?`}
                confirmLabel="Remove"
                onConfirm={doDeleteRole}
                onCancel={() => setDeleteRole(null)}
            />
        )}
      </div>
  );
}