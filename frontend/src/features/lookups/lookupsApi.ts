
import { api } from '../../api/client';
import { ensureId } from '../../api/ids';
import type { Gender, Race, Demographic } from '../../types';

export function getAllGenders(): Promise<Gender[]> {
    return api.get<Gender[]>('/gender/getAll');
}

export function getGender(genderId: string): Promise<Gender> {
    return api.get<Gender>(`/gender/read/${encodeURIComponent(genderId)}`);
}

export function createGender(input: { genderId?: string; description: string }): Promise<Gender> {
    const payload: Gender = {
        genderId: ensureId(input.genderId),
        description: input.description,
    };
    return api.post<Gender>('/gender/create', payload);
}

export function updateGender(gender: Gender): Promise<Gender> {
    return api.put<Gender>('/gender/update', gender);
}

export function deleteGender(genderId: string): Promise<void> {
    return api.delete(`/gender/delete/${encodeURIComponent(genderId)}`);
}


export function getAllRaces(): Promise<Race[]> {
    return api.get<Race[]>('/race/getAll');
}

export function getRace(raceId: string): Promise<Race> {
    return api.get<Race>(`/race/read/${encodeURIComponent(raceId)}`);
}

export function createRace(input: { raceId?: string; description: string }): Promise<Race> {
    const payload: Race = {
        raceId: ensureId(input.raceId),
        description: input.description,
    };
    return api.post<Race>('/race/create', payload);
}

export function updateRace(race: Race): Promise<Race> {
    return api.put<Race>('/race/update', race);
}

export function deleteRace(raceId: string): Promise<void> {
    return api.delete(`/race/delete/${encodeURIComponent(raceId)}`);
}

export function getAllDemographics(): Promise<Demographic[]> {
    return api.get<Demographic[]>('/demographic/getAll');
}

export function getDemographic(demographyId: string): Promise<Demographic> {
    return api.get<Demographic>(`/demographic/read/${encodeURIComponent(demographyId)}`);
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
    return api.post<Demographic>('/demographic/create', payload);
}

export function updateDemographic(demographic: Demographic): Promise<Demographic> {
    return api.put<Demographic>('/demographic/update', demographic);
}

export function deleteDemographic(demographyId: string): Promise<void> {
    return api.delete(`/demographic/delete/${encodeURIComponent(demographyId)}`);
}

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