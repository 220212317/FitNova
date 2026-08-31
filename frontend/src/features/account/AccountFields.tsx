/** Athi Sintiya (220212317) */
import { useState, type FormEvent } from 'react';
import type { Account } from '../../types';
import { createAccount } from './accountApi';

interface AccountFieldsProps {
    account: Partial<Account> | null;
    onAccountChange: (account: Account) => void;
    disabled?: boolean;
}

export default function AccountFields({
                                          account,
                                          onAccountChange,
                                          disabled = false,
                                      }: AccountFieldsProps) {
    const [email, setEmail] = useState(account?.email ?? '');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [submitting, setSubmitting] = useState(false);

    async function handleCreateAccount(e: FormEvent<HTMLFormElement>) {
        e.preventDefault();

        if (!email.trim()) {
            setError('Email is required.');
            return;
        }
        if (!password) {
            setError('Password is required.');
            return;
        }
        if (password !== confirmPassword) {
            setError('Passwords do not match.');
            return;
        }

        setSubmitting(true);
        setError(null);

        try {
            const created = await createAccount({
                email: email.trim(),
                password,
            });
            onAccountChange(created);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to create account.');
        } finally {
            setSubmitting(false);
        }
    }

    // If we already have an account, just display it (read-only)
    if (account?.accountId) {
        return (
            <fieldset disabled>
                <legend>Account</legend>
                <div>
                    <label>Email</label>
                    <input type="email" value={account.email} readOnly />
                </div>
                <div>
                    <label>Account ID</label>
                    <input type="text" value={account.accountId} readOnly />
                </div>
                {account.registrationDate && (
                    <div>
                        <label>Registered</label>
                        <input type="text" value={account.registrationDate} readOnly />
                    </div>
                )}
            </fieldset>
        );
    }

    return (
        <fieldset disabled={disabled}>
            <legend>Create Account</legend>

            {error && <p role="alert" style={{ color: 'red' }}>{error}</p>}

            <form onSubmit={handleCreateAccount}>
                <div>
                    <label htmlFor="account-email">Email *</label>
                    <input
                        id="account-email"
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="user@example.com"
                        disabled={submitting}
                        required
                    />
                </div>

                <div>
                    <label htmlFor="account-password">Password *</label>
                    <input
                        id="account-password"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="••••••••"
                        disabled={submitting}
                        required
                    />
                </div>

                <div>
                    <label htmlFor="account-confirm-password">Confirm Password *</label>
                    <input
                        id="account-confirm-password"
                        type="password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        placeholder="••••••••"
                        disabled={submitting}
                        required
                    />
                </div>

                <button type="submit" disabled={submitting}>
                    {submitting ? 'Creating...' : 'Create Account'}
                </button>
            </form>
        </fieldset>
    );
}