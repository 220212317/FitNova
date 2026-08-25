/** TODO — Collins Shibambo (230093183) */
const BASE_URL = "[http://localhost:8080/FitNova](http://localhost:8080/FitNova)";

export interface User {
    userId: string;
    firstName: string;
    lastName: string;
    dateOfBirth: string;
    account: unknown;
    demographic: unknown;
    address: unknown;
    contact: unknown;
    nextOfKinContacts: unknown[];
}

export interface UserRole {
    userRoleId: string;
    user: User;
    roleId: "MEMBER" | "TRAINER" | "ADMIN";
    description: string;
}

export class ApiError extends Error {
    status: number;

    constructor(status: number, message: string) {
        super(message);
        this.status = status;
    }
}

async function handleResponse<T>(response: Response): Promise<T> {
    if (!response.ok) {
        const text = await response.text().catch(() => "");
        throw new ApiError(response.status, text || response.statusText);
    }

    if (response.status === 204) {
        return undefined as T;
    }

    return response.json() as Promise<T>;
}

export async function getAllUsers(): Promise<User[]> {
    const response = await fetch(`${BASE_URL}/user/getAll`);
    return handleResponse<User[]>(response);
}

export async function getUser(userId: string): Promise<User> {
    const response = await fetch(`${BASE_URL}/user/read/${userId}`);
    return handleResponse<User>(response);
}

export async function createUser(user: Partial<User>): Promise<User> {
    const response = await fetch(`${BASE_URL}/user/create`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(user),
    });

    return handleResponse<User>(response);
}

export async function updateUser(user: User): Promise<User> {
    const response = await fetch(`${BASE_URL}/user/update`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(user),
    });

    return handleResponse<User>(response);
}

export async function deleteUser(userId: string): Promise<void> {
    const response = await fetch(`${BASE_URL}/user/delete/${userId}`, {
        method: "DELETE",
    });

    return handleResponse<void>(response);
}

export async function createUserRole(
    userRole: Partial<UserRole>
): Promise<UserRole> {
    const response = await fetch(`${BASE_URL}/userrole/create`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userRole),
    });

    return handleResponse<UserRole>(response);
}