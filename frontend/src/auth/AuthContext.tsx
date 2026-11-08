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
  roles: UserRole[];
}

interface AuthContextValue {
  user: AuthUser | null;
  isAuthenticated: boolean;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
}

/**
 * Backend AuthResponse:
 * { accessToken, refreshToken, tokenType, userId, email, roles: RoleType[] }
 */
interface AuthResponsePayload {
  accessToken?: string;
  refreshToken?: string;
  tokenType?: string;
  userId?: string | number;
  email?: string;
  roles?: string[];
  role?: string;
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
  const [user, setUser] = useState<AuthUser | null>(() => {
    const s = readStoredSession()?.user;
    if (!s) return null;
    return { ...s, roles: s.roles ?? [s.role] };
  });

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

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

function normaliseRole(raw: string | undefined | null): UserRole | null {
  if (!raw) return null;
  const role = String(raw).replace(/^ROLE_/, "").toUpperCase();
  if (role === "MEMBER" || role === "TRAINER" || role === "ADMIN") return role;
  return null;
}

/** Prefer ADMIN > TRAINER > MEMBER when multiple roles are present. */
function pickPrimary(roles: UserRole[]): UserRole {
  if (roles.includes("ADMIN")) return "ADMIN";
  if (roles.includes("TRAINER")) return "TRAINER";
  return roles[0] ?? "MEMBER";
}

function collectRoles(response: AuthResponsePayload): UserRole[] {
  const rawList =
    response.roles ??
    response.user?.roles ??
    (response.role ? [response.role] : null) ??
    (response.user?.role ? [response.user.role] : []);

  const out: UserRole[] = [];
  for (const item of rawList) {
    const r = normaliseRole(item);
    if (r && !out.includes(r)) out.push(r);
  }
  return out;
}

function toSession(response: AuthResponsePayload, loginEmail: string): StoredAuthSession {
  if (!response.accessToken) {
    throw new Error("The login response did not include an access token.");
  }

  const roles = collectRoles(response);
  if (roles.length === 0) {
    throw new Error("The login response did not include a valid user role.");
  }
  const role = pickPrimary(roles);

  const userId = response.userId ?? response.user?.userId ?? response.user?.id;
  if (userId === undefined || userId === null || String(userId).trim() === "") {
    throw new Error("The login response did not include a user ID.");
  }

  return {
    accessToken: response.accessToken,
    refreshToken: response.refreshToken,
    user: {
      userId: String(userId),
      email: response.email ?? response.user?.email ?? loginEmail,
      role,
      roles,
    },
  };
}

// eslint-disable-next-line react-refresh/only-export-components
export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used inside <AuthProvider>");
  return ctx;
}
