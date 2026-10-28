import { createContext, useContext, useState, type ReactNode } from "react";

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

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
    const [user] = useState<AuthUser | null>(null);

    // TODO: POST /auth/login, store the access token, keep the refresh token
    const login = async (_email: string, _password: string) => {};

    // TODO: clear tokens and user state
    const logout = () => {};

    return (
        <AuthContext.Provider value={{ user, isAuthenticated: user !== null, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error("useAuth must be used inside <AuthProvider>");
    return ctx;
}
