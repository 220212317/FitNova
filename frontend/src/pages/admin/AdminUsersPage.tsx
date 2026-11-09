import type { CSSProperties } from "react";
import { useEffect, useMemo, useState, type FormEvent } from "react";
import { adminApi, usersApi } from "../../api/fitnova";
import { useAuth } from "../../auth/AuthContext";
import { ConfirmDialog } from "../../components/Modal";
import { useToast } from "../../components/ToastContext";
import type { User, RoleType } from "../../types";

const ROLES: RoleType[] = ["MEMBER", "TRAINER", "ADMIN"];

export function AdminUsersPage() {
  const { toast } = useToast();
  const { user: me } = useAuth();
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [busy, setBusy] = useState<string | null>(null);

  const [deleting, setDeleting] = useState<User | null>(null);
  const [roleChange, setRoleChange] = useState<{
    user: User;
    role: RoleType;
  } | null>(null);

  const [query, setQuery] = useState("");
  const [roleFilter, setRoleFilter] = useState<RoleType | "ALL">("ALL");

  const [showStaff, setShowStaff] = useState(false);
  const [staffRole, setStaffRole] = useState<"TRAINER" | "ADMIN">("TRAINER");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [dob, setDob] = useState("");
  const [creating, setCreating] = useState(false);

  const fetchUsers = async () => {
    try {
      setUsers(await usersApi.getAllStrict());
      setError(null);
    } catch (e) {
      setError(e instanceof Error ? e.message : "Could not load users.");
    } finally {
      setLoading(false);
    }
  };

  const load = async () => {
    setLoading(true);
    await fetchUsers();
  };

  useEffect(() => {
    let cancelled = false;

    usersApi
        .getAllStrict()
        .then((data) => {
          if (cancelled) return;
          setUsers(data);
          setError(null);
        })
        .catch((error: unknown) => {
          if (cancelled) return;
          setError(
              error instanceof Error ? error.message : "Could not load users."
          );
        })
        .finally(() => {
          if (!cancelled) setLoading(false);
        });

    return () => {
      cancelled = true;
    };
  }, []);

  const primaryRole = (user: User): RoleType => {
    const roles = user.userRoles?.map((r) => r.roleId) ?? [];
    if (roles.includes("ADMIN")) return "ADMIN";
    if (roles.includes("TRAINER")) return "TRAINER";
    return "MEMBER";
  };

  const isMe = (user: User) => String(me?.userId) === user.userId;

  const visible = useMemo(() => {
    const q = query.trim().toLowerCase();
    return users
        .filter((user) => roleFilter === "ALL" || primaryRole(user) === roleFilter)
        .filter((user) => {
          if (!q) return true;
          return (
              `${user.firstName} ${user.lastName}`.toLowerCase().includes(q) ||
              (user.account?.email ?? "").toLowerCase().includes(q)
          );
        })
        .sort((a, b) =>
            `${a.lastName} ${a.firstName}`.localeCompare(`${b.lastName} ${b.firstName}`)
        );
  }, [users, query, roleFilter]);

  const confirmRoleChange = async () => {
    if (!roleChange) return;
    const { user: target, role } = roleChange;
    setBusy(target.userId);
    try {
      await adminApi.setRole(target.userId, role);
      toast(`${target.firstName} ${target.lastName} is now ${role}.`, "success");
      setRoleChange(null);
      await load();
    } catch (e) {
      toast(e instanceof Error ? e.message : "Could not change role.", "error");
      setRoleChange(null);
    } finally {
      setBusy(null);
    }
  };

  const confirmDelete = async () => {
    if (!deleting) return;
    const target = deleting;

    setBusy(target.userId);
    try {
      await usersApi.delete(target.userId);
      toast(`Deleted ${target.firstName} ${target.lastName}.`, "success");
      setDeleting(null);
      await load();
    } catch (e) {
      toast(e instanceof Error ? e.message : "Could not delete user.", "error");
      setDeleting(null);
    } finally {
      setBusy(null);
    }
  };

  const createStaff = async (e: FormEvent) => {
    e.preventDefault();
    setCreating(true);
    try {
      await adminApi.createStaff(
          {
            firstName: firstName.trim(),
            lastName: lastName.trim(),
            email: email.trim(),
            password,
            dateOfBirth: dob,
          },
          staffRole
      );
      toast(`${staffRole} account created.`, "success");
      setShowStaff(false);
      setFirstName("");
      setLastName("");
      setEmail("");
      setPassword("");
      setDob("");
      await load();
    } catch (err) {
      toast(err instanceof Error ? err.message : "Staff create failed.", "error");
    } finally {
      setCreating(false);
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
            <h1 className="page-title">Users</h1>
            <p className="page-sub">
              See everyone on the platform, change their role, remove accounts, or
              create staff accounts.
            </p>
          </div>
          <button
              type="button"
              className="btn"
              onClick={() => setShowStaff((value) => !value)}
          >
            {showStaff ? "Close" : "Create staff"}
          </button>
        </div>

        {showStaff && (
            <form
                className="card"
                onSubmit={createStaff}
                style={{ marginBottom: "1.25rem", maxWidth: 520 }}
            >
              <h2 style={{ fontSize: "1rem", marginBottom: "0.75rem" }}>
                New trainer / admin
              </h2>
              <div className="grid-2">
                <input
                    placeholder="First name"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                    required
                    style={inp}
                />
                <input
                    placeholder="Last name"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                    required
                    style={inp}
                />
                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                    style={inp}
                />
                <input
                    type="date"
                    value={dob}
                    onChange={(e) => setDob(e.target.value)}
                    required
                    style={inp}
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                    style={inp}
                />
                <select
                    value={staffRole}
                    onChange={(e) =>
                        setStaffRole(e.target.value as "TRAINER" | "ADMIN")
                    }
                    style={inp}
                >
                  <option value="TRAINER">TRAINER</option>
                  <option value="ADMIN">ADMIN</option>
                </select>
              </div>
              <button
                  type="submit"
                  className="btn"
                  style={{ marginTop: "0.85rem" }}
                  disabled={creating}
              >
                {creating ? "Creating…" : "Create account"}
              </button>
            </form>
        )}

        <div
            className="card"
            style={{
              marginBottom: "1rem",
              display: "flex",
              gap: "0.75rem",
              flexWrap: "wrap",
            }}
        >
          <input
              aria-label="Search users"
              placeholder="Search by name or email"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              style={{ ...inp, flex: 1, minWidth: 220, width: "auto" }}
          />
          <select
              aria-label="Filter by role"
              value={roleFilter}
              onChange={(e) =>
                  setRoleFilter(e.target.value as RoleType | "ALL")
              }
              style={{ ...inp, width: "auto" }}
          >
            <option value="ALL">All roles</option>
            {ROLES.map((role) => (
                <option key={role} value={role}>
                  {role}
                </option>
            ))}
          </select>
        </div>

        {loading ? (
            <div className="skeleton" style={{ height: 120 }} />
        ) : error ? (
            <div className="card empty">
              <p style={{ marginBottom: "0.75rem" }}>{error}</p>
              <button
                  type="button"
                  className="btn btn-outline"
                  onClick={() => void load()}
              >
                Try again
              </button>
            </div>
        ) : visible.length === 0 ? (
            <div className="card empty">No users match.</div>
        ) : (
            <div className="card" style={{ padding: 0, overflow: "auto" }}>
              <table style={{ width: "100%", borderCollapse: "collapse", fontSize: "0.9rem" }}>
                <thead>
                <tr
                    style={{
                      textAlign: "left",
                      borderBottom: "1px solid var(--fn-border)",
                    }}
                >
                  <th style={cell}>Name</th>
                  <th style={cell}>Email</th>
                  <th style={cell}>Role</th>
                  <th style={cell}>Actions</th>
                </tr>
                </thead>
                <tbody>
                {visible.map((user) => {
                  const role = primaryRole(user);
                  const self = isMe(user);

                  return (
                      <tr
                          key={user.userId}
                          style={{ borderBottom: "1px solid var(--fn-border)" }}
                      >
                        <td style={cell}>
                          {user.firstName} {user.lastName}
                          {self && (
                              <span style={{ color: "var(--fn-muted)" }}> (you)</span>
                          )}
                        </td>
                        <td style={cell}>{user.account?.email ?? "—"}</td>
                        <td style={cell}>
                          <select
                              aria-label={`Role for ${user.firstName} ${user.lastName}`}
                              value={role}
                              disabled={busy === user.userId || self}
                              title={
                                self
                                    ? "You can't change your own role"
                                    : "Change role"
                              }
                              onChange={(e) => {
                                const next = e.target.value as RoleType;
                                if (next !== role) setRoleChange({ user, role: next });
                              }}
                              style={{ ...inp, width: "auto", padding: "0.35rem 0.6rem" }}
                          >
                            {ROLES.map((r) => (
                                <option key={r} value={r}>
                                  {r}
                                </option>
                            ))}
                          </select>
                        </td>
                        <td style={cell}>
                          <button
                              type="button"
                              className="btn btn-danger"
                              style={{ padding: "0.35rem 0.8rem", fontSize: "0.8rem" }}
                              disabled={busy === user.userId || self}
                              title={
                                self
                                    ? "You can't delete your own account here"
                                    : "Delete user"
                              }
                              onClick={() => setDeleting(user)}
                          >
                            Delete
                          </button>
                        </td>
                      </tr>
                  );
                })}
                </tbody>
              </table>
            </div>
        )}

        {roleChange && (
            <ConfirmDialog
                title="Change role"
                message={`Make ${roleChange.user.firstName} ${roleChange.user.lastName} a ${roleChange.role}? ${
                    roleChange.role === "ADMIN"
                        ? "Admins can manage users, lookups and resources, and cannot book sessions. "
                        : ""
                }Their previous role is replaced.`}
                confirmLabel="Change role"
                onConfirm={() => void confirmRoleChange()}
                onCancel={() => setRoleChange(null)}
            />
        )}

        {deleting && (
            <ConfirmDialog
                title="Delete user"
                message={`Delete ${deleting.firstName} ${deleting.lastName}? Their roles, bookings and account are removed. This cannot be undone.`}
                confirmLabel="Delete"
                onConfirm={() => void confirmDelete()}
                onCancel={() => setDeleting(null)}
            />
        )}
      </div>
  );
}

const cell: CSSProperties = { padding: "0.85rem 1rem" };

const inp: CSSProperties = {
  background: "var(--fn-bg)",
  border: "1px solid var(--fn-border)",
  borderRadius: 10,
  padding: "0.6rem 0.75rem",
  width: "100%",
};