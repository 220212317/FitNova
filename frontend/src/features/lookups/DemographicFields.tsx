/** TODO — Inga Plati (230126634) */
import { useEffect, useState } from 'react';
import type { Gender, Race } from '../../types';
import { getAllGenders, getAllRaces } from './lookupsApi';
import { ApiError } from '../../api/client';

/**
 * DemographicFields
 *
 * NOT a CRUD form like GenderForm/RaceForm — this is a reusable field
 * group meant to be embedded inside a larger form (the People wizard,
 * per issue #87's task list — that wizard itself belongs to whoever owns
 * the People/User feature, not this ticket).
 *
 * It's a *controlled* component: it doesn't own the selected gender/race,
 * the parent form does. This component only fetches the dropdown OPTIONS
 * (all existing Genders/Races) and reports selections upward via callback
 * props. The parent decides what to do with the selection (e.g. bundling
 * it into a Demographic + User create payload) and when to submit.
 *
 * Depends on Gender/Race rows already existing in the database — see the
 * no-cascade note in lookupsApi.ts. If GenderForm/RaceForm haven't been
 * used to create any yet, these dropdowns will be empty except for the
 * placeholder option.
 */

interface DemographicFieldsProps {
    genderId: string | null;
    raceId: string | null;
    onGenderIdChange: (genderId: string) => void;
    onRaceIdChange: (raceId: string) => void;
    disabled?: boolean;
}

export default function DemographicFields({
                                              genderId,
                                              raceId,
                                              onGenderIdChange,
                                              onRaceIdChange,
                                              disabled = false,
                                          }: DemographicFieldsProps) {
    const [genders, setGenders] = useState<Gender[]>([]);
    const [races, setRaces] = useState<Race[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        void loadOptions();
    }, []);

    async function loadOptions() {
        setLoading(true);
        setError(null);
        try {
            // Fire both requests together rather than one after the other —
            // they don't depend on each other, so there's no reason to wait
            // for genders before starting the races request.
            const [genderData, raceData] = await Promise.all([getAllGenders(), getAllRaces()]);
            setGenders(genderData);
            setRaces(raceData);
        } catch (err) {
            setError(err instanceof ApiError ? err.message : 'Failed to load gender/race options.');
        } finally {
            setLoading(false);
        }
    }

    if (loading) return <p>Loading gender/race options…</p>;
    if (error) return <p role="alert">{error}</p>;

    return (
        <fieldset disabled={disabled}>
            <legend>Demographic</legend>

            <label htmlFor="demographic-gender">Gender</label>
            <select
                id="demographic-gender"
                value={genderId ?? ''}
                onChange={(e) => onGenderIdChange(e.target.value)}
            >
                <option value="" disabled>
                    Select gender
                </option>
                {genders.map((gender) => (
                    <option key={gender.genderId} value={gender.genderId}>
                        {gender.description}
                    </option>
                ))}
            </select>

            <label htmlFor="demographic-race">Race</label>
            <select
                id="demographic-race"
                value={raceId ?? ''}
                onChange={(e) => onRaceIdChange(e.target.value)}
            >
                <option value="" disabled>
                    Select race
                </option>
                {races.map((race) => (
                    <option key={race.raceId} value={race.raceId}>
                        {race.description}
                    </option>
                ))}
            </select>
        </fieldset>
    );
}

