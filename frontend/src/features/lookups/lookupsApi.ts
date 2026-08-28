/** TODO — Inga Plati (230126634) */
/**
 * lookupsApi.ts
 *
 * CRUD calls for the three lookup entities behind the Lookups page:
 * Gender, Race, and Demographic (which just references a Gender + a Race).
 *
 * Backend routes confirmed directly from GenderController / RaceController /
 * DemographicController (za.ac.cput.controller):
 *   POST   /{resource}/create
 *   GET    /{resource}/read/{id}
 *   PUT    /{resource}/update
 *   DELETE /{resource}/delete/{id}
 *   GET    /{resource}/getAll
 *
 * Important: GenderServiceImpl / RaceServiceImpl / DemographicServiceImpl all
 * reject create() with a null id (-> 400 from the controller), and none of
 * them go through their Factory's id-generation fallback (the controllers
 * call the service directly, bypassing the factory). That means ensureId()
 * below is the ONLY thing preventing a blank-id create request from either
 * failing outright, or worse, succeeding with an empty-string primary key.
 * Never remove those ensureId() calls without confirming the backend has
 * been changed to guard against this itself.
 *
 * Note on Demographic specifically: gender/race are @ManyToOne with no
 * cascade, so the backend expects them to already exist — you must create
 * the Gender and Race rows first, then reference their real ids when
 * creating a Demographic. Passing a brand-new/unsaved Gender or Race object
 * will not implicitly create it.
 */

import { get, post, put, del } from '../../api/client';
import { ensureId } from '../../api/ids';
import type { Gender, Race, Demographic } from '../../types';

// ---------------------------------------------------------------------------
// Gender
// ---------------------------------------------------------------------------

export function getAllGenders(): Promise<Gender[]> {
    return get<Gender[]>('/gender/getAll');
}

export function getGender(genderId: string): Promise<Gender> {
    return get<Gender>(`/gender/read/${encodeURIComponent(genderId)}`);
}

/**
 * `genderId` is optional here on purpose — pass it if the user chose a
 * specific code, omit it to let ensureId() generate one.
 */
export function createGender(input: { genderId?: string; description: string }): Promise<Gender> {
    const payload: Gender = {
        genderId: ensureId(input.genderId),
        description: input.description,
    };
    return post<Gender>('/gender/create', payload);
}

export function updateGender(gender: Gender): Promise<Gender> {
    return put<Gender>('/gender/update', gender);
}

export function deleteGender(genderId: string): Promise<void> {
    return del<void>(`/gender/delete/${encodeURIComponent(genderId)}`);
}

// ---------------------------------------------------------------------------
// Race
// ---------------------------------------------------------------------------

export function getAllRaces(): Promise<Race[]> {
    return get<Race[]>('/race/getAll');
}

export function getRace(raceId: string): Promise<Race> {
    return get<Race>(`/race/read/${encodeURIComponent(raceId)}`);
}

export function createRace(input: { raceId?: string; description: string }): Promise<Race> {
    const payload: Race = {
        raceId: ensureId(input.raceId),
        description: input.description,
    };
    return post<Race>('/race/create', payload);
}

export function updateRace(race: Race): Promise<Race> {
    return put<Race>('/race/update', race);
}

export function deleteRace(raceId: string): Promise<void> {
    return del<void>(`/race/delete/${encodeURIComponent(raceId)}`);
}

// ---------------------------------------------------------------------------
// Demographic (a saved Gender + a saved Race)
// ---------------------------------------------------------------------------

export function getAllDemographics(): Promise<Demographic[]> {
    return get<Demographic[]>('/demographic/getAll');
}

export function getDemographic(demographyId: string): Promise<Demographic> {
    return get<Demographic>(`/demographic/read/${encodeURIComponent(demographyId)}`);
}

export function createDemographic(input: {
    demographyId?: string;
    gender: Gender;
    race: Race;
}): Promise<Demographic> {
    const payload: Demographic = {
        demographyId: ensureId(input.demographyId),
        gender: input.gender,
        race: input.race,
    };
    return post<Demographic>('/demographic/create', payload);
}

export function updateDemographic(demographic: Demographic): Promise<Demographic> {
    return put<Demographic>('/demographic/update', demographic);
}

export function deleteDemographic(demographyId: string): Promise<void> {
    return del<void>(`/demographic/delete/${encodeURIComponent(demographyId)}`);
}

// ---------------------------------------------------------------------------
// Convenience grouping — lets fitnova.ts do:
//   export { lookupsApi } from '../features/lookups/lookupsApi';
// Individual functions above remain directly importable within this
// feature (e.g. GenderForm.tsx can just `import { createGender } from './lookupsApi'`).
// ---------------------------------------------------------------------------

export const lookupsApi = {
    getAllGenders,
    getGender,
    createGender,
    updateGender,
    deleteGender,
    getAllRaces,
    getRace,
    createRace,
    updateRace,
    deleteRace,
    getAllDemographics,
    getDemographic,
    createDemographic,
    updateDemographic,
    deleteDemographic,
};

