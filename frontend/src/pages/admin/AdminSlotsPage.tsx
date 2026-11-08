import { useEffect, useMemo, useState, type CSSProperties } from "react";
import { slotsApi } from "../../api/fitnova";
import type { AvailabilitySlot, SlotStatus } from "../../types";
import { formatDate, formatTime, statusBadgeClass } from "../../lib/format";

/**
 * Admin view of every trainer's slots. Read-only: admins oversee availability but
 * do not book or publish slots themselves (trainers publish, members book).
 */
export function AdminSlotsPage() {
  const [slots, setSlots] = useState<AvailabilitySlot[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [status, setStatus] = useState<SlotStatus | "ALL">("ALL");
  const [trainerId, setTrainerId] = useState("ALL");
  const [upcomingOnly, setUpcomingOnly] = useState(true);

  const load = async () => {
    setLoading(true);
    setError(null);
    try {
      setSlots(await slotsApi.getAll());
    } catch (e) {
      setError(e instanceof Error ? e.message : "Could not load slots.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void load();
  }, []);

  const trainers = useMemo(() => {
    const map = new Map<string, string>();
    for (const s of slots) {
      if (s.trainer?.userId) {
        map.set(s.trainer.userId, `${s.trainer.firstName} ${s.trainer.lastName}`.trim());
      }
    }
    return [...map.entries()].sort((a, b) => a[1].localeCompare(b[1]));
  }, [slots]);

  const visible = useMemo(() => {
    const now = new Date();
    return slots
        .filter((s) => status === "ALL" || s.status === status)
        .filter((s) => trainerId === "ALL" || s.trainer?.userId === trainerId)
        .filter((s) => {
          if (!upcomingOnly) return true;
          return new Date(`${s.date}T${s.endTime || "23:59:59"}`) >= now;
        })
        .sort((a, b) =>
            `${a.date}T${a.startTime}`.localeCompare(`${b.date}T${b.startTime}`)
        );
  }, [slots, status, trainerId, upcomingOnly]);

  const count = (st: SlotStatus) => slots.filter((s) => s.status === st).length;

  return (
      <div>
        <h1 className="page-title">Slots</h1>
        <p className="page-sub">
          Every trainer&apos;s availability across the gym. Trainers publish slots and members book
          them; admins can only view them here.
        </p>

        <div className="stat-row" style={{ marginBottom: "1.25rem" }}>
          <div className="stat-card">
            <span>Total</span>
            <strong>{loading ? "…" : slots.length}</strong>
          </div>
          <div className="stat-card">
            <span>Available</span>
            <strong>{loading ? "…" : count("AVAILABLE")}</strong>
          </div>
          <div className="stat-card">
            <span>Booked</span>
            <strong>{loading ? "…" : count("BOOKED")}</strong>
          </div>
          <div className="stat-card">
            <span>Unavailable</span>
            <strong>{loading ? "…" : count("UNAVAILABLE")}</strong>
          </div>
        </div>

        <div className="card" style={{ marginBottom: "1rem", display: "flex", gap: "0.75rem", flexWrap: "wrap", alignItems: "center" }}>
          <select
              aria-label="Filter by status"
              value={status}
              onChange={(e) => setStatus(e.target.value as SlotStatus | "ALL")}
              style={selectStyle}
          >
            <option value="ALL">All statuses</option>
            <option value="AVAILABLE">Available</option>
            <option value="BOOKED">Booked</option>
            <option value="UNAVAILABLE">Unavailable</option>
          </select>
          <select
              aria-label="Filter by trainer"
              value={trainerId}
              onChange={(e) => setTrainerId(e.target.value)}
              style={selectStyle}
          >
            <option value="ALL">All trainers</option>
            {trainers.map(([id, name]) => (
                <option key={id} value={id}>
                  {name}
                </option>
            ))}
          </select>
          <label style={{ display: "flex", alignItems: "center", gap: "0.4rem", fontSize: "0.9rem" }}>
            <input
                type="checkbox"
                checked={upcomingOnly}
                onChange={(e) => setUpcomingOnly(e.target.checked)}
            />
            Upcoming only
          </label>
        </div>

        {loading ? (
            <div className="skeleton" style={{ height: 120 }} />
        ) : error ? (
            <div className="card empty">
              <p style={{ marginBottom: "0.75rem" }}>{error}</p>
              <button type="button" className="btn btn-outline" onClick={() => void load()}>
                Try again
              </button>
            </div>
        ) : visible.length === 0 ? (
            <div className="card empty">No slots match these filters.</div>
        ) : (
            <div className="card" style={{ padding: 0, overflow: "auto" }}>
              <table style={{ width: "100%", borderCollapse: "collapse", fontSize: "0.92rem" }}>
                <thead>
                <tr style={{ textAlign: "left", borderBottom: "1px solid var(--fn-border)" }}>
                  <th style={cell}>Date</th>
                  <th style={cell}>Time</th>
                  <th style={cell}>Trainer</th>
                  <th style={cell}>Status</th>
                </tr>
                </thead>
                <tbody>
                {visible.map((s) => (
                    <tr key={s.slotId} style={{ borderBottom: "1px solid var(--fn-border)" }}>
                      <td style={cell}>{formatDate(s.date)}</td>
                      <td style={cell}>
                        {formatTime(s.startTime)}–{formatTime(s.endTime)}
                      </td>
                      <td style={cell}>
                        {s.trainer ? `${s.trainer.firstName} ${s.trainer.lastName}` : "—"}
                      </td>
                      <td style={cell}>
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

const cell: CSSProperties = { padding: "0.85rem 1rem" };

const selectStyle: CSSProperties = {
  background: "var(--fn-bg)",
  border: "1px solid var(--fn-border)",
  borderRadius: 10,
  padding: "0.5rem 0.75rem",
};
