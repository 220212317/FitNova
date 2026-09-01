/** TODO — Collins Shibambo (230093183) */
import { useState } from "react";
import { createUserRole } from "./userApi";
import { ApiError } from "../../api/client";

type RoleType = "MEMBER" | "TRAINER" | "ADMIN";

interface UserRoleFieldsProps {
    userId: string;
    onCreated?: () => void;
}

export default function UserRoleFields({ userId, onCreated }: UserRoleFieldsProps) {
    const [roleId, setRoleId] = useState<RoleType>("MEMBER");
    const [description, setDescription] = useState("");
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState(false);

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();
        setSubmitting(true);
        setError(null);
        setSuccess(false);

        try {
            await createUserRole({
                userId,
                roleId,
                description: description.trim() || undefined,
            });
            setSuccess(true);
            setDescription("");
            onCreated?.();
        } catch (err) {
            setError(err instanceof ApiError ? err.message : "Failed to assign role.");
        } finally {
            setSubmitting(false);
        }
    }

    return (
        <form onSubmit={handleSubmit}>
            <h3>Assign Role</h3>

            {error && <p style={{ color: "red" }}>{error}</p>}
            {success && <p style={{ color: "green" }}>Role assigned.</p>}

            <label>
                Role
                <select
                    value={roleId}
                    onChange={(e) => setRoleId(e.target.value as RoleType)}
                >
                    <option value="MEMBER">Member</option>
                    <option value="TRAINER">Trainer</option>
                    <option value="ADMIN">Admin</option>
                </select>
            </label>

            <label>
                Description
                <input
                    type="text"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    placeholder="Optional"
                />
            </label>

            <button type="submit" disabled={submitting}>
                {submitting ? "Assigning..." : "Assign Role"}
            </button>
        </form>
    );
}
