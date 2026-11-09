import { useEffect, useState } from "react";
import { bookingsApi } from "../../api/fitnova";
import { useAuth } from "../../auth/AuthContext";
import type { Booking } from "../../types";
import { formatDate, formatTime, statusBadgeClass } from "../../lib/format";

/**
 * Backend has no trainer accept/reject step — bookings are CONFIRMED on create.
 * Trainers (and admins) view bookings on their slots.
 */
export function TrainerBookingsPage() {
  const { user } = useAuth();
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let cancelled = false;
    (async () => {
      try {
        const all = await bookingsApi.getAll().catch(() => [] as Booking[]);
        if (cancelled) return;
        const filtered =
          user?.role === "ADMIN"
            ? all
            : all.filter((b) => b.slot?.trainer?.userId === user?.userId);
        filtered.sort((a, b) =>
          (b.bookingDateTime ?? "").localeCompare(a.bookingDateTime ?? "")
        );
        setBookings(filtered);
      } finally {
        if (!cancelled) setLoading(false);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, [user?.userId, user?.role]);

  return (
    <div>
      <h1 className="page-title">{user?.role === "ADMIN" ? "All bookings" : "Session bookings"}</h1>
      <p className="page-sub">
        {user?.role === "ADMIN"
          ? "Every booking on the platform. Status is CONFIRMED, CANCELLED, or COMPLETED."
          : "Bookings linked to your slots. Status is CONFIRMED, CANCELLED, or COMPLETED (backend model)."}
      </p>

      {loading ? (
        <div className="skeleton" style={{ height: 100 }} />
      ) : bookings.length === 0 ? (
        <div className="card empty">{user?.role === "ADMIN" ? "No bookings yet." : "No bookings on your slots yet."}</div>
      ) : (
        <div className="card" style={{ padding: 0, overflow: "hidden" }}>
          <table style={{ width: "100%", borderCollapse: "collapse", fontSize: "0.9rem" }}>
            <thead>
              <tr style={{ textAlign: "left", borderBottom: "1px solid var(--fn-border)" }}>
                <th style={{ padding: "0.85rem 1rem" }}>Member</th>
                <th style={{ padding: "0.85rem 1rem" }}>Date</th>
                <th style={{ padding: "0.85rem 1rem" }}>Time</th>
                <th style={{ padding: "0.85rem 1rem" }}>Status</th>
              </tr>
            </thead>
            <tbody>
              {bookings.map((b) => (
                <tr key={b.bookingId} style={{ borderBottom: "1px solid var(--fn-border)" }}>
                  <td style={{ padding: "0.85rem 1rem" }}>
                    {b.member ? `${b.member.firstName} ${b.member.lastName}` : "Member"}
                  </td>
                  <td style={{ padding: "0.85rem 1rem" }}>{formatDate(b.slot?.date)}</td>
                  <td style={{ padding: "0.85rem 1rem" }}>
                    {formatTime(b.slot?.startTime)}–{formatTime(b.slot?.endTime)}
                  </td>
                  <td style={{ padding: "0.85rem 1rem" }}>
                    <span className={statusBadgeClass(b.status)}>{b.status}</span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
