import type { AvailabilitySlot } from "../../types";
import api from "../../api/client";
import { ensureId } from "../../api/client";
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
    const response = await api.get<AvailabilitySlot[]>("/availability-slots/getAll");

    return Array.isArray(response.data) ? response.data : [];

}
export async function createSlot(
    formData: SlotFormData
): Promise<AvailabilitySlot> {
    const payload = {
        date: formData.date,
        startTime: normalizeTime(formData.startTime),
        endTime: normalizeTime(formData.endTime),
        status: formData.status,
        trainer: {
            userId: formData.trainerUserId,
        },
    };

    const response = await api.post<AvailabilitySlot>(
        "/availability-slots/create",
        payload
    );

    return response.data;

}
export async function updateSlot(
    slotId: string,
    formData: SlotFormData
): Promise<AvailabilitySlot> {
    const payload = {
        slotId: ensureId(slotId),
        date: formData.date,
        startTime: normalizeTime(formData.startTime),
        endTime: normalizeTime(formData.endTime),
        status: formData.status,
        trainer: {
            userId: formData.trainerUserId,
        },
    };

    const response = await api.put<AvailabilitySlot>(
        "/availability-slots/update",
        payload
    );

    return response.data;

}
export async function deleteSlot(slotId: string): Promise<void> {
    await api.delete(
        /availability-slots/delete/${ensureId(slotId)}
);
}