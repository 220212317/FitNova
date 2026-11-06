/** schedule.ts
 *  Author: Inga Plati (230126634)
 */
import { hhmm } from "./gymData";
import type { AvailabilitySlot, Booking } from "../../types";

const pad = (value: number): string => String(value).padStart(2, "0");

const compare = (a: string, b: string): number => (a < b ? -1 : a > b ? 1 : 0);

const startKey = (slot?: AvailabilitySlot): string =>
    slot?.date ? `${slot.date} ${hhmm(slot.startTime)}` : "";

/** True when the slot starts at or after `now`. A slot with no date is never upcoming. */
export function isUpcoming(
    slot?: AvailabilitySlot,
    now: Date = new Date()
): boolean {
    if (!slot?.date) return false;

    const today = `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())}`;
    if (slot.date > today) return true;
    if (slot.date < today) return false;

    return hhmm(slot.startTime) >= `${pad(now.getHours())}:${pad(now.getMinutes())}`;
}

export function isUpcomingBooking(
    booking: Booking,
    now: Date = new Date()
): boolean {
    return booking.status === "CONFIRMED" && isUpcoming(booking.slot, now);
}

export function bySlotStart(a: AvailabilitySlot, b: AvailabilitySlot): number {
    return compare(startKey(a), startKey(b));
}

export function byBookingStart(a: Booking, b: Booking): number {
    return compare(startKey(a.slot), startKey(b.slot));
}