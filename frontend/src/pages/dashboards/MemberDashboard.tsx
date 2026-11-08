import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { CalendarPlus, CalendarCheck, BookOpen, BadgeCheck } from "lucide-react";
import { bookingsApi, slotsApi } from "../../api/fitnova";
import { useAuth } from "../../auth/AuthContext";
import type { AvailabilitySlot, Booking } from "../../types";
import { formatDate, formatTime, statusBadgeClass } from "../../lib/format";
import "./dashboards.css";

export default function MemberDashboard() {
  const { user } = useAuth();
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [slots, setSlots] = useState<AvailabilitySlot[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let cancelled = false;
    (async () => {
      try {
        const [allBookings, allSlots] = await Promise.all([
          bookingsApi.getAll().catch(() => [] as Booking[]),
          slotsApi.getAll().catch(() => [] as AvailabilitySlot[]),
        ]);
        if (cancelled) return;
        const mine = user?.userId
          ? allBookings.filter((b) => b.member?.userId === user.userId)
          : allBookings;
        setBookings(mine.slice(0, 5));
        setSlots(allSlots.filter((s) => s.status === "AVAILABLE").slice(0, 5));
      } finally {
        if (!cancelled) setLoading(false);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, [user?.userId]);

  const confirmed = bookings.filter((b) => b.status === "CONFIRMED").length;
  const cancelled = bookings.filter((b) => b.status === "CANCELLED").length;

  return (
    <div className="dash">
      <header className="dash-head">
        <div>
          <h1 className="page-title">Member dashboard</h1>
          <p className="page-sub">Book sessions, track requests, and open the resource library.</p>
        </div>
      </header>

      <div className="stat-row">
        <div className="stat-card">
          <span>Available slots</span>
          <strong>{loading ? "…" : slots.length}+</strong>
        </div>
        <div className="stat-card">
          <span>Confirmed</span>
          <strong>{loading ? "…" : confirmed}</strong>
        </div>
        <div className="stat-card">
          <span>Cancelled</span>
          <strong>{loading ? "…" : cancelled}</strong>
        </div>
      </div>

      <div className="quick-links">
        <Link to="/book" className="quick-card">
          <CalendarPlus size={20} />
          <div>
            <strong>Book a session</strong>
            <span>Browse available trainer slots</span>
          </div>
        </Link>
        <Link to="/my-bookings" className="quick-card">
          <CalendarCheck size={20} />
          <div>
            <strong>My bookings</strong>
            <span>Confirmed, cancelled, and history</span>
          </div>
        </Link>
        <Link to="/app/resources" className="quick-card">
          <BookOpen size={20} />
          <div>
            <strong>Resources</strong>
            <span>Manuals, nutrition, tips</span>
          </div>
        </Link>
        <Link to="/apply-trainer" className="quick-card">
          <BadgeCheck size={20} />
          <div>
            <strong>Become a trainer</strong>
            <span>Apply for admin review</span>
          </div>
        </Link>
      </div>

      <div className="dash-panels">
        <section className="card">
          <h2>Open slots</h2>
          {loading ? (
            <div className="skeleton" style={{ height: 80 }} />
          ) : slots.length === 0 ? (
            <p className="empty">No open slots right now. Check back soon.</p>
          ) : (
            <ul className="list">
              {slots.map((s) => (
                <li key={s.slotId}>
                  <div>
                    <strong>
                      {formatDate(s.date)} · {formatTime(s.startTime)}–{formatTime(s.endTime)}
                    </strong>
                    <span>
                      {s.trainer
                        ? `${s.trainer.firstName} ${s.trainer.lastName}`
                        : "Trainer TBA"}
                    </span>
                  </div>
                  <span className={statusBadgeClass(s.status)}>{s.status}</span>
                </li>
              ))}
            </ul>
          )}
          <Link to="/book" className="btn btn-outline" style={{ marginTop: "1rem" }}>
            View all
          </Link>
        </section>

        <section className="card">
          <h2>Recent bookings</h2>
          {loading ? (
            <div className="skeleton" style={{ height: 80 }} />
          ) : bookings.length === 0 ? (
            <p className="empty">You have not booked a session yet.</p>
          ) : (
            <ul className="list">
              {bookings.map((b) => (
                <li key={b.bookingId}>
                  <div>
                    <strong>
                      {formatDate(b.slot?.date)} · {formatTime(b.slot?.startTime)}
                    </strong>
                    <span>{b.slot?.trainer ? `${b.slot.trainer.firstName} ${b.slot.trainer.lastName}` : "—"}</span>
                  </div>
                  <span className={statusBadgeClass(b.status)}>{b.status}</span>
                </li>
              ))}
            </ul>
          )}
          <Link to="/my-bookings" className="btn btn-outline" style={{ marginTop: "1rem" }}>
            All bookings
          </Link>
        </section>
      </div>
    </div>
  );
}
