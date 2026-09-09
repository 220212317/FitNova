/** TODO — Phumelela Sakie (240040546) */

import { useState } from "react";
import type { FormEvent } from "react";

import type { AvailabilitySlot } from "../../types";

import {
    createSlot,
    normalizeTime,
    updateSlot,
} from "./slotsApi";

import type { SlotFormData } from "./slotsApi";

interface SlotFormProps {
    editingSlot: AvailabilitySlot | null;
    onSaved: () => void;
    onCancelEdit: () => void;
}

interface SlotFormFieldsProps {
    editingSlot: AvailabilitySlot | null;
    onSaved: () => void;
    onCancelEdit: () => void;
}

function SlotFormFields({
                            editingSlot,
                            onSaved,
                            onCancelEdit,
                        }: SlotFormFieldsProps) {

    const [date, setDate] = useState(
        editingSlot?.date ?? ""
    );

    const [startTime, setStartTime] = useState(
        editingSlot?.startTime
            ? editingSlot.startTime.substring(0, 5)
            : ""
    );

    const [endTime, setEndTime] = useState(
        editingSlot?.endTime
            ? editingSlot.endTime.substring(0, 5)
            : ""
    );

    const [status, setStatus] = useState(
        editingSlot?.status ?? "AVAILABLE"
    );

    const [trainerUserId, setTrainerUserId] = useState(
        editingSlot?.trainer?.userId ?? ""
    );

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    const isEditing = editingSlot !== null;

    function resetForm() {
        setDate("");
        setStartTime("");
        setEndTime("");
        setStatus("AVAILABLE");
        setTrainerUserId("");
    }

    function validateForm(): string {
        if (!date) {
            return "Date is required.";
        }

        if (!startTime) {
            return "Start time is required.";
        }

        if (!endTime) {
            return "End time is required.";
        }

        if (!status) {
            return "Status is required.";
        }

        if (!trainerUserId.trim()) {
            return "Trainer is required.";
        }

        const normalizedStart = normalizeTime(startTime);
        const normalizedEnd = normalizeTime(endTime);

        if (normalizedEnd <= normalizedStart) {
            return "End time must be after start time.";
        }

        return "";
    }

    async function handleSubmit(
        event: FormEvent<HTMLFormElement>
    ) {
        event.preventDefault();

        setError("");
        setSuccess("");

        const validationError = validateForm();

        if (validationError) {
            setError(validationError);
            return;
        }

        const formData: SlotFormData = {
            date,
            startTime,
            endTime,
            status,
            trainerUserId: trainerUserId.trim(),
        };

        try {
            setLoading(true);

            if (isEditing && editingSlot) {
                await updateSlot(
                    editingSlot.slotId,
                    formData
                );

                setSuccess(
                    "Availability slot updated successfully."
                );
            } else {
                await createSlot(formData);

                setSuccess(
                    "Availability slot created successfully."
                );
            }

            resetForm();
            onSaved();

        } catch (err) {
            setError(
                err instanceof Error
                    ? err.message
                    : "Unable to save availability slot."
            );
        } finally {
            setLoading(false);
        }
    }

    function handleCancel() {
        resetForm();
        setError("");
        setSuccess("");
        onCancelEdit();
    }

    return (
        <div className="slot-form">

            <h2>
                {isEditing
                    ? "Edit Availability Slot"
                    : "Create Availability Slot"}
            </h2>

            <form onSubmit={handleSubmit}>

                <div>
                    <label htmlFor="slot-date">
                        Date
                    </label>

                    <input
                        id="slot-date"
                        type="date"
                        value={date}
                        onChange={(event) =>
                            setDate(event.target.value)
                        }
                        required
                    />
                </div>

                <div>
                    <label htmlFor="slot-start-time">
                        Start Time
                    </label>

                    <input
                        id="slot-start-time"
                        type="time"
                        value={startTime}
                        onChange={(event) =>
                            setStartTime(event.target.value)
                        }
                        required
                    />
                </div>

                <div>
                    <label htmlFor="slot-end-time">
                        End Time
                    </label>

                    <input
                        id="slot-end-time"
                        type="time"
                        value={endTime}
                        onChange={(event) =>
                            setEndTime(event.target.value)
                        }
                        required
                    />
                </div>

                <div>
                    <label htmlFor="slot-status">
                        Status
                    </label>

                    <select
                        id="slot-status"
                        value={status}
                        onChange={(event) =>
                            setStatus(
                                event.target.value as "AVAILABLE" | "BOOKED" | "UNAVAILABLE"
                            )
                        }
                    >
                        <option value="AVAILABLE">
                            AVAILABLE
                        </option>

                        <option value="BOOKED">
                            BOOKED
                        </option>

                        <option value="UNAVAILABLE">
                            UNAVAILABLE
                        </option>
                    </select>

                </div>

                <div>
                    <label htmlFor="trainer-user-id">
                        Trainer User ID
                    </label>

                    <input
                        id="trainer-user-id"
                        type="text"
                        value={trainerUserId}
                        onChange={(event) =>
                            setTrainerUserId(
                                event.target.value
                            )
                        }
                        placeholder="Enter trainer user ID"
                        required
                    />
                </div>

                {error && (
                    <p role="alert">
                        {error}
                    </p>
                )}

                {success && (
                    <p role="status">
                        {success}
                    </p>
                )}

                <div>
                    <button
                        type="submit"
                        disabled={loading}
                    >
                        {loading
                            ? "Saving..."
                            : isEditing
                                ? "Update Slot"
                                : "Create Slot"}
                    </button>

                    {isEditing && (
                        <button
                            type="button"
                            onClick={handleCancel}
                            disabled={loading}
                        >
                            Cancel
                        </button>
                    )}
                </div>

            </form>
        </div>
    );
}

function SlotForm({
                      editingSlot,
                      onSaved,
                      onCancelEdit,
                  }: SlotFormProps) {

    /*
     * The key forces React to create a fresh form whenever
     * the selected slot changes.
     *
     * This removes the need for useEffect + setState.
     */

    const formKey =
        editingSlot?.slotId ?? "new-slot";

    return (
        <SlotFormFields
            key={formKey}
            editingSlot={editingSlot}
            onSaved={onSaved}
            onCancelEdit={onCancelEdit}
        />
    );
}

export default SlotForm;