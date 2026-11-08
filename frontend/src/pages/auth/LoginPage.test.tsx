import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import { AuthProvider } from "../../auth/AuthContext";
import LoginPage from "./LoginPage";

describe("LoginPage", () => {
  beforeEach(() => sessionStorage.clear());
  afterEach(() => vi.restoreAllMocks());

  const renderPage = () => render(
    <AuthProvider>
      <MemoryRouter initialEntries={["/login"]}>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/dashboard" element={<div>Dashboard destination</div>} />
        </Routes>
      </MemoryRouter>
    </AuthProvider>
  );

  it("submits credentials and redirects to the dashboard", async () => {
    const fetchMock = vi.fn().mockResolvedValue(new Response(JSON.stringify({
      accessToken: "token",
      userId: "member-1",
      email: "member@fitnova.test",
      role: "MEMBER",
    }), { status: 200 }));
    vi.stubGlobal("fetch", fetchMock);
    const user = userEvent.setup();
    renderPage();

    await user.type(screen.getByLabelText(/email address/i), "member@fitnova.test");
    await user.type(screen.getByLabelText(/^password$/i), "Password1");
    await user.click(screen.getByRole("button", { name: /sign in/i }));

    expect(await screen.findByText("Dashboard destination")).toBeInTheDocument();
    expect(fetchMock).toHaveBeenCalledTimes(1);
  });

  it("shows a useful API error", async () => {
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(new Response(
      JSON.stringify({ message: "Email or password is incorrect." }),
      { status: 401 }
    )));
    const user = userEvent.setup();
    renderPage();
    await user.type(screen.getByLabelText(/email address/i), "member@fitnova.test");
    await user.type(screen.getByLabelText(/^password$/i), "WrongPass1");
    await user.click(screen.getByRole("button", { name: /sign in/i }));
    expect(await screen.findByRole("alert")).toHaveTextContent("Email or password is incorrect.");
  });
});
