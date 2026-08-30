/** TODO — Avuyile Sitoyi (240971051) */

import { useCallback, useEffect, useState } from 'react';
import {
    getAllBookings,
    createBooking,
    updateBooking,
    cancelBooking,
    deleteBooking,
    type BookingCreateInput,
} from '../features/bookings/bookingsApi';
import { BookingList } from '../features/bookings/BookingList';
import { BookingForm } from '../features/bookings/BookingForm';
import type { Booking } from '../types';
import { ApiError } from '../api/client';
import '../features/bookings/bookings.css';

type ViewState = 'list' | 'create' | 'edit';

export default function BookingsPage() {
    const [bookings, setBookings] = useState<Booking[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [view, setView] = useState<ViewState>('list');
    const [editingBooking, setEditingBooking] = useState<Booking | null>(null);

    const loadBookings = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            const data = await getAllBookings();
            setBookings(data);
        } catch (err) {
            setError(messageFor(err));
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        void loadBookings();
    }, [loadBookings]);

    function messageFor(err: unknown): string {
        if (err instanceof ApiError) return err.message;
        if (err instanceof Error) return err.message;
        return 'Something went wrong. Try again.';
    }

    async function handleCreateOrUpdate(input: BookingCreateInput & { bookingId?: string }) {
        if (input.bookingId) {
            await updateBooking(input as BookingCreateInput & { bookingId: string });
        } else {
            await createBooking(input);
        }
        setView('list');
        setEditingBooking(null);
        await loadBookings();
    }

    function handleEdit(booking: Booking) {
        setEditingBooking(booking);
        setView('edit');
    }

    async function handleCancel(booking: Booking) {
        setError(null);
        try {
            await cancelBooking(booking);
            await loadBookings();
        } catch (err) {
            setError(messageFor(err));
        }
    }

    async function handleDelete(booking: Booking) {
        const confirmed = window.confirm(
            `Delete this booking (${booking.bookingId})? This can't be undone.`
        );
        if (!confirmed) return;

        setError(null);
        try {
            await deleteBooking(booking.bookingId);
            await loadBookings();
        } catch (err) {
            setError(messageFor(err));
        }
    }

    return (
        <>
            <div className="bk-header">
                <h1>Bookings</h1>
                {view === 'list' && (
                    <button
                        type="button"
                        className="bk-button bk-button--primary"
                        onClick={() => {
                            setEditingBooking(null);
                            setView('create');
                        }}
                    >
                        Book a session
                    </button>
                )}
            </div>

            {error && <div className="bk-state bk-state--error">{error}</div>}

            {view !== 'list' && (
                <BookingForm
                    initial={editingBooking ?? undefined}
                    onSubmit={handleCreateOrUpdate}
                    onCancel={() => {
                        setView('list');
                        setEditingBooking(null);
                    }}
                />
            )}

            {view === 'list' &&
                (loading ? (
                    <div className="bk-state">Loading bookings…</div>
                ) : (
                    <BookingList
                        bookings={bookings}
                        onEdit={handleEdit}
                        onCancel={handleCancel}
                        onDelete={handleDelete}
                    />
                ))}
        </>
    );
}
