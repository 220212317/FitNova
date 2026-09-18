import { api } from "../../api/client";
import { ensureId } from "../../api/ids";
import type { AvailabilitySlot } from "../../types";

export interface SlotFormData {
    date: string;
    startTime: string;
    endTime: string;
    status: string;
    trainerUserId: string;
}

export function normalizeTime(time: string): string {
    if (!time) {
        return "";
    }

    const cleanedTime = time.trim();
    const parts = cleanedTime.split(":");

    if (parts.length === 2) {
        return `${parts[0].padStart(2, "0")}:${parts[1].padStart(2, "0")}:00`;
    }

    if (parts.length >= 3) {
        return `${parts[0].padStart(2, "0")}:${parts[1].padStart(2, "0")}:${parts[2]
            .substring(0, 2)
            .padStart(2, "0")}`;
    }

    return cleanedTime;
}

export async function getAllSlots(): Promise<AvailabilitySlot[]> {
    try {
        const data = await api.get<AvailabilitySlot[] | { data?: AvailabilitySlot[] }>(
            "/availability-slots/all"
        );
        if (Array.isArray(data)) return data;
        if (Array.isArray(data?.data)) return data.data;
        return [];
    } catch {
        try {
            const data = await api.get<AvailabilitySlot[] | { data?: AvailabilitySlot[] }>(
                "/availability-slots/getAll"
            );
            if (Array.isArray(data)) return data;
            if (Array.isArray(data?.data)) return data.data;
            return [];
        } catch {
            return [];
        }
    }
}

export async function createSlot(formData: SlotFormData): Promise<AvailabilitySlot> {
    const payload = {
        slotId: ensureId(),
        date: formData.date,
        startTime: normalizeTime(formData.startTime),
        endTime: normalizeTime(formData.endTime),
        status: formData.status,
        trainer: {
            userId: formData.trainerUserId,
        },
    };

    return api.post<AvailabilitySlot>("/availability-slots/create", payload);
}

export async function updateSlot(
    slotId: string,
    formData: SlotFormData
): Promise<AvailabilitySlot> {
    const payload = {
        slotId,
        date: formData.date,
        startTime: normalizeTime(formData.startTime),
        endTime: normalizeTime(formData.endTime),
        status: formData.status,
        trainer: {
            userId: formData.trainerUserId,
        },
    };

    return api.put<AvailabilitySlot>("/availability-slots/update", payload);
}

export async function deleteSlot(slotId: string): Promise<void> {
    await api.delete(`/availability-slots/delete/${encodeURIComponent(slotId)}`);
}