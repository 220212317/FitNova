import type { AvailabilitySlot, Booking, User } from "../../types";

export interface GymData {
    people: User[];
    slots: AvailabilitySlot[];
    bookings: Booking[];
    loading: boolean;
}

export function useGymData(): GymData {
    // TODO: load users, slots and bookings from the API
    return { people: [], slots: [], bookings: [], loading: false };
}
