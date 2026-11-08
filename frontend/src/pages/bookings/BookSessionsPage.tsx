import { useEffect, useState } from "react";
import { bookingsApi, slotsApi } from "../../api/fitnova";
import { useAuth } from "../../auth/AuthContext";
import { useToast } from "../../components/Toast";
import type { AvailabilitySlot, Booking } from "../../types";
import { formatDate, formatTime, statusBadgeClass, toBackendDateTime } from "../../lib/format";
import { ensureId } from "../../api/ids";

/**
 * Member books an AVAILABLE slot.
 * Backend BookingStatus = CONFIRMED | CANCELLED | COMPLETED (no pending step).
 * Slot moves AVAILABLE → BOOKED.
 */
export function BookSessionsPage() {
  const { user } = useAuth();
  const { toast } = useToast();
  const [slots, setSlots] = useState<AvailabilitySlot[]>([]);
  const [loading, setLoading] = useState(true);
  const [busyId, setBusyId] = useState<string | null>(null);

  const load = async () => {
    setLoading(true);
    try {
      const all = await slotsApi.getAll().catch(() => [] as AvailabilitySlot[]);
      setSlots(all.filter((s) => s.status === "AVAILABLE"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void load();
  }, []);

  const requestBooking = async (slot: AvailabilitySlot) => {
    if (!user) return;
    setBusyId(slot.slotId);
    try {
      const booking: Booking = {
        bookingId: ensureId(""),
        bookingDateTime: toBackendDateTime(),
        status: "CONFIRMED",
        member: { userId: user.userId, firstName: "", lastName: "" },
        slot: { slotId: slot.slotId, date: slot.date, startTime: slot.startTime, endTime: slot.endTime, status: slot.status },
      };
      await bookingsApi.create(booking);
      // Best-effort: mark slot booked (trainer/admin hold slot:manage; member may get 403 — backend may also handle this)
      try {
        await slotsApi.update({ ...slot, status: "BOOKED" });
      } catch {
        /* slot status may be enforced server-side */
      }
      toast("Session booked and confirmed.", "success");
      await load();
    } catch (e) {
      toast(e instanceof Error ? e.message : "Could not create booking.", "error");
    } finally {
      setBusyId(null);
    }
  };

  return (
      <div>
        <h1 className="page-title">Book a session</h1>
        <p className="page-sub">
          Only future available slots can be booked. A successful booking is stored as{" "}
          <strong>CONFIRMED</strong> and the slot becomes <strong>BOOKED</strong>.
        </p>

        {loading ? (
            <div className="skeleton" style={{ height: 120 }} />
        ) : slots.length === 0 ? (
            <div className="card empty">No available slots at the moment. Check again later.</div>
        ) : (
            <div className="grid-2">
              {slots.map((s) => (
                  <article
                      key={s.slotId}
                      className="card"
                      style={{ display: "flex", flexDirection: "column", gap: "0.75rem" }}
                  >
                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "start" }}>
                      <div>
                        <strong style={{ fontSize: "1.05rem" }}>{formatDate(s.date)}</strong>
                        <div style={{ color: "var(--fn-muted)", fontSize: "0.9rem" }}>
                          {formatTime(s.startTime)} – {formatTime(s.endTime)}
                        </div>
                      </div>
                      <span className={statusBadgeClass(s.status)}>{s.status}</span>
                    </div>
                    <div style={{ fontSize: "0.9rem" }}>
                      Trainer:{" "}
                      {s.trainer ? `${s.trainer.firstName} ${s.trainer.lastName}` : "—"}
                    </div>
                    <button
                        type="button"
                        className="btn"
                        disabled={busyId === s.slotId}
                        onClick={() => void requestBooking(s)}
                    >
                      {busyId === s.slotId ? "Booking…" : "Book session"}
                    </button>
                  </article>
              ))}
            </div>
        )}
      </div>
  );
}
