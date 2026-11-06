/** schedule.test.ts
 *  Author: Inga Plati (230126634)
 */
import { describe, expect, it } from "vitest";
import {
    byBookingStart,
    bySlotStart,
    isUpcoming,
    isUpcomingBooking,
} from "./schedule";
import type { AvailabilitySlot, Booking, BookingStatus } from "../../types";

const NOW = new Date(2026, 10, 6, 10, 30);

const slot = (
    slotId: string,
    date: string,
    startTime: string
): AvailabilitySlot => ({
    slotId,
    date,
    startTime,
    endTime: "23:59:00",
    status: "AVAILABLE",
});

const booking = (
    bookingId: string,
    status: BookingStatus,
    bookedSlot?: AvailabilitySlot
): Booking => ({
    bookingId,
    bookingDateTime: "2026-11-01T08:00:00",
    status,
    slot: bookedSlot,
});

describe("isUpcoming", () => {
    it("is true for a later date", () => {
        expect(isUpcoming(slot("s1", "2026-11-07", "06:00:00"), NOW)).toBe(true);
    });

    it("is false for an earlier date", () => {
        expect(isUpcoming(slot("s2", "2026-11-05", "23:00:00"), NOW)).toBe(false);
    });

    it("is true later on the same day", () => {
        expect(isUpcoming(slot("s3", "2026-11-06", "11:00:00"), NOW)).toBe(true);
    });

    it("is true when the slot starts right now", () => {
        expect(isUpcoming(slot("s4", "2026-11-06", "10:30:00"), NOW)).toBe(true);
    });

    it("is false earlier on the same day", () => {
        expect(isUpcoming(slot("s5", "2026-11-06", "09:00:00"), NOW)).toBe(false);
    });

    it("is false when there is no slot or no date", () => {
        expect(isUpcoming(undefined, NOW)).toBe(false);
        expect(isUpcoming(slot("s6", "", "10:00:00"), NOW)).toBe(false);
    });
});

describe("isUpcomingBooking", () => {
    const future = slot("future", "2026-11-07", "09:00:00");
    const past = slot("past", "2026-11-01", "09:00:00");

    it("is true for a confirmed booking in the future", () => {
        expect(isUpcomingBooking(booking("b1", "CONFIRMED", future), NOW)).toBe(true);
    });

    it("is false for a cancelled booking in the future", () => {
        expect(isUpcomingBooking(booking("b2", "CANCELLED", future), NOW)).toBe(false);
    });

    it("is false for a confirmed booking in the past", () => {
        expect(isUpcomingBooking(booking("b3", "CONFIRMED", past), NOW)).toBe(false);
    });

    it("is false for a booking with no slot", () => {
        expect(isUpcomingBooking(booking("b4", "CONFIRMED"), NOW)).toBe(false);
    });
});

describe("sorting", () => {
    it("bySlotStart puts the earliest slot first", () => {
        const slots = [
            slot("c", "2026-11-08", "09:00:00"),
            slot("a", "2026-11-07", "08:00:00"),
            slot("b", "2026-11-07", "15:00:00"),
        ];

        expect(slots.sort(bySlotStart).map((item) => item.slotId)).toEqual([
            "a",
            "b",
            "c",
        ]);
    });

    it("byBookingStart orders bookings by their slot", () => {
        const bookings = [
            booking("late", "CONFIRMED", slot("x", "2026-11-09", "09:00:00")),
            booking("early", "CONFIRMED", slot("y", "2026-11-07", "09:00:00")),
        ];

        expect(bookings.sort(byBookingStart).map((item) => item.bookingId)).toEqual([
            "early",
            "late",
        ]);
    });
});