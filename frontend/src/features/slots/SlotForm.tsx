import { useState } from "react";
import type { FormEvent } from "react";

import type { AvailabilitySlot } from "../../types";
import { useAuth } from "../../auth/AuthContext";

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

function getLocalDate(): string {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, "0");
    const day = String(today.getDate()).padStart(2, "0");

    return `${year}-${month}-${day}`;
}

function SlotFormFields({
                            editingSlot,
                            onSaved,
                            onCancelEdit,
                        }: SlotFormFieldsProps) {
    const { user } = useAuth();

    const [date, setDate] = useState(editingSlot?.date ?? "");

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

    const [status, setStatus] = useState<"AVAILABLE" | "UNAVAILABLE">(
        editingSlot?.status === "UNAVAILABLE"
            ? "UNAVAILABLE"
            : "AVAILABLE"
    );

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    const isEditing = editingSlot !== null;
    const isBooked = editingSlot?.status === "BOOKED";

    if (!user || user.role !== "TRAINER") {
        return (
            <div className="slot-form-panel">
                <p className="slot-form-msg slot-form-msg--error" role="alert">
                    Only authenticated trainers can manage availability.
                </p>
            </div>
        );
    }

    if (
        editingSlot &&
        editingSlot.trainer?.userId !== user.userId
    ) {
        return (
            <div className="slot-form-panel">
                <p className="slot-form-msg slot-form-msg--error" role="alert">
                    You cannot edit another trainer's availability.
                </p>
            </div>
        );
    }

    if (isBooked) {
        return (
            <div className="slot-form-panel">
                <h2>Booked Availability</h2>

                <p className="slot-form-msg slot-form-msg--error" role="alert">
                    This slot is booked and cannot be edited.
                </p>

                <button
                    className="btn btn-ghost"
                    type="button"
                    onClick={onCancelEdit}
                >
                    Close
                </button>
            </div>
        );
    }

    function resetForm() {
        setDate("");
        setStartTime("");
        setEndTime("");
        setStatus("AVAILABLE");
    }

    function validateForm(): string {
        if (!date || !startTime || !endTime) {
            return "Date, start time and end time are required.";
        }

        if (!status) {
            return "Please select a slot status.";
        }

        const normalizedStart = normalizeTime(startTime);
        const normalizedEnd = normalizeTime(endTime);

        if (normalizedEnd <= normalizedStart) {
            return "End time must be after start time.";
        }

        const startDateTime = new Date(`${date}T${startTime}:00`);
        const endDateTime = new Date(`${date}T${endTime}:00`);

        if (
            Number.isNaN(startDateTime.getTime()) ||
            Number.isNaN(endDateTime.getTime())
        ) {
            return "Please enter a valid date and time.";
        }

        if (startDateTime <= new Date()) {
            return "Availability must start in the future.";
        }

        if (endDateTime <= startDateTime) {
            return "End time must be after start time.";
        }

        return "";
    }

    async function handleSubmit(event: FormEvent<HTMLFormElement>) {
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
        };

        try {
            setLoading(true);

            if (isEditing && editingSlot) {
                await updateSlot(editingSlot.slotId, formData);
                setSuccess("Availability slot updated successfully.");
            } else {
                await createSlot(formData);
                setSuccess("Availability slot created successfully.");
            }

            resetForm();
            onSaved();
        } catch (err: unknown) {
            setError(
                err instanceof Error
                    ? err.message
                    : "Unable to save availability slot. Please try again."
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
        <div className="slot-form-panel">
            <h2>
                {isEditing
                    ? "Edit Availability Slot"
                    : "Create Availability Slot"}
            </h2>

            <form onSubmit={handleSubmit}>
                <div className="slot-form-grid">
                    <div className="fn-field">
                        <label htmlFor="slot-date">Date</label>
                        <input
                            id="slot-date"
                            type="date"
                            min={getLocalDate()}
                            value={date}
                            onChange={(event) => setDate(event.target.value)}
                            required
                        />
                    </div>

                    <div className="fn-field">
                        <label htmlFor="slot-start-time">Start Time</label>
                        <input
                            id="slot-start-time"
                            type="time"
                            value={startTime}
                            onChange={(event) => setStartTime(event.target.value)}
                            required
                        />
                    </div>

                    <div className="fn-field">
                        <label htmlFor="slot-end-time">End Time</label>
                        <input
                            id="slot-end-time"
                            type="time"
                            min={startTime || undefined}
                            value={endTime}
                            onChange={(event) => setEndTime(event.target.value)}
                            required
                        />
                    </div>

                    <div className="fn-field">
                        <label htmlFor="slot-status">Status</label>
                        <select
                            id="slot-status"
                            value={status}
                            onChange={(event) =>
                                setStatus(
                                    event.target.value as
                                        | "AVAILABLE"
                                        | "UNAVAILABLE"
                                )
                            }
                        >
                            <option value="AVAILABLE">AVAILABLE</option>
                            <option value="UNAVAILABLE">UNAVAILABLE</option>
                        </select>
                    </div>

                    <div className="fn-field fn-field--wide">
                        <label htmlFor="slot-trainer">Trainer</label>
                        <input
                            id="slot-trainer"
                            type="text"
                            value={user.email}
                            readOnly
                            disabled
                        />
                        <small>
                            Availability will be linked to your logged-in
                            trainer account.
                        </small>
                    </div>
                </div>

                {error && (
                    <p
                        className="slot-form-msg slot-form-msg--error"
                        role="alert"
                    >
                        {error}
                    </p>
                )}

                {success && (
                    <p
                        className="slot-form-msg slot-form-msg--ok"
                        role="status"
                    >
                        {success}
                    </p>
                )}

                <div className="slot-form-actions">
                    <button
                        className="btn"
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
                            className="btn btn-ghost"
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
    const formKey = editingSlot?.slotId ?? "new-slot";

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