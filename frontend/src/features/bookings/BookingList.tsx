
/** TODO — Avuyile Sitoyi (240971051) */

import type { Booking, BookingStatus } from '../../types';
import '../bookings/bookings.css';

interface BookingListProps {
    bookings: Booking[];
    onEdit: (booking: Booking) => void;
    onCancel: (booking: Booking) => void;
    onDelete: (booking: Booking) => void;
}

const STATUS_LABEL: Record<BookingStatus, string> = {
    CONFIRMED: 'Confirmed',
    CANCELLED: 'Cancelled',
    COMPLETED: 'Completed',
};

function StatusTag({ status }: { status: BookingStatus }) {
    const className =
        status === 'CANCELLED'
            ? 'bk-tag bk-tag--cancelled'
            : status === 'COMPLETED'
                ? 'bk-tag bk-tag--completed'
                : 'bk-tag';
    return <span className={className}>{STATUS_LABEL[status]}</span>;
}

// member/slot are optional on Booking (they may not always be loaded/
// expanded by the backend), so render a fallback rather than crashing.
function memberName(booking: Booking): string {
    if (!booking.member) return 'Unknown member';
    return `${booking.member.firstName} ${booking.member.lastName}`;
}

function sessionTime(booking: Booking): string {
    if (!booking.slot) return 'Slot unavailable';
    const { date, startTime, endTime } = booking.slot;
    return `${date} · ${startTime.slice(0, 5)}–${endTime.slice(0, 5)}`;
}

export function BookingList({ bookings, onEdit, onCancel, onDelete }: BookingListProps) {
    if (bookings.length === 0) {
        return <div className="bk-state">No bookings yet. Book a session to see it here.</div>;
    }

    return (
        <div className="bk-list">
            {bookings.map((booking) => (
                <div className="bk-card" key={booking.bookingId}>
                    <div className="bk-card__main">
                        <span className="bk-card__member">{memberName(booking)}</span>
                        <span className="bk-card__meta">{sessionTime(booking)}</span>
                    </div>

                    <div className="bk-card__actions">
                        <StatusTag status={booking.status} />
                        <button type="button" className="bk-button" onClick={() => onEdit(booking)}>
                            Edit
                        </button>
                        {booking.status === 'CONFIRMED' && (
                            <button type="button" className="bk-button" onClick={() => onCancel(booking)}>
                                Cancel
                            </button>
                        )}
                        <button
                            type="button"
                            className="bk-button bk-button--danger"
                            onClick={() => onDelete(booking)}
                        >
                            Delete
                        </button>
                    </div>
                </div>
            ))}
        </div>
    );
}

