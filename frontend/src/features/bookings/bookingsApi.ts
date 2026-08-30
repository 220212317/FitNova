/** TODO — Avuyile Sitoyi (240971051) */
/**
 * bookingsApi.ts
 *
 * CRUD + finder calls for Booking, matching za.ac.cput.controller.BookingController
 * exactly (routes, method names, and JSON shape confirmed against that class
 * and against Booking.java / BookingStatus.java).
 *
 * Backend routes:
 *   GET    /booking/getAll
 *   GET    /booking/read/{bookingId}
 *   POST   /booking/create
 *   PUT    /booking/update
 *   DELETE /booking/delete/{bookingId}
 *   GET    /booking/findByMember/{userId}
 *   GET    /booking/findBySlot/{slotId}
 *   GET    /booking/findByStatus/{status}
 *
 * Payload shape for create/update: the backend's Booking.member and
 * Booking.slot are @ManyToOne(optional = false), but only need the id to
 * resolve the relationship — so the payload only sends
 * `member: { userId }` and `slot: { slotId } }`, not full nested objects
 * (see issue #91: "Payload: member: { userId }, slot: { slotId } only").
 *
 * bookingDateTime must be "yyyy-MM-dd'T'HH:mm:ss" — no trailing Z, no
 * milliseconds — to match the backend's LocalDateTime deserialization.
 */

import { api } from '../../api/client';
import { ensureId } from '../../api/ids';
import type { Booking, BookingStatus } from '../../types';

const BASE = '/booking';

export interface BookingCreateInput {
    bookingId?: string;
    bookingDateTime: string;
    status?: BookingStatus;
    memberId: string;
    slotId: string;
}

function toPayload(input: BookingCreateInput) {
    return {
        bookingId: ensureId(input.bookingId),
        bookingDateTime: input.bookingDateTime,
        status: input.status ?? 'CONFIRMED',
        member: { userId: input.memberId },
        slot: { slotId: input.slotId },
    };
}

export function getAllBookings(): Promise<Booking[]> {
    return api.get<Booking[]>(`${BASE}/getAll`);
}

export function getBooking(bookingId: string): Promise<Booking> {
    return api.get<Booking>(`${BASE}/read/${encodeURIComponent(bookingId)}`);
}

export function createBooking(input: BookingCreateInput): Promise<Booking> {
    return api.post<Booking>(`${BASE}/create`, toPayload(input));
}

export function updateBooking(input: BookingCreateInput & { bookingId: string }): Promise<Booking> {
    return api.put<Booking>(`${BASE}/update`, toPayload(input));
}

/** Cancel = update with status set to CANCELLED, per issue #91. */
export function cancelBooking(booking: Booking): Promise<Booking> {
    if (!booking.member || !booking.slot) {
        throw new Error('Cannot cancel a booking with no member/slot loaded.');
    }
    return updateBooking({
        bookingId: booking.bookingId,
        bookingDateTime: booking.bookingDateTime,
        status: 'CANCELLED',
        memberId: booking.member.userId,
        slotId: booking.slot.slotId,
    });
}

export function deleteBooking(bookingId: string): Promise<void> {
    return api.delete(`${BASE}/delete/${encodeURIComponent(bookingId)}`);
}

export function findBookingsByMember(userId: string): Promise<Booking[]> {
    return api.get<Booking[]>(`${BASE}/findByMember/${encodeURIComponent(userId)}`);
}

export function findBookingsBySlot(slotId: string): Promise<Booking[]> {
    return api.get<Booking[]>(`${BASE}/findBySlot/${encodeURIComponent(slotId)}`);
}

export function findBookingsByStatus(status: BookingStatus): Promise<Booking[]> {
    return api.get<Booking[]>(`${BASE}/findByStatus/${status}`);
}

// Grouped export, consistent with lookupsApi's convention, so fitnova.ts
// can do: export { bookingsApi } from '../features/bookings/bookingsApi';
export const bookingsApi = {
    getAllBookings,
    getBooking,
    createBooking,
    updateBooking,
    cancelBooking,
    deleteBooking,
    findBookingsByMember,
    findBookingsBySlot,
    findBookingsByStatus,
};