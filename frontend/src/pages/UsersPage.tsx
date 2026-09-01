/** Collins Shibambo (230093183) */
import { useState, useEffect } from "react";
import { getAllUsers, deleteUser } from "../features/user/userApi";
import UserList from "../features/user/UserList";
import UserForm from "../features/user/UserForm";
import UserRoleFields from "../features/user/UserRoleFields";
import type { User } from "../types";
import { ApiError } from "../api/client";

type ViewState = "list" | "create";

export default function UsersPage() {
    const [users, setUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [view, setView] = useState<ViewState>("list");
    const [assigningRoleFor, setAssigningRoleFor] = useState<User | null>(null);

    function messageFor(err: unknown): string {
        if (err instanceof ApiError) return err.message;
        if (err instanceof Error) return err.message;
        return "Something went wrong. Try again.";
    }

    async function loadUsers() {
        setLoading(true);
        setError(null);
        try {
            const data = await getAllUsers();
            setUsers(data);
        } catch (err) {
            setError(messageFor(err));
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        let cancelled = false;

        getAllUsers()
            .then((data) => {
                if (!cancelled) {
                    setUsers(data);
                }
            })
            .catch((err) => {
                if (!cancelled) {
                    setError(messageFor(err));
                }
            })
            .finally(() => {
                if (!cancelled) {
                    setLoading(false);
                }
            });

        return () => {
            cancelled = true;
        };
    }, []);

    async function handleDelete(user: User) {
        const confirmed = window.confirm(
            `Delete ${user.firstName} ${user.lastName}? This cannot be undone.`
        );
        if (!confirmed) {
            return;
        }

        setError(null);
        try {
            await deleteUser(user.userId);
            setUsers((prev) => prev.filter((u) => u.userId !== user.userId));
        } catch (err) {
            setError(messageFor(err));
        }
    }

    function handleCreated() {
        setView("list");
        loadUsers();
    }

    function handleAssignRole(user: User) {
        setAssigningRoleFor(user);
    }

    return (
        <div>
            <div>
                <h1>Users</h1>
                {view === "list" && (
                    <button type="button" onClick={() => setView("create")}>
                        Add User
                    </button>
                )}
            </div>

            {error && <p style={{ color: "red" }}>{error}</p>}

            {view === "create" && (
                <UserForm onCreated={handleCreated} onCancel={() => setView("list")} />
            )}

            {view === "list" &&
                (loading ? (
                    <p>Loading users...</p>
                ) : (
                    <UserList
                        users={users}
                        onDelete={handleDelete}
                        onAssignRole={handleAssignRole}
                    />
                ))}

            {assigningRoleFor && (
                <div>
                    <p>
                        Assigning role to {assigningRoleFor.firstName} {assigningRoleFor.lastName}
                    </p>
                    <UserRoleFields
                        userId={assigningRoleFor.userId}
                        onCreated={() => setAssigningRoleFor(null)}
                    />
                    <button type="button" onClick={() => setAssigningRoleFor(null)}>
                        Close
                    </button>
                </div>
            )}
        </div>
    );
}