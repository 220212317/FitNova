import { useEffect, useState } from 'react';
import type { Gender, Race } from '../../types';
import { getAllGenders, getAllRaces } from './lookupsApi';
import { ApiError } from '../../api/client';

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

    function loadOptions() {
        Promise.all([getAllGenders(), getAllRaces()])
            .then(([genderData, raceData]) => {
                setGenders(genderData);
                setRaces(raceData);
            })
            .catch((err: unknown) => {
                setError(err instanceof ApiError ? err.message : 'Failed to load gender/race options.');
            })
            .finally(() => {
                setLoading(false);
            });
    }

    useEffect(() => {
        loadOptions();
    }, []);

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
