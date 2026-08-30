/** TODO — Avuyile Sitoyi (240971051) */
/**
 * Per issue #91:
 *  - member + slot required; status defaults to CONFIRMED on create
 *  - bookingDateTime as yyyy-MM-dd'T'HH:mm:ss (no Z, no millis)
 *  - payload only needs member.userId and slot.slotId (handled in
 *    bookingsApi.ts's toPayload(), not here)
 */

import { useState, type FormEvent } from 'react';
import type { Booking } from '../../types';
import type { BookingCreateInput } from './bookingsApi';
import './bookings.css';

interface BookingFormProps {
    initial?: Booking;
    onSubmit: (input: BookingCreateInput & { bookingId?: string }) => Promise<void>;
    onCancel: () => void;
}

/**
 * <input type="datetime-local"> gives "yyyy-MM-ddTHH:mm" (no seconds, no
 * timezone). The backend's LocalDateTime expects
 * "yyyy-MM-dd'T'HH:mm:ss" — no trailing Z, no millis — so append ":00"
 * rather than Date#toISOString(), which would add both.
 */
function toBackendDateTime(datetimeLocalValue: string): string {
    return datetimeLocalValue.length === 16 ? `${datetimeLocalValue}:00` : datetimeLocalValue;
}

function toDatetimeLocalValue(backendValue: string): string {
    return backendValue.slice(0, 16);
}

export function BookingForm({ initial, onSubmit, onCancel }: BookingFormProps) {
    const isEdit = Boolean(initial);

    const [userId, setUserId] = useState(initial?.member?.userId ?? '');
    const [slotId, setSlotId] = useState(initial?.slot?.slotId ?? '');
    const [bookingDateTime, setBookingDateTime] = useState(
        initial ? toDatetimeLocalValue(initial.bookingDateTime) : ''
    );
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState<string | null>(null);

    async function handleSubmit(event: FormEvent) {
        event.preventDefault();
        setError(null);

        if (!userId.trim() || !slotId.trim() || !bookingDateTime) {
            setError('Member, slot, and date/time are all required.');
            return;
        }

        setSubmitting(true);
        try {
            await onSubmit({
                bookingId: initial?.bookingId,
                bookingDateTime: toBackendDateTime(bookingDateTime),
                status: initial?.status ?? 'CONFIRMED',
                memberId: userId.trim(),
                slotId: slotId.trim(),
            });
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Something went wrong. Try again.');
        } finally {
            setSubmitting(false);
        }
    }

    return (
        <form className="bk-form" onSubmit={handleSubmit}>
            <h2>{isEdit ? 'Edit session' : 'Book a session'}</h2>

            <div className="bk-field">
                <label htmlFor="userId">Member ID</label>
                <input
                    id="userId"
                    value={userId}
                    onChange={(e) => setUserId(e.target.value)}
                    placeholder="U-..."
                    disabled={submitting}
                />
            </div>

            <div className="bk-field">
                <label htmlFor="slotId">Slot ID</label>
                <input
                    id="slotId"
                    value={slotId}
                    onChange={(e) => setSlotId(e.target.value)}
                    placeholder="S-..."
                    disabled={submitting}
                />
            </div>

            <div className="bk-field">
                <label htmlFor="bookingDateTime">Date &amp; time</label>
                <input
                    id="bookingDateTime"
                    type="datetime-local"
                    value={bookingDateTime}
                    onChange={(e) => setBookingDateTime(e.target.value)}
                    disabled={submitting}
                />
            </div>

            {error && <div className="bk-state bk-state--error">{error}</div>}

            <div className="bk-form__actions">
                <button type="button" className="bk-button" onClick={onCancel} disabled={submitting}>
                    Cancel
                </button>
                <button type="submit" className="bk-button bk-button--primary" disabled={submitting}>
                    {submitting ? 'Saving…' : isEdit ? 'Save changes' : 'Book session'}
                </button>
            </div>
        </form>
    );
}

