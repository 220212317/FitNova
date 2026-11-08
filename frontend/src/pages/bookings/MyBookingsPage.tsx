import { useEffect, useState } from "react";
import { bookingsApi, slotsApi } from "../../api/fitnova";
import { useAuth } from "../../auth/AuthContext";
import { useToast } from "../../components/Toast";
import type { Booking } from "../../types";
import { formatDate, formatTime, statusBadgeClass } from "../../lib/format";

export function MyBookingsPage() {
  const { user } = useAuth();
  const { toast } = useToast();
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [loading, setLoading] = useState(true);
  const [busyId, setBusyId] = useState<string | null>(null);

  const load = async () => {
    setLoading(true);
    try {
      let list: Booking[] = [];
      if (user?.userId) {
        try {
          list = await bookingsApi.byMember(user.userId);
        } catch {
          const all = await bookingsApi.getAll().catch(() => [] as Booking[]);
          list = all.filter((b) => b.member?.userId === user.userId);
        }
      }
      list.sort((a, b) =>
        (b.bookingDateTime ?? "").localeCompare(a.bookingDateTime ?? "")
      );
      setBookings(list);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void load();
  }, [user?.userId]);

  const cancel = async (b: Booking) => {
    if (b.status !== "CONFIRMED") return;
    setBusyId(b.bookingId);
    try {
      await bookingsApi.update({ ...b, status: "CANCELLED" });
      if (b.slot?.slotId) {
        try {
          await slotsApi.update({ ...b.slot, status: "AVAILABLE" });
        } catch {
          /* may require trainer privileges */
        }
      }
      toast("Booking cancelled. Slot should be available again.", "success");
      await load();
    } catch (e) {
      toast(e instanceof Error ? e.message : "Cancel failed.", "error");
    } finally {
      setBusyId(null);
    }
  };

  return (
    <div>
      <h1 className="page-title">My bookings</h1>
      <p className="page-sub">
        Confirmed sessions you have booked. Cancel before the session starts to free the slot.
      </p>

      {loading ? (
        <div className="skeleton" style={{ height: 100 }} />
      ) : bookings.length === 0 ? (
        <div className="card empty">No bookings yet. Open Book session to reserve a slot.</div>
      ) : (
        <div className="card" style={{ padding: 0, overflow: "hidden" }}>
          <table style={{ width: "100%", borderCollapse: "collapse", fontSize: "0.92rem" }}>
            <thead>
              <tr style={{ textAlign: "left", borderBottom: "1px solid var(--fn-border)" }}>
                <th style={{ padding: "0.85rem 1rem" }}>Date</th>
                <th style={{ padding: "0.85rem 1rem" }}>Time</th>
                <th style={{ padding: "0.85rem 1rem" }}>Trainer</th>
                <th style={{ padding: "0.85rem 1rem" }}>Status</th>
                <th style={{ padding: "0.85rem 1rem" }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {bookings.map((b) => (
                <tr key={b.bookingId} style={{ borderBottom: "1px solid var(--fn-border)" }}>
                  <td style={{ padding: "0.85rem 1rem" }}>{formatDate(b.slot?.date)}</td>
                  <td style={{ padding: "0.85rem 1rem" }}>
                    {formatTime(b.slot?.startTime)}–{formatTime(b.slot?.endTime)}
                  </td>
                  <td style={{ padding: "0.85rem 1rem" }}>
                    {b.slot?.trainer
                      ? `${b.slot.trainer.firstName} ${b.slot.trainer.lastName}`
                      : "—"}
                  </td>
                  <td style={{ padding: "0.85rem 1rem" }}>
                    <span className={statusBadgeClass(b.status)}>{b.status}</span>
                  </td>
                  <td style={{ padding: "0.85rem 1rem" }}>
                    {b.status === "CONFIRMED" ? (
                      <button
                        type="button"
                        className="btn btn-outline"
                        style={{ padding: "0.35rem 0.75rem", fontSize: "0.8rem" }}
                        disabled={busyId === b.bookingId}
                        onClick={() => void cancel(b)}
                      >
                        Cancel
                      </button>
                    ) : (
                      <span style={{ color: "var(--fn-muted)" }}>—</span>
                    )}
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
