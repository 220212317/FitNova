import { useEffect, useState, type CSSProperties, type FormEvent } from "react";
import { slotsApi } from "../../api/fitnova";
import { useAuth } from "../../auth/AuthContext";
import { useToast } from "../../components/Toast";
import type { AvailabilitySlot } from "../../types";
import { formatDate, formatTime, statusBadgeClass } from "../../lib/format";
import { ensureId } from "../../api/ids";

/**
 * Trainer-only "My slots" page. Admins use /admin/slots (read-only, all trainers).
 * Backend AvailabilitySlotServiceImpl only lets primary role TRAINER create slots.
 */
export function TrainerSlotsPage() {
  const { user } = useAuth();
  const { toast } = useToast();
  const [slots, setSlots] = useState<AvailabilitySlot[]>([]);
  const [loading, setLoading] = useState(true);
  const [date, setDate] = useState("");
  const [startTime, setStartTime] = useState("09:00");
  const [endTime, setEndTime] = useState("10:00");
  const [saving, setSaving] = useState(false);

  const load = async () => {
    setLoading(true);
    try {
      const all = await slotsApi.getAll().catch(() => [] as AvailabilitySlot[]);
      setSlots(all.filter((s) => s.trainer?.userId === user?.userId));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void load();
  }, [user?.userId, user?.role]);

  const createSlot = async (e: FormEvent) => {
    e.preventDefault();
    if (!date || !startTime || !endTime) {
      toast("Date and times are required.", "error");
      return;
    }
    setSaving(true);
    try {
      // Times as HH:mm — Jackson maps to LocalTime
      const slot: AvailabilitySlot = {
        slotId: ensureId(""),
        date,
        startTime: startTime.length === 5 ? `${startTime}:00` : startTime,
        endTime: endTime.length === 5 ? `${endTime}:00` : endTime,
        status: "AVAILABLE",
      };
      await slotsApi.create(slot);
      toast("Slot created (AVAILABLE).", "success");
      await load();
    } catch (err) {
      toast(err instanceof Error ? err.message : "Failed to create slot.", "error");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div>
      <h1 className="page-title">My slots</h1>
      <p className="page-sub">
        Publish availability. Status starts as AVAILABLE; a confirmed booking moves it to BOOKED.
      </p>

      <form className="card" onSubmit={createSlot} style={{ marginBottom: "1.25rem" }}>
        <h2 style={{ fontSize: "1rem", marginBottom: "0.85rem" }}>New availability</h2>
        <div className="grid-2" style={{ marginBottom: "0.75rem" }}>
          <div>
            <label style={{ fontSize: "0.8rem", color: "var(--fn-muted)" }}>Date</label>
            <input type="date" value={date} onChange={(e) => setDate(e.target.value)} style={inputStyle} />
          </div>
          <div />
          <div>
            <label style={{ fontSize: "0.8rem", color: "var(--fn-muted)" }}>Start</label>
            <input type="time" value={startTime} onChange={(e) => setStartTime(e.target.value)} style={inputStyle} />
          </div>
          <div>
            <label style={{ fontSize: "0.8rem", color: "var(--fn-muted)" }}>End</label>
            <input type="time" value={endTime} onChange={(e) => setEndTime(e.target.value)} style={inputStyle} />
          </div>
        </div>
        <button type="submit" className="btn" disabled={saving}>
          {saving ? "Saving…" : "Create available slot"}
        </button>
      </form>

      {loading ? (
        <div className="skeleton" style={{ height: 100 }} />
      ) : slots.length === 0 ? (
        <div className="card empty">No slots yet.</div>
      ) : (
        <div className="card" style={{ padding: 0, overflow: "hidden" }}>
          <table style={{ width: "100%", borderCollapse: "collapse", fontSize: "0.92rem" }}>
            <thead>
              <tr style={{ textAlign: "left", borderBottom: "1px solid var(--fn-border)" }}>
                <th style={{ padding: "0.85rem 1rem" }}>Date</th>
                <th style={{ padding: "0.85rem 1rem" }}>Time</th>
                <th style={{ padding: "0.85rem 1rem" }}>Trainer</th>
                <th style={{ padding: "0.85rem 1rem" }}>Status</th>
              </tr>
            </thead>
            <tbody>
              {slots.map((s) => (
                <tr key={s.slotId} style={{ borderBottom: "1px solid var(--fn-border)" }}>
                  <td style={{ padding: "0.85rem 1rem" }}>{formatDate(s.date)}</td>
                  <td style={{ padding: "0.85rem 1rem" }}>
                    {formatTime(s.startTime)}–{formatTime(s.endTime)}
                  </td>
                  <td style={{ padding: "0.85rem 1rem" }}>
                    {s.trainer ? `${s.trainer.firstName} ${s.trainer.lastName}` : "—"}
                  </td>
                  <td style={{ padding: "0.85rem 1rem" }}>
                    <span className={statusBadgeClass(s.status)}>{s.status}</span>
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

const inputStyle: CSSProperties = {
  display: "block",
  width: "100%",
  marginTop: "0.3rem",
  background: "var(--fn-bg)",
  border: "1px solid var(--fn-border)",
  borderRadius: 10,
  padding: "0.6rem 0.75rem",
};
