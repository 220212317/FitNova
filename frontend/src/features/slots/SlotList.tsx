/** TODO — Phumelela Sakie (240040546) */

import { deleteSlot } from "./slotsApi";

import type { AvailabilitySlot } from "../../types";
import {useMemo, useState} from "react";
interface SlotListProps {
    slots: AvailabilitySlot[];
    onEdit: (slot: AvailabilitySlot) => void;
    onDeleted: () => void;
}

function SlotList({
                      slots,
                      onEdit,
                      onDeleted,
                  }: SlotListProps) {
    const [statusFilter, setStatusFilter] = useState("ALL");
    const [deletingId, setDeletingId] = useState<string | null>(null);
    const [error, setError] = useState("");

    const filteredSlots = useMemo(() => {
        if (statusFilter === "ALL") {
            return slots;
        }

        return slots.filter(
            (slot) => slot.status === statusFilter
        );
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

    return (
        <div className="slot-list">
            <div>
                <h2>Availability Slots</h2>

                <label htmlFor="status-filter">
                    Filter by status:
                </label>

                <select
                    id="status-filter"
                    value={statusFilter}
                    onChange={(event) =>
                        setStatusFilter(event.target.value)
                    }
                >
                    <option value="ALL">
                        All
                    </option>

                    <option value="AVAILABLE">
                        Available
                    </option>

                    <option value="BOOKED">
                        Booked
                    </option>

                    <option value="UNAVAILABLE">
                        Unavailable
                    </option>
                </select>
            </div>

            {error && (
                <p role="alert">
                    {error}
                </p>
            )}

            {filteredSlots.length === 0 ? (
                <p>
                    No availability slots found.
                </p>
            ) : (
                <div>
                    {filteredSlots.map((slot) => (
                        <div
                            key={slot.slotId}
                            className="slot-card"
                        >
                            <h3>
                                {slot.date}
                            </h3>

                            <p>
                                <strong>Time:</strong>{" "}
                                {slot.startTime} - {slot.endTime}
                            </p>

                            <p>
                                <strong>Status:</strong>{" "}
                                {slot.status}
                            </p>

                            <p>
                                <strong>Trainer:</strong>{" "}
                                {getTrainerName(slot)}
                            </p>

                            <p>
                                <strong>Trainer ID:</strong>{" "}
                                {slot.trainer?.userId || "Not assigned"}
                            </p>

                            <p>
                                <strong>Slot ID:</strong>{" "}
                                {slot.slotId}
                            </p>

                            <div>
                                <button
                                    type="button"
                                    onClick={() => onEdit(slot)}
                                >
                                    Edit
                                </button>

                                <button
                                    type="button"
                                    onClick={() => handleDelete(slot)}
                                    disabled={
                                        deletingId === slot.slotId
                                    }
                                >
                                    {deletingId === slot.slotId
                                        ? "Deleting..."
                                        : "Delete"}
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default SlotList;