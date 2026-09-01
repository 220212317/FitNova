/** TODO — Collins Shibambo (230093183) */
import { useEffect, useState } from "react";
import { getAllUsers, deleteUser } from "./userApi";
import type { User } from "../../types";
import { ApiError } from "../../api/client";

export default function UserList() {
    const [users, setUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    async function handleDelete(userId: string, name: string) {
        const confirmed = window.confirm(`Delete ${name}? This cannot be undone.`);
        if (!confirmed) {
            return;
        }

        try {
            await deleteUser(userId);
            setUsers((prev) => prev.filter((u) => u.userId !== userId));
        } catch (err) {
            setError(err instanceof ApiError ? err.message : "Failed to delete user.");
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
                    setError(err instanceof ApiError ? err.message : "Failed to load users.");
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

    if (loading) {
        return <p>Loading users...</p>;
    }

    return (
        <div>
            <h2>Users</h2>

            {error && <p style={{ color: "red" }}>{error}</p>}

            {users.length === 0 ? (
                <p>No users found.</p>
            ) : (
                <table>
                    <thead>
                    <tr>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Date of Birth</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    {users.map((user) => (
                        <tr key={user.userId}>
                            <td>{user.firstName}</td>
                            <td>{user.lastName}</td>
                            <td>{user.dateOfBirth}</td>
                            <td>
                                <button
                                    onClick={() =>
                                        handleDelete(user.userId, `${user.firstName} ${user.lastName}`)
                                    }
                                >
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}
