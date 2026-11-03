import { api } from "../../api/client";
import type { AvailabilitySlot } from "../../types";

export interface SlotFormData {
    date: string;
    startTime: string;
    endTime: string;
    status: "AVAILABLE" | "UNAVAILABLE";
}

// Convert HH:mm into HH:mm:ss for the backend.
export function normalizeTime(time: string): string {
    if (!time) {
        return "";
    }

    const parts = time.trim().split(":");

    if (parts.length === 2) {
        return `${parts[0].padStart(2, "0")}:${parts[1].padStart(2, "0")}:00`;
    }

    if (parts.length >= 3) {
        return `${parts[0].padStart(2, "0")}:${parts[1].padStart(2, "0")}:${parts[2]
            .substring(0, 2)
            .padStart(2, "0")}`;
    }

    return time.trim();
}

function getApiError(error: unknown, defaultMessage: string): string {
    if (typeof error === "object" && error !== null && "response" in error) {
        const response = (
            error as {
                response?: {
                    data?: {
                        message?: string;
                        error?: string;
                    };
                };
            }
        ).response;

        if (response?.data?.message) {
            return response.data.message;
        }

        if (response?.data?.error) {
            return response.data.error;
        }
    }

    if (error instanceof Error) {
        return error.message;
    }

    return defaultMessage;
}

// Get all availability slots.
export async function getAllSlots(): Promise<AvailabilitySlot[]> {
    try {
        const response = await api.get<
            AvailabilitySlot[] | { data?: AvailabilitySlot[] }
        >("/availability-slots/all");

        const data = response.data;

        if (Array.isArray(data)) {
            return data;
        }

        if (Array.isArray(data?.data)) {
            return data.data;
        }

        return [];
    } catch (error: unknown) {
        throw new Error(
            getApiError(error, "Unable to load availability slots.")
        );
    }
}

// Create availability for the authenticated trainer.
export async function createSlot(
    formData: SlotFormData
): Promise<AvailabilitySlot> {
    const payload = {
        slotId: "",
        date: formData.date,
        startTime: normalizeTime(formData.startTime),
        endTime: normalizeTime(formData.endTime),
        status: formData.status,
    };

    try {
        const response = await api.post<AvailabilitySlot>(
            "/availability-slots/create",
            payload
        );

        return response.data;
    } catch (error: unknown) {
        throw new Error(
            getApiError(error, "Unable to create availability slot.")
        );
    }
}

// Update an existing availability slot.
export async function updateSlot(
    slotId: string,
    formData: SlotFormData
): Promise<AvailabilitySlot> {
    if (!slotId.trim()) {
        throw new Error("Slot ID is required.");
    }

    const payload = {
        slotId,
        date: formData.date,
        startTime: normalizeTime(formData.startTime),
        endTime: normalizeTime(formData.endTime),
        status: formData.status,
    };

    try {
        const response = await api.put<AvailabilitySlot>(
            "/availability-slots/update",
            payload
        );

        return response.data;
    } catch (error: unknown) {
        throw new Error(
            getApiError(error, "Unable to update availability slot.")
        );
    }
}

// Delete an availability slot.
export async function deleteSlot(slotId: string): Promise<void> {
    if (!slotId.trim()) {
        throw new Error("Slot ID is required.");
    }

    try {
        await api.delete(
            `/availability-slots/delete/${encodeURIComponent(slotId)}`
        );
    } catch (error: unknown) {
        throw new Error(
            getApiError(error, "Unable to delete availability slot.")
        );
    }
}