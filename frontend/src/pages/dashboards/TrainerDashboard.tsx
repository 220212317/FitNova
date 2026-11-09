import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { CalendarPlus, ClipboardList } from "lucide-react";
import { bookingsApi, slotsApi } from "../../api/fitnova";
import { useAuth } from "../../auth/AuthContext";
import type { AvailabilitySlot, Booking } from "../../types";
import { formatDate, formatTime, statusBadgeClass } from "../../lib/format";
import "./dashboards.css";

export default function TrainerDashboard() {
  const { user } = useAuth();
  const [slots, setSlots] = useState<AvailabilitySlot[]>([]);
  const [pending, setPending] = useState<Booking[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let cancelled = false;
    (async () => {
      try {
        const [allSlots, allBookings] = await Promise.all([
          slotsApi.getAll().catch(() => [] as AvailabilitySlot[]),
          bookingsApi.getAll().catch(() => [] as Booking[]),
        ]);
        if (cancelled) return;
        const mineSlots = user?.userId
          ? allSlots.filter((s) => s.trainer?.userId === user.userId)
          : allSlots;
        const mineBookings = allBookings.filter((b) => {
          const isMine = !user?.userId || b.slot?.trainer?.userId === user.userId;
          return isMine && b.status === "CONFIRMED";
        });
        setSlots(mineSlots.slice(0, 6));
        setPending(mineBookings.slice(0, 6));
      } finally {
        if (!cancelled) setLoading(false);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, [user?.userId]);

  const available = slots.filter((s) => s.status === "AVAILABLE").length;
  const booked = slots.filter((s) => s.status === "BOOKED").length;

  return (
    <div className="dash">
      <header className="dash-head">
        <div>
          <h1 className="page-title">Trainer dashboard</h1>
          <p className="page-sub">Publish availability and respond to booking requests.</p>
        </div>
      </header>

      <div className="stat-row">
        <div className="stat-card">
          <span>Your slots</span>
          <strong>{loading ? "…" : slots.length}</strong>
        </div>
        <div className="stat-card">
          <span>Available</span>
          <strong>{loading ? "…" : available}</strong>
        </div>
        <div className="stat-card">
          <span>Booked</span>
          <strong>{loading ? "…" : booked}</strong>
        </div>
        <div className="stat-card">
          <span>Confirmed on your slots</span>
          <strong>{loading ? "…" : pending.length}</strong>
        </div>
      </div>

      <div className="quick-links">
        <Link to="/trainer/slots" className="quick-card">
          <CalendarPlus size={20} />
          <div>
            <strong>Manage slots</strong>
            <span>Create and edit availability</span>
          </div>
        </Link>
        <Link to="/trainer/bookings" className="quick-card">
          <ClipboardList size={20} />
          <div>
            <strong>Session bookings</strong>
            <span>View bookings on your slots</span>
          </div>
        </Link>
      </div>

      <div className="dash-panels">
        <section className="card">
          <h2>Your upcoming slots</h2>
          {loading ? (
            <div className="skeleton" style={{ height: 80 }} />
          ) : slots.length === 0 ? (
            <p className="empty">No slots yet. Create your first availability window.</p>
          ) : (
            <ul className="list">
              {slots.map((s) => (
                <li key={s.slotId}>
                  <div>
                    <strong>
                      {formatDate(s.date)} · {formatTime(s.startTime)}–{formatTime(s.endTime)}
                    </strong>
                    <span>{s.title || "Training session"}</span>
                  </div>
                  <span className={statusBadgeClass(s.status)}>{s.status}</span>
                </li>
              ))}
            </ul>
          )}
        </section>

        <section className="card">
          <h2>Confirmed on your slots</h2>
          {loading ? (
            <div className="skeleton" style={{ height: 80 }} />
          ) : pending.length === 0 ? (
            <p className="empty">No confirmed bookings on your slots.</p>
          ) : (
            <ul className="list">
              {pending.map((b) => (
                <li key={b.bookingId}>
                  <div>
                    <strong>
                      {b.member
                        ? `${b.member.firstName} ${b.member.lastName}`
                        : "Member"}
                    </strong>
                    <span>
                      {formatDate(b.slot?.date)} · {formatTime(b.slot?.startTime)}
                    </span>
                  </div>
                  <span className={statusBadgeClass(b.status)}>{b.status}</span>
                </li>
              ))}
            </ul>
          )}
          <Link to="/trainer/bookings" className="btn btn-outline" style={{ marginTop: "1rem" }}>
            View all
          </Link>
        </section>
      </div>
    </div>
  );
}
