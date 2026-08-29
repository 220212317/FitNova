/** TODO — Inga Plati (230126634) */
import { useEffect, useState, type FormEvent } from 'react';
import type { Race } from '../../types';
import { getAllRaces, createRace, updateRace, deleteRace } from './lookupsApi';
import { ApiError } from '../../api/client';

export default function RaceForm() {
    const [races, setRaces] = useState<Race[]>([]);
    const [description, setDescription] = useState('');
    const [editingId, setEditingId] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        void loadRaces();
    }, []);

    async function loadRaces() {
        setLoading(true);
        setError(null);
        try {
            const data = await getAllRaces();
            setRaces(data);
        } catch (err) {
            setError(err instanceof ApiError ? err.message : 'Failed to load races.');
        } finally {
            setLoading(false);
        }
    }

    async function handleSubmit(e: FormEvent<HTMLFormElement>) {
        e.preventDefault();
        const trimmed = description.trim();
        if (!trimmed) return;

        setSubmitting(true);
        setError(null);
        try {
            if (editingId) {
                await updateRace({ raceId: editingId, description: trimmed });
            } else {
                await createRace({ description: trimmed });
            }
            resetForm();
            await loadRaces();
        } catch (err) {
            setError(err instanceof ApiError ? err.message : 'Failed to save race.');
        } finally {
            setSubmitting(false);
        }
    }

    function startEdit(race: Race) {
        setEditingId(race.raceId);
        setDescription(race.description);
    }

    function resetForm() {
        setEditingId(null);
        setDescription('');
    }

    async function handleDelete(raceId: string) {
        if (!window.confirm('Delete this race?')) return;
        setError(null);
        try {
            await deleteRace(raceId);
            if (editingId === raceId) resetForm();
            await loadRaces();
        } catch (err) {
            setError(err instanceof ApiError ? err.message : 'Failed to delete race.');
        }
    }

    return (
        <section>
            <h2>Race</h2>

            <form onSubmit={handleSubmit}>
                <label htmlFor="race-description">Description</label>
                <input
                    id="race-description"
                    type="text"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    placeholder="e.g. Black African"
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
                <p>Loading races…</p>
            ) : races.length === 0 ? (
                <p>No races yet.</p>
            ) : (
                <ul>
                    {races.map((race) => (
                        <li key={race.raceId}>
                            <span>{race.description}</span>
                            <button type="button" onClick={() => startEdit(race)}>
                                Edit
                            </button>
                            <button type="button" onClick={() => handleDelete(race.raceId)}>
                                Delete
                            </button>
                        </li>
                    ))}
                </ul>
            )}
        </section>
    );
}

