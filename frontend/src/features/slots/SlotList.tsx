/** TODO — Phumelela Sakie (240040546) */

import { deleteSlot } from "./slotsApi";

import type { AvailabilitySlot } from "../../types";
import { useMemo, useState } from "react";
import { Pencil, Trash2 } from "lucide-react";

interface SlotListProps {
    slots: AvailabilitySlot[];
    onEdit: (slot: AvailabilitySlot) => void;
    onDeleted: () => void;
}

function SlotList({ slots, onEdit, onDeleted }: SlotListProps) {
    const [statusFilter, setStatusFilter] = useState("ALL");
    const [deletingId, setDeletingId] = useState<string | null>(null);
    const [error, setError] = useState("");

    const filteredSlots = useMemo(() => {
        if (statusFilter === "ALL") {
            return slots;
        }

        return slots.filter((slot) => slot.status === statusFilter);
    }, [slots, statusFilter]);

    async function handleDelete(slot: AvailabilitySlot) {
        const confirmed = window.confirm(
            `Are you sure you want to delete the availability slot on ${slot.date} from ${slot.startTime} to ${slot.endTime}?`
        );

        if (!confirmed) {
            return;
        }

        try {
            setError("");
            setDeletingId(slot.slotId);

            await deleteSlot(slot.slotId);

            onDeleted();
        } catch (err) {
            setError(
                err instanceof Error
                    ? err.message
                    : "Unable to delete availability slot."
            );
        } finally {
            setDeletingId(null);
        }
    }

    function getTrainerName(slot: AvailabilitySlot): string {
        if (!slot.trainer) {
            return "Not assigned";
        }

        const firstName = slot.trainer.firstName || "";
        const lastName = slot.trainer.lastName || "";

        const fullName = `${firstName} ${lastName}`.trim();

        if (fullName) {
            return fullName;
        }

        if (slot.trainer.userId) {
            return slot.trainer.userId;
        }

        return "Not assigned";
    }

    function statusBadgeClass(status: string): string {
        const s = status?.toLowerCase() ?? "";
        if (s === "available") return "badge available";
        if (s === "booked") return "badge booked";
        if (s === "unavailable") return "badge unavailable";
        return "badge";
    }

    return (
        <div className="slot-list">
            <div className="slot-list-header">
                <h2>Availability slots</h2>

                <div className="slot-filter">
                    <label htmlFor="status-filter">Filter by status</label>
                    <select
                        id="status-filter"
                        value={statusFilter}
                        onChange={(event) =>
                            setStatusFilter(event.target.value)
                        }
                    >
                        <option value="ALL">All</option>
                        <option value="AVAILABLE">Available</option>
                        <option value="BOOKED">Booked</option>
                        <option value="UNAVAILABLE">Unavailable</option>
                    </select>
                </div>
            </div>

            {error && (
                <div className="alert alert-error" role="alert">
                    {error}
                </div>
            )}

            {filteredSlots.length === 0 ? (
                <div className="fn-state">No availability slots found.</div>
            ) : (
                <div className="slot-cards">
                    {filteredSlots.map((slot) => (
                        <article key={slot.slotId} className="slot-card">
                            <h3 className="slot-card__date">{slot.date}</h3>

                            <p className="slot-card__row">
                                <strong>Time</strong>
                                <span>
                                    {slot.startTime} – {slot.endTime}
                                </span>
                            </p>

                            <p className="slot-card__row">
                                <strong>Status</strong>
                                <span className={statusBadgeClass(slot.status)}>
                                    {slot.status}
                                </span>
                            </p>

                            <p className="slot-card__row">
                                <strong>Trainer</strong>
                                <span>{getTrainerName(slot)}</span>
                            </p>

                            <p className="slot-card__row">
                                <strong>Trainer ID</strong>
                                <span className="slot-card__id">
                                    {slot.trainer?.userId || "Not assigned"}
                                </span>
                            </p>

                            <p className="slot-card__row">
                                <strong>Slot ID</strong>
                                <span className="slot-card__id">
                                    {slot.slotId}
                                </span>
                            </p>

                            <div className="slot-card__actions">
                                <button
                                    className="btn-icon"
                                    type="button"
                                    onClick={() => onEdit(slot)}
                                    title="Edit"
                                >
                                    <Pencil size={14} />
                                    Edit
                                </button>

                                <button
                                    className="btn-icon danger"
                                    type="button"
                                    onClick={() => handleDelete(slot)}
                                    disabled={deletingId === slot.slotId}
                                    title="Delete"
                                >
                                    <Trash2 size={14} />
                                    {deletingId === slot.slotId
                                        ? "Deleting..."
                                        : "Delete"}
                                </button>
                            </div>
                        </article>
                    ))}
                </div>
            )}
        </div>
    );
}

export default SlotList;