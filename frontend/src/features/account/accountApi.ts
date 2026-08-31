/** Athi Sintiya (220212317) */
import { api } from '../../api/client';
import { ensureId } from '../../api/ids';
import type { Account } from '../../types';


export function getAllAccounts(): Promise<Account[]> {
    return api.get<Account[]>('/account/getAll');
}

export function getAccount(accountId: string): Promise<Account> {
    return api.get<Account>(`/account/read/${encodeURIComponent(accountId)}`);
}

export function createAccount(input: {
    accountId?: string;
    email: string;
    password: string;
    registrationDate?: string;
}): Promise<Account> {
    const payload: Account = {
        accountId: ensureId(input.accountId),
        email: input.email,
        password: input.password,
        registrationDate: input.registrationDate,
    };
    return api.post<Account>('/account/create', payload);
}

export function updateAccount(account: Account): Promise<Account> {
    return api.put<Account>('/account/update', account);
}

export function deleteAccount(accountId: string): Promise<void> {
    return api.delete(`/account/delete/${encodeURIComponent(accountId)}`);
}

export function findAccountByEmail(email: string): Promise<Account> {
    return api.get<Account>(`/account/findByEmail/${encodeURIComponent(email)}`);
}

export function findAccountsByRegistrationDate(registrationDate: string): Promise<Account[]> {
    return api.get<Account[]>(`/account/findByRegistrationDate/${registrationDate}`);
}

export const accountApi = {
    getAllAccounts,
    getAccount,
    createAccount,
    updateAccount,
    deleteAccount,
    findAccountByEmail,
    findAccountsByRegistrationDate,
};