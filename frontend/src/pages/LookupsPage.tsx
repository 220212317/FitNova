/** TODO — Inga Plati (230126634) */
import GenderForm from '../features/lookups/GenderForm';
import RaceForm from '../features/lookups/RaceForm';

/**
 * LookupsPage
 *
 * Assembles the Gender and Race CRUD screens. Demographic isn't managed
 * here on its own — per issue #87, DemographicFields is consumed inside
 * the People wizard instead, since a Demographic is just "an existing
 * Gender + an existing Race", not something meaningfully created in
 * isolation.
 */
export default function LookupsPage() {
    return (
        <div>
            <h1>Lookups</h1>
            <GenderForm />
            <RaceForm />
        </div>
    );
}
