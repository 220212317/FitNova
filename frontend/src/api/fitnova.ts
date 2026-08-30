/** TODO — Avuyile Sitoyi (240971051) */
/**
 * fitnova.ts
 *
 * Central export point for all FitNova API groups, per issue #83's task:
 * "fitnova.ts: export API groups (usersApi, accountsApi, slotsApi,
 * bookingsApi, lookupsApi, ...) — coordinate exports with other members".
 *
 * bookingsApi is this issue's own responsibility (#91/#92). The other
 * groups are each a teammate's own feature — re-exported here once they
 * exist. As of this file, lookupsApi is implemented; usersApi, accountsApi,
 * slotsApi, contactApi are still TODO stubs in their own files, so they're
 * commented out below until each one is actually built.
 */

export { bookingsApi } from '../features/bookings/bookingsApi';
export { lookupsApi } from '../features/lookups/lookupsApi';

// TODO: uncomment once each teammate exports a grouped object from their
// own api file (matching the `export const xApi = { ... }` convention
// already used in lookupsApi.ts and bookingsApi.ts).
// export { usersApi } from '../features/user/userApi';
// export { accountsApi } from '../features/account/accountApi';
// export { slotsApi } from '../features/slots/slotsApi';
// export { contactApi } from '../features/contact/contactApi';

export { api, ApiError } from './client';
export * from '../types';