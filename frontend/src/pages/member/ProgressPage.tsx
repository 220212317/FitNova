import { useEffect, useState } from "react";
import { bookingsApi } from "../../api/fitnova";
import { useAuth } from "../../auth/AuthContext";
import type { Booking } from "../../types";
import { Activity, CheckCircle2, Clock, CalendarDays } from "lucide-react";
import { formatDate, formatTime } from "../../lib/format";

export function ProgressPage() {
    const { user } = useAuth();
    const [bookings, setBookings] = useState<Booking[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        let cancelled = false;
        async function load() {
            if (!user?.userId) return;
            try {
                // Fetch user's bookings
                const all = await bookingsApi.byMember(user.userId).catch(() => []);
                if (!cancelled) setBookings(all);
            } finally {
                if (!cancelled) setLoading(false);
            }
        }
        void load();
        return () => { cancelled = true; };
    }, [user?.userId]);

    // Calculate stats based on booking history
    const completed = bookings.filter((b) => b.status === "COMPLETED");
    const upcoming = bookings.filter((b) => b.status === "CONFIRMED");
    const cancelled = bookings.filter((b) => b.status === "CANCELLED");

    // Calculate total hours (assuming a standard 1-hour slot if exact end time isn't available)
    const totalMinutes = completed.reduce((acc, b) => {
        if (!b.slot?.startTime || !b.slot?.endTime) return acc + 60;
        const start = b.slot.startTime.split(":").map(Number);
        const end = b.slot.endTime.split(":").map(Number);
        const diff = (end[0] * 60 + end[1]) - (start[0] * 60 + start[1]);
        return acc + (diff > 0 ? diff : 60);
    }, 0);

    const totalHours = (totalMinutes / 60).toFixed(1);

    if (loading) {
        return <div className="skeleton" style={{ height: 200 }} />;
    }

    return (
        <div className="dash">
            <header className="dash-head">
                <div>
                    <h1 className="page-title">My Progress</h1>
                    <p className="page-sub">Track your consistency and completed training sessions.</p>
                </div>
            </header>

            {/* STAT CARDS */}
            <div className="stat-row">
                <div className="stat-card">
                    <span>Completed Sessions</span>
                    <strong>{completed.length}</strong>
                    <small>Total workouts done</small>
                </div>
                <div className="stat-card">
                    <span>Hours Trained</span>
                    <strong>{totalHours}</strong>
                    <small>Time invested in yourself</small>
                </div>
                <div className="stat-card">
                    <span>Upcoming Sessions</span>
                    <strong>{upcoming.length}</strong>
                    <small>Keep the momentum going</small>
                </div>
            </div>

            <div className="dash-panels">
                {/* RECENT ACTIVITY */}
                <section className="card">
                    <h2>Recent Activity</h2>
                    {completed.length === 0 ? (
                        <p className="empty">No completed sessions yet. Book a slot to get started!</p>
                    ) : (
                        <ul className="list">
                            {completed.slice(0, 5).map((b) => (
                                <li key={b.bookingId}>
                                    <div>
                                        <strong>{formatDate(b.slot?.date)}</strong>
                                        <span>{formatTime(b.slot?.startTime)} – {formatTime(b.slot?.endTime)}</span>
                                    </div>
                                    <span className="badge badge-confirmed">
                    <CheckCircle2 size={12} style={{ marginRight: 4 }} />
                    Completed
                  </span>
                                </li>
                            ))}
                        </ul>
                    )}
                </section>

                {/* BOOKING HISTORY */}
                <section className="card">
                    <h2>Booking History</h2>
                    {bookings.length === 0 ? (
                        <p className="empty">You haven't booked any sessions yet.</p>
                    ) : (
                        <ul className="list">
                            {bookings.slice(0, 5).map((b) => (
                                <li key={b.bookingId}>
                                    <div>
                                        <strong>{formatDate(b.slot?.date)}</strong>
                                        <span>
                      {b.slot?.trainer
                          ? `${b.slot.trainer.firstName} ${b.slot.trainer.lastName}`
                          : "Trainer"}
                    </span>
                                    </div>
                                    <span
                                        className={`badge badge-${b.status.toLowerCase()}`}
                                    >
                    {b.status}
                  </span>
                                </li>
                            ))}
                        </ul>
                    )}
                </section>
            </div>
        </div>
    );
}

export default ProgressPage;
