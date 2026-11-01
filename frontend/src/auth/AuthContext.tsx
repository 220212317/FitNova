import {
    createContext,
    useCallback,
    useContext,
    useEffect,
    useMemo,
    useState,
    type ReactNode,
} from "react";
import { api } from "../api/client";
import {
    clearStoredSession,
    readStoredSession,
    storeSession,
    type StoredAuthSession,
} from "./storage";

export type UserRole = "MEMBER" | "TRAINER" | "ADMIN";

export interface AuthUser {
    userId: string;
    email: string;
    role: UserRole;
}

interface AuthContextValue {
    user: AuthUser | null;
    isAuthenticated: boolean;
    login: (email: string, password: string) => Promise<void>;
    logout: () => void;
}

interface AuthResponsePayload {
    accessToken?: string;
    refreshToken?: string;
    userId?: string | number;
    email?: string;
    role?: string;
    roles?: string[];
    user?: {
        userId?: string | number;
        id?: string | number;
        email?: string;
        role?: string;
        roles?: string[];
    };
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
    const [user, setUser] = useState<AuthUser | null>(() => readStoredSession()?.user ?? null);

    const login = useCallback(async (email: string, password: string) => {
        const response = await api.post<AuthResponsePayload>("/auth/login", { email, password });
        const session = toSession(response, email);
        storeSession(session);
        setUser(session.user);
    }, []);

    const logout = useCallback(() => {
        clearStoredSession();
        setUser(null);
    }, []);

    useEffect(() => {
        window.addEventListener("fitnova:unauthorized", logout);
        return () => window.removeEventListener("fitnova:unauthorized", logout);
    }, [logout]);

    const value = useMemo(
        () => ({ user, isAuthenticated: user !== null, login, logout }),
        [user, login, logout]
    );

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
}

function toSession(response: AuthResponsePayload, loginEmail: string): StoredAuthSession {
    if (!response.accessToken) {
        throw new Error("The login response did not include an access token.");
    }

    const rawRole = response.user?.role ?? response.user?.roles?.[0] ?? response.role ?? response.roles?.[0];
    const role = rawRole?.replace(/^ROLE_/, "").toUpperCase();
    if (role !== "MEMBER" && role !== "TRAINER" && role !== "ADMIN") {
        throw new Error("The login response did not include a valid user role.");
    }

    const userId = response.user?.userId ?? response.user?.id ?? response.userId;
    if (userId === undefined || userId === null || String(userId).trim() === "") {
        throw new Error("The login response did not include a user ID.");
    }

    return {
        accessToken: response.accessToken,
        refreshToken: response.refreshToken,
        user: {
            userId: String(userId),
            email: response.user?.email ?? response.email ?? loginEmail,
            role,
        },
    };
}

// This hook intentionally lives beside its provider so the context stays private.
// eslint-disable-next-line react-refresh/only-export-components
export function useAuth() {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error("useAuth must be used inside <AuthProvider>");
    return ctx;
}
