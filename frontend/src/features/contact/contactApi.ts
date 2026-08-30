/** TODO — Lisakhanya Tshokolo (220239215) */

const BASE_URL = (import.meta as any).env?.VITE_API_BASE_URL ?? "";

async function request<T>(path: string, options?: RequestInit): Promise<T> {
    const res = await fetch(`${BASE_URL}${path}`, {
        headers: { "Content-Type": "application/json" },
        ...options,
    });

    if (!res.ok) {
        const message = await res.text().catch(() => res.statusText);
        throw new Error(`Request to ${path} failed (${res.status}): ${message}`);
    }

    if (res.status === 204) {
        return undefined as T;
    }

    return res.json() as Promise<T>;
}

export interface ContactDto {
    contactId?: string;
    cellphoneNumber: string;
    alternativeCellphoneNumber?: string;
    emailAddress: string;
}

export function createContact(payload: ContactDto): Promise<ContactDto> {
    return request<ContactDto>("/contact", {
        method: "POST",
        body: JSON.stringify(payload),
    });
}

export function updateContact(id: string, payload: ContactDto): Promise<ContactDto> {
    return request<ContactDto>(`/contact/${id}`, {
        method: "PUT",
        body: JSON.stringify(payload),
    });
}

export function getContact(id: string): Promise<ContactDto> {
    return request<ContactDto>(`/contact/${id}`);
}

export interface UserRef {
    userId: string;
}

export interface NextOfKinContactDto {
    nextOfKinContactId?: string;
    firstName: string;
    lastName: string;
    relationship: string;
    cellphoneNumber: string;
    user?: UserRef;
}

export interface SaveNextOfKinOptions {
    userId?: string;
}

type NextOfKinInput = Omit<NextOfKinContactDto, "nextOfKinContactId" | "user">;

function withUserRef(payload: NextOfKinInput, options?: SaveNextOfKinOptions): NextOfKinContactDto {
    return {
        ...payload,
        ...(options?.userId != null ? { user: { userId: options.userId } } : {}),
    };
}

export function createNextOfKin(
    payload: NextOfKinInput,
    options?: SaveNextOfKinOptions
): Promise<NextOfKinContactDto> {
    return request<NextOfKinContactDto>("/nextofkincontact", {
        method: "POST",
        body: JSON.stringify(withUserRef(payload, options)),
    });
}

export function updateNextOfKin(
    id: string,
    payload: NextOfKinInput,
    options?: SaveNextOfKinOptions
): Promise<NextOfKinContactDto> {
    return request<NextOfKinContactDto>(`/nextofkincontact/${id}`, {
        method: "PUT",
        body: JSON.stringify(withUserRef(payload, options)),
    });
}

export function getNextOfKin(id: string): Promise<NextOfKinContactDto> {
    return request<NextOfKinContactDto>(`/nextofkincontact/${id}`);
}
