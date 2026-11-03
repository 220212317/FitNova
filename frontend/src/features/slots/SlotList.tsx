import { useMemo, useState } from "react";
import { Pencil, Trash2 } from "lucide-react";

import type { AvailabilitySlot } from "../../types";
import { useAuth } from "../../auth/AuthContext";
import { deleteSlot } from "./slotsApi";

interface SlotListProps {
    slots: AvailabilitySlot[];
    onEdit: (slot: AvailabilitySlot) => void;
    onDeleted: () => void;
}

function SlotList({ slots, onEdit, onDeleted }: SlotListProps) {
    const { user } = useAuth();

    const [statusFilter, setStatusFilter] = useState("ALL");
    const [deletingId, setDeletingId] = useState<string | null>(null);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    const trainerSlots = useMemo(() => {
        if (!user || user.role !== "TRAINER") {
            return [];
        }

        return slots.filter(
            (slot) => slot.trainer?.userId === user.userId
        );
    }, [slots, user]);

    const filteredSlots = useMemo(() => {
        if (statusFilter === "ALL") {
            return trainerSlots;
        }

        return trainerSlots.filter(
            (slot) => slot.status === statusFilter
        );
    }, [trainerSlots, statusFilter]);

    async function handleDelete(slot: AvailabilitySlot) {
        if (slot.trainer?.userId !== user?.userId) {
            setError("You cannot delete another trainer's availability.");
            return;
        }

        if (slot.status === "BOOKED") {
            setError("Booked slots cannot be deleted.");
            return;
        }

        const confirmed = window.confirm(
            `Are you sure you want to delete the availability slot on ${slot.date} from ${slot.startTime} to ${slot.endTime}?`
        );

        if (!confirmed) {
            return;
        }

        try {
            setError("");
            setSuccess("");
            setDeletingId(slot.slotId);

            await deleteSlot(slot.slotId);

            setSuccess("Availability slot deleted successfully.");
            onDeleted();
        } catch (err: unknown) {
            setError(
                err instanceof Error
                    ? err.message
                    : "Unable to delete availability slot. Please try again."
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

        return fullName || slot.trainer.userId || "Not assigned";
    }

    function statusBadgeClass(status: string): string {
        const currentStatus = status?.toLowerCase() ?? "";

        if (currentStatus === "available") {
            return "badge available";
        }

        if (currentStatus === "booked") {
            return "badge booked";
        }

        if (currentStatus === "unavailable") {
            return "badge unavailable";
        }

        return "badge";
    }

    if (!user || user.role !== "TRAINER") {
        return (
            <div className="slot-list">
                <div className="alert alert-error" role="alert">
                    Only authenticated trainers can view availability slots.
                </div>
            </div>
        );
    }

    return (
        <div className="slot-list">
            <div className="slot-list-header">
                <h2>My Availability Slots</h2>

                <div className="slot-filter">
                    <label htmlFor="status-filter">
                        Filter by status
                    </label>

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

            {success && (
                <div className="alert alert-success" role="status">
                    {success}
                </div>
            )}

            {filteredSlots.length === 0 ? (
                <div className="fn-state">
                    {trainerSlots.length === 0
                        ? "You have not created any availability slots yet."
                        : "No availability slots match this status filter."}
                </div>
            ) : (
                <div className="slot-cards">
                    {filteredSlots.map((slot) => {
                        const isBooked = slot.status === "BOOKED";
                        const isDeleting = deletingId === slot.slotId;

                        return (
                            <article
                                key={slot.slotId}
                                className="slot-card"
                            >
                                <h3 className="slot-card__date">
                                    {slot.date}
                                </h3>

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
                                    <strong>Slot ID</strong>
                                    <span className="slot-card__id">
                                        {slot.slotId}
                                    </span>
                                </p>

                                <div className="slot-card__actions">
                                    {!isBooked && (
                                        <button
                                            className="btn-icon"
                                            type="button"
                                            onClick={() => onEdit(slot)}
                                            title="Edit availability"
                                        >
                                            <Pencil size={14} />
                                            Edit
                                        </button>
                                    )}

                                    <button
                                        className="btn-icon danger"
                                        type="button"
                                        onClick={() => handleDelete(slot)}
                                        disabled={isBooked || isDeleting}
                                        title={
                                            isBooked
                                                ? "Booked slots cannot be deleted"
                                                : "Delete availability"
                                        }
                                    >
                                        <Trash2 size={14} />
                                        {isDeleting ? "Deleting..." : "Delete"}
                                    </button>
                                </div>

                                {isBooked && (
                                    <small>
                                        This slot is booked and cannot be edited
                                        or deleted.
                                    </small>
                                )}
                            </article>
                        );
                    })}
                </div>
            )}
        </div>
    );
}

export default SlotList;