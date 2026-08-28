/** TODO — Inga Plati (230126634) */
import { useEffect, useState, type FormEvent } from 'react';
import type { Gender } from '../../types';
import { getAllGenders, createGender, updateGender, deleteGender } from './lookupsApi';
import { ApiError } from '../../api/client';

/**
 * GenderForm
 *
 * Full CRUD for the Gender lookup: lists existing genders, and one form
 * that switches between "create new" and "edit selected" depending on
 * whether `editingId` is set.
 *
 * No separate GenderList component — issue #87 only lists GenderForm.tsx
 * for this entity, so list + form live together here.
 */
export default function GenderForm() {
    const [genders, setGenders] = useState<Gender[]>([]);
    const [description, setDescription] = useState('');
    const [editingId, setEditingId] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState<string | null>(null);

    // Runs once, right after the component first renders — this is where
    // the initial "get everything from the backend" call belongs.
    useEffect(() => {
        void loadGenders();
    }, []);

    async function loadGenders() {
        setLoading(true);
        setError(null);
        try {
            const data = await getAllGenders();
            setGenders(data);
        } catch (err) {
            setError(err instanceof ApiError ? err.message : 'Failed to load genders.');
        } finally {
            setLoading(false);
        }
    }

    async function handleSubmit(e: FormEvent<HTMLFormElement>) {
        e.preventDefault(); // stop the browser's default full-page-reload form submit
        const trimmed = description.trim();
        if (!trimmed) return;

        setSubmitting(true);
        setError(null);
        try {
            if (editingId) {
                await updateGender({ genderId: editingId, description: trimmed });
            } else {
                // genderId is intentionally omitted — createGender's ensureId()
                // fallback generates one, since the user isn't choosing an id here.
                await createGender({ description: trimmed });
            }
            resetForm();
            await loadGenders();
        } catch (err) {
            setError(err instanceof ApiError ? err.message : 'Failed to save gender.');
        } finally {
            setSubmitting(false);
        }
    }

    function startEdit(gender: Gender) {
        setEditingId(gender.genderId);
        setDescription(gender.description);
    }

    function resetForm() {
        setEditingId(null);
        setDescription('');
    }

    async function handleDelete(genderId: string) {
        if (!window.confirm('Delete this gender?')) return;
        setError(null);
        try {
            await deleteGender(genderId);
            // If the row being edited was just deleted, don't leave a stale
            // form pointed at an id that no longer exists.
            if (editingId === genderId) resetForm();
            await loadGenders();
        } catch (err) {
            setError(err instanceof ApiError ? err.message : 'Failed to delete gender.');
        }
    }

    return (
        <section>
            <h2>Gender</h2>

            <form onSubmit={handleSubmit}>
                <label htmlFor="gender-description">Description</label>
                <input
                    id="gender-description"
                    type="text"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    placeholder="e.g. Male"
                    disabled={submitting}
                />
                <button type="submit" disabled={submitting || !description.trim()}>
                    {editingId ? 'Save' : 'Add'}
                </button>
                {editingId && (
                    <button type="button" onClick={resetForm} disabled={submitting}>
                        Cancel
                    </button>
                )}
            </form>

            {error && <p role="alert">{error}</p>}

            {loading ? (
                <p>Loading genders…</p>
            ) : genders.length === 0 ? (
                <p>No genders yet.</p>
            ) : (
                <ul>
                    {genders.map((gender) => (
                        <li key={gender.genderId}>
                            <span>{gender.description}</span>
                            <button type="button" onClick={() => startEdit(gender)}>
                                Edit
                            </button>
                            <button type="button" onClick={() => handleDelete(gender.genderId)}>
                                Delete
                            </button>
                        </li>
                    ))}
                </ul>
            )}
        </section>
    );
}



