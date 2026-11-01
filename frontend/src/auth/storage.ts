export const AUTH_SESSION_KEY = "fitnova.auth.session";

export interface StoredAuthSession {
    accessToken: string;
    refreshToken?: string;
    user: {
        userId: string;
        email: string;
        role: "MEMBER" | "TRAINER" | "ADMIN";
    };
}

export function readStoredSession(): StoredAuthSession | null {
    if (typeof window === "undefined") return null;

    const raw = window.sessionStorage.getItem(AUTH_SESSION_KEY);
    if (!raw) return null;

    try {
        const session = JSON.parse(raw) as Partial<StoredAuthSession>;
        if (!session.accessToken || !session.user?.userId || !session.user.email || !session.user.role) {
            window.sessionStorage.removeItem(AUTH_SESSION_KEY);
            return null;
        }
        return session as StoredAuthSession;
    } catch {
        window.sessionStorage.removeItem(AUTH_SESSION_KEY);
        return null;
    }
}

export function storeSession(session: StoredAuthSession): void {
    window.sessionStorage.setItem(AUTH_SESSION_KEY, JSON.stringify(session));
}

export function clearStoredSession(): void {
    if (typeof window !== "undefined") {
        window.sessionStorage.removeItem(AUTH_SESSION_KEY);
    }
}
