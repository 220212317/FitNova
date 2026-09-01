/** Collins Shibambo (230093183) */
import { api } from '../../api/client';
import { ensureId } from '../../api/ids';
import type { User, UserRole, RoleType } from '../../types';

export function getAllUsers(): Promise<User[]> {
    return api.get<User[]>('/user/getAll');
}

export function getUser(userId: string): Promise<User> {
    return api.get<User>(`/user/read/${encodeURIComponent(userId)}`);
}

export function createUser(input: {
    userId?: string;
    firstName: string;
    lastName: string;
    dateOfBirth?: string;
    account?: User['account'];
    demographic?: User['demographic'];
    address?: User['address'];
    contact?: User['contact'];
    nextOfKinContacts?: User['nextOfKinContacts'];
}): Promise<User> {
    const payload: User = {
        userId: ensureId(input.userId),
        firstName: input.firstName,
        lastName: input.lastName,
        dateOfBirth: input.dateOfBirth,
        account: input.account,
        demographic: input.demographic,
        address: input.address,
        contact: input.contact,
        nextOfKinContacts: input.nextOfKinContacts,
    };
    return api.post<User>('/user/create', payload);
}

export function updateUser(user: User): Promise<User> {
    return api.put<User>('/user/update', user);
}

export function deleteUser(userId: string): Promise<void> {
    return api.delete(`/user/delete/${encodeURIComponent(userId)}`);
}

export function findUsersByName(firstName: string, lastName: string): Promise<User[]> {
    return api.get<User[]>(
        `/user/findByName/${encodeURIComponent(firstName)}/${encodeURIComponent(lastName)}`
    );
}

export function searchUsersByLastName(lastName: string): Promise<User[]> {
    return api.get<User[]>(`/user/searchByLastName/${encodeURIComponent(lastName)}`);
}

export function createUserRole(input: {
    userRoleId?: string;
    userId: string;
    roleId: RoleType;
    description?: string;
}): Promise<UserRole> {
    const payload = {
        userRoleId: ensureId(input.userRoleId),
        user: { userId: input.userId },
        roleId: input.roleId,
        description: input.description,
    };
    return api.post<UserRole>('/userrole/create', payload);
}

export function getUserRole(userRoleId: string): Promise<UserRole> {
    return api.get<UserRole>(`/userrole/read/${encodeURIComponent(userRoleId)}`);
}

export function updateUserRole(userRole: UserRole): Promise<UserRole> {
    return api.put<UserRole>('/userrole/update', userRole);
}

export function deleteUserRole(userRoleId: string): Promise<void> {
    return api.delete(`/userrole/delete/${encodeURIComponent(userRoleId)}`);
}

export function findUserRolesByUser(userId: string): Promise<UserRole[]> {
    return api.get<UserRole[]>(`/userrole/findByUser/${encodeURIComponent(userId)}`);
}

export function findUserRolesByRole(roleId: RoleType): Promise<UserRole[]> {
    return api.get<UserRole[]>(`/userrole/findByRole/${encodeURIComponent(roleId)}`);
}

export function findUserRoleByUserAndRole(
    userId: string,
    roleId: RoleType
): Promise<UserRole> {
    return api.get<UserRole>(
        `/userrole/findByUserAndRole/${encodeURIComponent(userId)}/${encodeURIComponent(roleId)}`
    );
}

export function getAllUserRoles(): Promise<UserRole[]> {
    return api.get<UserRole[]>('/userrole/getAll');
}

export const userApi = {
    getAllUsers,
    getUser,
    createUser,
    updateUser,
    deleteUser,
    findUsersByName,
    searchUsersByLastName,
    createUserRole,
    getUserRole,
    updateUserRole,
    deleteUserRole,
    findUserRolesByUser,
    findUserRolesByRole,
    findUserRoleByUserAndRole,
    getAllUserRoles,
};