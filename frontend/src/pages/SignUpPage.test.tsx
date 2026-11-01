import { fireEvent, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import { afterEach, describe, expect, it, vi } from "vitest";
import SignUpPage from "./SignUpPage";

describe("SignUpPage", () => {
  afterEach(() => vi.restoreAllMocks());

  const renderPage = () => render(
    <MemoryRouter initialEntries={["/signup"]}>
      <Routes>
        <Route path="/signup" element={<SignUpPage />} />
        <Route path="/login" element={<div>Login destination</div>} />
      </Routes>
    </MemoryRouter>
  );

  it("validates credentials before moving to profile details", async () => {
    const user = userEvent.setup();
    renderPage();
    await user.type(screen.getByLabelText(/email address/i), "member@fitnova.test");
    await user.type(screen.getByLabelText(/^password$/i), "short");
    await user.type(screen.getByLabelText(/confirm password/i), "short");
    await user.click(screen.getByRole("button", { name: /continue/i }));
    expect(screen.getByRole("alert")).toHaveTextContent(/at least 8 characters/i);
  });

  it("submits both registration steps and redirects to login", async () => {
    const fetchMock = vi.fn().mockResolvedValue(new Response(null, { status: 204 }));
    vi.stubGlobal("fetch", fetchMock);
    const user = userEvent.setup();
    renderPage();

    await user.type(screen.getByLabelText(/email address/i), "member@fitnova.test");
    await user.type(screen.getByLabelText(/^password$/i), "Password1");
    await user.type(screen.getByLabelText(/confirm password/i), "Password1");
    await user.click(screen.getByRole("button", { name: /continue/i }));
    await user.type(screen.getByLabelText(/first name/i), "Fit");
    await user.type(screen.getByLabelText(/last name/i), "Member");
    fireEvent.change(screen.getByLabelText(/date of birth/i), { target: { value: "1990-01-02" } });
    await user.click(screen.getByRole("button", { name: /create account/i }));

    expect(await screen.findByText("Login destination")).toBeInTheDocument();
    const request = fetchMock.mock.calls[0][1] as RequestInit;
    expect(JSON.parse(String(request.body))).toMatchObject({
      firstName: "Fit",
      lastName: "Member",
      email: "member@fitnova.test",
      dateOfBirth: "1990-01-02",
    });
  });
});
