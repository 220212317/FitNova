/** TODO — Inga Plati (230126634) */
/**
 * ids.ts
 *
 * Helpers for generating and guaranteeing client-side IDs for create payloads.
 *
 * Context (confirmed against the backend source, za.ac.cput.domain):
 * - Gender.genderId and Race.raceId are @Id String fields with NO @GeneratedValue.
 *   The database does not assign these — whatever string is sent in the POST
 *   body becomes the primary key.
 * - GenderController/RaceController deserialize the request body directly and
 *   pass it straight to the service layer. They do NOT go through
 *   GenderFactory/RaceFactory, so the factories' own "generate if empty"
 *   fallback never runs for real HTTP requests.
 * - GenderServiceImpl/RaceServiceImpl reject the request (→ 400) if the id is
 *   null, but do NOT guard against an empty string "" — that would slip
 *   through to repository.save() and insert a row with an empty primary key.
 *
 * Net effect: ensureId() here is the only real safety net for these lookup
 * entities. It must reject blank strings, not just null/undefined.
 */

/**
 * Generates a new client-side unique identifier (UUID v4 string).
 * Matches the format the backend would itself generate via
 * Helper.generateId() (UUID.randomUUID().toString()), so a
 * frontend-generated id looks identical to a backend-generated one.
 */
export function newId(): string {
    if (typeof crypto !== "undefined" && typeof crypto.randomUUID === "function") {
        return crypto.randomUUID();
    }
    // Fallback for environments without crypto.randomUUID (older browsers,
    // some test runners). Not cryptographically strong — fine here, since
    // this is just an id, not a security token.
    return `id-${Date.now()}-${Math.random().toString(16).slice(2)}`;
}

/**
 * Guarantees a non-empty id string for a create payload.
 * Returns the trimmed value if it's present and non-blank,
 * otherwise generates a new one.
 *
 * Deliberately treats "" (and whitespace-only strings) as invalid, not just
 * null/undefined — the backend's own null-check would let "" through into
 * an INSERT with an empty primary key.
 */
export function ensureId(value?: string | null): string {
    const trimmed = value?.trim();
    return trimmed ? trimmed : newId();
}