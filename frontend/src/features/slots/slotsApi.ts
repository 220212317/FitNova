import type { AvailabilitySlot } from "../../types";

export interface SlotFormData {
    date: string;
    startTime: string;
    endTime: string;
    status: string;
    trainerUserId: string;
}

const API_BASE_URL = "http://localhost:8080/availability-slots";

async function getErrorMessage(response: Response): Promise<string> {
    try {
        const data = await response.json();

        if (data?.message) {
            return data.message;
        }

        if (data?.error) {
            return data.error;
        }

        if (typeof data === "string") {
            return data;
        }

        return `Request failed with status ${response.status}`;
    } catch {
        return `Request failed with status ${response.status}`;
    }
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
    let response = await fetch(`${API_BASE_URL}/all`);

    if (!response.ok) {
        response = await fetch(`${API_BASE_URL}/getAll`);
    }

    if (!response.ok) {
        throw new Error(await getErrorMessage(response));
    }

    const data = await response.json();

    if (Array.isArray(data)) {
        return data;
    }

    if (Array.isArray(data?.data)) {
        return data.data;
    }

    return [];
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

    const response = await fetch(`${API_BASE_URL}/create`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
    });

    if (!response.ok) {
        throw new Error(await getErrorMessage(response));
    }

    return response.json();
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

    const response = await fetch(`${API_BASE_URL}/update`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
    });

    if (!response.ok) {
        throw new Error(await getErrorMessage(response));
    }

    return response.json();
}

export async function deleteSlot(slotId: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/delete/${slotId}`, {
        method: "DELETE",
    });

    if (!response.ok) {
        throw new Error(await getErrorMessage(response));
    }
}