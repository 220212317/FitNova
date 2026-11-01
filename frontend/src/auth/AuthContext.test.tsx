import { act, render, screen, waitFor } from "@testing-library/react";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import { AuthProvider, useAuth } from "./AuthContext";
import { AUTH_SESSION_KEY } from "./storage";

describe("AuthContext", () => {
  beforeEach(() => sessionStorage.clear());
  afterEach(() => vi.restoreAllMocks());

  function Probe() {
    const auth = useAuth();
    return (
      <div>
        <span>{auth.isAuthenticated ? `${auth.user?.email}:${auth.user?.role}` : "signed-out"}</span>
        <button onClick={() => void auth.login("member@fitnova.test", "Password1")}>login</button>
        <button onClick={auth.logout}>logout</button>
      </div>
    );
  }

  it("is unauthenticated by default", () => {
    render(<AuthProvider><Probe /></AuthProvider>);
    expect(screen.getByText("signed-out")).toBeInTheDocument();
  });

  it("login stores the session and exposes the user role", async () => {
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(new Response(JSON.stringify({
      accessToken: "access-token",
      refreshToken: "refresh-token",
      user: { userId: "user-1", email: "member@fitnova.test", role: "ROLE_MEMBER" },
    }), { status: 200 })));

    render(<AuthProvider><Probe /></AuthProvider>);
    screen.getByRole("button", { name: "login" }).click();

    expect(await screen.findByText("member@fitnova.test:MEMBER")).toBeInTheDocument();
    expect(JSON.parse(sessionStorage.getItem(AUTH_SESSION_KEY) ?? "{}").accessToken).toBe("access-token");
  });

  it("restores and clears a stored session", async () => {
    sessionStorage.setItem(AUTH_SESSION_KEY, JSON.stringify({
      accessToken: "access-token",
      user: { userId: "admin-1", email: "admin@fitnova.test", role: "ADMIN" },
    }));
    render(<AuthProvider><Probe /></AuthProvider>);
    expect(screen.getByText("admin@fitnova.test:ADMIN")).toBeInTheDocument();

    await act(async () => window.dispatchEvent(new Event("fitnova:unauthorized")));
    await waitFor(() => expect(screen.getByText("signed-out")).toBeInTheDocument());
    expect(sessionStorage.getItem(AUTH_SESSION_KEY)).toBeNull();
  });
});
