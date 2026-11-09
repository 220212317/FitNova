import { render, screen } from "@testing-library/react";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import App from "./App";
import { AUTH_SESSION_KEY } from "./auth/storage";

describe("App routing", () => {
  beforeEach(() => sessionStorage.clear());
  afterEach(() => vi.restoreAllMocks());

  it("redirects unauthenticated users from /dashboard to /login", async () => {
    window.history.pushState({}, "", "/dashboard");
    render(<App />);
    expect(await screen.findByRole("heading", { name: /sign in to your account/i })).toBeInTheDocument();
  });

  it("allows an authenticated member to view the trainer directory", async () => {
    sessionStorage.setItem(AUTH_SESSION_KEY, JSON.stringify({
      accessToken: "valid-token",
      user: { userId: "member-1", email: "member@fitnova.test", role: "MEMBER" },
    }));
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(new Response(JSON.stringify([
      { userId: "trainer-1", firstName: "Tara", lastName: "Trainer" },
    ]), { status: 200 })));
    window.history.pushState({}, "", "/trainers");
    render(<App />);
    expect(await screen.findByRole("heading", { name: "Tara Trainer" })).toBeInTheDocument();
  });

  it.todo("redirects authenticated users from /login to /dashboard");
  it.todo("blocks a member from /lookups");
  it.todo("allows an admin on /lookups");
});
