import {
  useEffect,
  useState,
  type CSSProperties,
  type FormEvent,
} from "react";
import { slotsApi } from "../../api/fitnova";
import { useAuth } from "../../auth/AuthContext";
import { useToast } from "../../components/ToastContext";
import type { AvailabilitySlot, SlotStatus } from "../../types";
import { formatDate, formatTime, statusBadgeClass } from "../../lib/format";
import { ensureId } from "../../api/ids";

type SlotFormStatus = Extract<SlotStatus, "AVAILABLE" | "UNAVAILABLE">;

export function TrainerSlotsPage() {
  const { user } = useAuth();
  const { toast } = useToast();

  const [slots, setSlots] = useState<AvailabilitySlot[]>([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const [date, setDate] = useState("");
  const [startTime, setStartTime] = useState("09:00");
  const [endTime, setEndTime] = useState("10:00");
  const [status, setStatus] = useState<SlotFormStatus>("AVAILABLE");
  const [editingSlotId, setEditingSlotId] = useState<string | null>(null);

  const resetForm = () => {
    setDate("");
    setStartTime("09:00");
    setEndTime("10:00");
    setStatus("AVAILABLE");
    setEditingSlotId(null);
  };

  useEffect(() => {
    if (!user) {
      return;
    }

    let cancelled = false;

    const fetchSlots = async () => {
      setLoading(true);

      try {
        const all = await slotsApi.getAll();
        const own = all.filter((slot) => slot.trainer?.userId === user.userId);

        if (cancelled) return;

        setSlots(own);
      } catch (err) {
        toast(
            err instanceof Error ? err.message : "Failed to load your slots.",
            "error",
        );
      } finally {
        if (!cancelled) {
          setLoading(false);
        }
      }
    };

    void fetchSlots();

    return () => {
      cancelled = true;
    };
  }, [toast, user]);

  const validateAndBuildSlot = () => {
    if (!date || !startTime || !endTime) {
      throw new Error("Date and times are required.");
    }

    if (!user) {
      throw new Error("You must be signed in.");
    }

    const start = startTime.length === 5 ? `${startTime}:00` : startTime;
    const end = endTime.length === 5 ? `${endTime}:00` : endTime;

    if (start >= end) {
      throw new Error("End time must be after start time.");
    }

    return {
      slotId: editingSlotId ?? ensureId(""),
      date,
      startTime: start,
      endTime: end,
      status,
      trainer: {
        userId: user.userId,
        firstName: user.firstName ?? "",
        lastName: user.lastName ?? "",
      },
    } as AvailabilitySlot;
  };

  const createOrUpdateSlot = async (e: FormEvent) => {
    e.preventDefault();

    try {
      setSaving(true);

      const payload = validateAndBuildSlot();

      if (editingSlotId) {
        await slotsApi.update(payload);
        toast("Slot updated successfully.", "success");
      } else {
        await slotsApi.create(payload);
        toast("Slot created successfully.", "success");
      }

      resetForm();

      if (user) {
        const all = await slotsApi.getAll();
        const own = all.filter((slot) => slot.trainer?.userId === user.userId);
        setSlots(own);
      }
    } catch (err) {
      toast(
          err instanceof Error ? err.message : "Failed to save slot.",
          "error",
      );
    } finally {
      setSaving(false);
    }
  };

  const handleEdit = (slot: AvailabilitySlot) => {
    if (slot.status === "BOOKED") {
      toast("Booked slots cannot be edited.", "error");
      return;
    }

    setEditingSlotId(slot.slotId);
    setDate(slot.date);
    setStartTime(slot.startTime);
    setEndTime(slot.endTime);
    setStatus(slot.status === "UNAVAILABLE" ? "UNAVAILABLE" : "AVAILABLE");
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const handleDelete = async (slot: AvailabilitySlot) => {
    if (slot.status === "BOOKED") {
      toast("Booked slots cannot be deleted.", "error");
      return;
    }

    const confirmed = window.confirm(
        `Delete this slot on ${formatDate(slot.date)} (${formatTime(slot.startTime)} - ${formatTime(slot.endTime)})?`,
    );

    if (!confirmed) return;

    try {
      await slotsApi.delete(slot.slotId);
      toast("Slot deleted successfully.", "success");

      if (editingSlotId === slot.slotId) {
        resetForm();
      }

      if (user) {
        const all = await slotsApi.getAll();
        const own = all.filter((slot) => slot.trainer?.userId === user.userId);
        setSlots(own);
      }
    } catch (err) {
      toast(
          err instanceof Error ? err.message : "Failed to delete slot.",
          "error",
      );
    }
  };

  const visibleSlots = user
      ? slots.filter((slot) => slot.trainer?.userId === user.userId)
      : [];
  const isLoading = Boolean(user) && loading;

  return (
      <div>
        <h1 className="page-title">My slots</h1>
        <p className="page-sub">
          Create, edit, and delete your availability. Booked slots are protected.
        </p>

        <form
            className="card"
            onSubmit={createOrUpdateSlot}
            style={{ marginBottom: "1.25rem" }}
        >
          <h2 style={{ fontSize: "1rem", marginBottom: "0.85rem" }}>
            {editingSlotId ? "Edit availability" : "New availability"}
          </h2>

          <div className="grid-2" style={{ marginBottom: "0.75rem" }}>
            <div>
              <label style={{ fontSize: "0.8rem", color: "var(--fn-muted)" }}>
                Date
              </label>
              <input
                  type="date"
                  value={date}
                  onChange={(e) => setDate(e.target.value)}
                  style={inputStyle}
              />
            </div>

            <div>
              <label style={{ fontSize: "0.8rem", color: "var(--fn-muted)" }}>
                Status
              </label>
              <select
                  value={status}
                  onChange={(e) => setStatus(e.target.value as SlotFormStatus)}
                  style={inputStyle}
              >
                <option value="AVAILABLE">AVAILABLE</option>
                <option value="UNAVAILABLE">UNAVAILABLE</option>
              </select>
            </div>

            <div>
              <label style={{ fontSize: "0.8rem", color: "var(--fn-muted)" }}>
                Start
              </label>
              <input
                  type="time"
                  value={startTime}
                  onChange={(e) => setStartTime(e.target.value)}
                  style={inputStyle}
              />
            </div>

            <div>
              <label style={{ fontSize: "0.8rem", color: "var(--fn-muted)" }}>
                End
              </label>
              <input
                  type="time"
                  value={endTime}
                  onChange={(e) => setEndTime(e.target.value)}
                  style={inputStyle}
              />
            </div>
          </div>

          <div style={{ display: "flex", gap: "0.75rem", flexWrap: "wrap" }}>
            <button type="submit" className="btn" disabled={saving}>
              {saving
                  ? "Saving..."
                  : editingSlotId
                      ? "Update slot"
                      : "Create slot"}
            </button>

            {editingSlotId && (
                <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={resetForm}
                    disabled={saving}
                >
                  Cancel
                </button>
            )}
          </div>
        </form>

        {isLoading ? (
            <div className="skeleton" style={{ height: 100 }} />
        ) : visibleSlots.length === 0 ? (
            <div className="card empty">No slots yet.</div>
        ) : (
            <div className="card" style={{ padding: 0, overflow: "hidden" }}>
              <table
                  style={{
                    width: "100%",
                    borderCollapse: "collapse",
                    fontSize: "0.92rem",
                  }}
              >
                <thead>
                <tr
                    style={{
                      textAlign: "left",
                      borderBottom: "1px solid var(--fn-border)",
                    }}
                >
                  <th style={{ padding: "0.85rem 1rem" }}>Date</th>
                  <th style={{ padding: "0.85rem 1rem" }}>Time</th>
                  <th style={{ padding: "0.85rem 1rem" }}>Status</th>
                  <th style={{ padding: "0.85rem 1rem" }}>Actions</th>
                </tr>
                </thead>

                <tbody>
                {visibleSlots.map((slot) => (
                    <tr
                        key={slot.slotId}
                        style={{ borderBottom: "1px solid var(--fn-border)" }}
                    >
                      <td style={{ padding: "0.85rem 1rem" }}>
                        {formatDate(slot.date)}
                      </td>

                      <td style={{ padding: "0.85rem 1rem" }}>
                        {formatTime(slot.startTime)} – {formatTime(slot.endTime)}
                      </td>

                      <td style={{ padding: "0.85rem 1rem" }}>
                    <span className={statusBadgeClass(slot.status)}>
                      {slot.status}
                    </span>
                      </td>

                      <td style={{ padding: "0.85rem 1rem" }}>
                        <div
                            style={{
                              display: "flex",
                              gap: "0.5rem",
                              flexWrap: "wrap",
                            }}
                        >
                          <button
                              type="button"
                              className="btn btn-secondary"
                              onClick={() => handleEdit(slot)}
                              disabled={slot.status === "BOOKED"}
                          >
                            Edit
                          </button>

                          <button
                              type="button"
                              className="btn btn-danger"
                              onClick={() => void handleDelete(slot)}
                              disabled={slot.status === "BOOKED"}
                          >
                            Delete
                          </button>
                        </div>
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