/** gymData.ts
 *  Author: Inga Plati (230126634)
 */
import { useCallback, useEffect, useState } from "react";
import { usersApi, slotsApi, bookingsApi } from "../../api/fitnova";
import type {
    AvailabilitySlot,
    Booking,
    BookingStatus,
    User,
} from "../../types";

export function pctOf(value: number, total: number): number {
    if (!total || total <= 0) return 0;
    return Math.round((value / total) * 100);
}

export function hhmm(time?: string): string {
    if (!time) return "";
    const parts = time.split(":");
    if (parts.length < 2) return time;
    return `${parts[0]}:${parts[1]}`;
}

export function minutes(start?: string, end?: string): number {
    if (!start || !end) return 0;
    const toMins = (t: string) => {
        const [h, m] = t.split(":");
        const hours = Number(h);
        const mins = Number(m);
        if (Number.isNaN(hours) || Number.isNaN(mins)) return NaN;
        return hours * 60 + mins;
    };
    const a = toMins(start);
    const b = toMins(end);
    if (Number.isNaN(a) || Number.isNaN(b)) return 0;
    return Math.max(0, b - a);
}

export function initialsOf(firstName?: string, lastName?: string): string {
    const first = firstName?.trim()?.charAt(0) ?? "";
    const last = lastName?.trim()?.charAt(0) ?? "";
    return `${first}${last}`.toUpperCase();
}

export function scopeBy<T>(
    items: T[],
    userId: string | undefined,
    person: (item: T) => User | undefined
): T[] {
    if (!userId) return [];
    return items.filter((item) => person(item)?.userId === userId);
}

export interface BookingCounts {
    total: number;
    confirmed: number;
    cancelled: number;
    completed: number;
}

export function derive(bookings: Booking[]): BookingCounts {
    const counts: BookingCounts = {
        total: bookings.length,
        confirmed: 0,
        cancelled: 0,
        completed: 0,
    };
    for (const booking of bookings) {
        switch (booking.status as BookingStatus) {
            case "CONFIRMED":
                counts.confirmed += 1;
                break;
            case "CANCELLED":
                counts.cancelled += 1;
                break;
            case "COMPLETED":
                counts.completed += 1;
                break;
        }
    }
    return counts;
}

export interface GymData {
    users: User[];
    slots: AvailabilitySlot[];
    bookings: Booking[];
}

export interface GymState extends GymData {
    loading: boolean;
    error: string | null;
}

const EMPTY: GymState = {
    users: [],
    slots: [],
    bookings: [],
    loading: true,
    error: null,
};

let state: GymState = EMPTY;
let inFlight: Promise<void> | null = null;
const listeners = new Set<() => void>();

function publish(next: GymState): void {
    state = next;
    listeners.forEach((listener) => listener());
}

async function fetchAll(): Promise<GymData> {
    const [users, slots, bookings] = await Promise.all([
        usersApi.getAll(),
        slotsApi.getAll(),
        bookingsApi.getAll(),
    ]);
    return {
        users: Array.isArray(users) ? users : [],
        slots: Array.isArray(slots) ? slots : [],
        bookings: Array.isArray(bookings) ? bookings : [],
    };
}

function load(force = false): Promise<void> {
    if (inFlight) return inFlight;
    if (!force && !state.loading && state.error === null) {
        return Promise.resolve();
    }

    publish({ ...state, loading: true, error: null });

    inFlight = fetchAll()
        .then((data) => {
            publish({ ...data, loading: false, error: null });
        })
        .catch((e: unknown) => {
            publish({
                ...state,
                loading: false,
                error:
                    e instanceof Error ? e.message : "Failed to load dashboard data",
            });
        })
        .finally(() => {
            inFlight = null;
        });

    return inFlight;
}

export function invalidateGymData(): void {
    state = EMPTY;
    inFlight = null;
}

export interface UseGymData extends GymState {
    refresh: () => Promise<void>;
}

export function useGymData(): UseGymData {
    const [snapshot, setSnapshot] = useState<GymState>(state);

    useEffect(() => {
        const listener = () => setSnapshot(state);
        listeners.add(listener);
        void load(false);
        return () => {
            listeners.delete(listener);
        };
    }, []);

    const refresh = useCallback(async () => {
        await load(true);
    }, []);

    return { ...snapshot, refresh };
}