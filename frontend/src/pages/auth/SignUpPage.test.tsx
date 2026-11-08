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
    await user.selectOptions(screen.getByLabelText(/gender/i), "Female");
    await user.click(screen.getByRole("button", { name: /continue/i }));
    await user.type(screen.getByLabelText(/street number/i), "12");
    await user.type(screen.getByLabelText(/street name/i), "Main Road");
    await user.type(screen.getByLabelText(/suburb/i), "Claremont");
    await user.type(screen.getByLabelText(/postal code/i), "7708");
    expect(screen.queryByLabelText(/^city$/i)).not.toBeInTheDocument();
    await user.selectOptions(screen.getByLabelText(/province/i), "Western Cape");
    expect(screen.getByRole("option", { name: "Stellenbosch" })).toBeInTheDocument();
    expect(screen.queryByRole("option", { name: "Durban" })).not.toBeInTheDocument();
    await user.selectOptions(screen.getByLabelText(/^city$/i), "Cape Town");
    await user.click(screen.getByRole("button", { name: /continue/i }));
    await user.type(screen.getByLabelText(/^cellphone number$/i), "0821234567");
    await user.type(screen.getByLabelText(/next of kin first name/i), "Jane");
    await user.type(screen.getByLabelText(/next of kin last name/i), "Member");
    await user.selectOptions(screen.getByLabelText(/relationship/i), "Parent");
    await user.type(screen.getByLabelText(/next of kin cellphone/i), "0837654321");
    await user.click(screen.getByRole("button", { name: /create account/i }));

    expect(await screen.findByText("Login destination")).toBeInTheDocument();
    const registerCall = fetchMock.mock.calls.find(([url]) => String(url).includes("/auth/register"));
    const request = registerCall![1] as RequestInit;
    expect(JSON.parse(String(request.body))).toMatchObject({
      firstName: "Fit",
      lastName: "Member",
      email: "member@fitnova.test",
      dateOfBirth: "1990-01-02",
      province: "Western Cape",
      city: "Cape Town",
      streetNumber: "12",
      streetName: "Main Road",
      suburbName: "Claremont",
      postalCode: "7708",
      gender: "Female",
      cellphoneNumber: "0821234567",
      nextOfKin: { firstName: "Jane", lastName: "Member", relationship: "Parent", cellphoneNumber: "0837654321" },
    });
  });
});
