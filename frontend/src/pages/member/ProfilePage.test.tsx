import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";
import { afterEach, describe, expect, it, vi } from "vitest";
import ProfilePage from "./ProfilePage";

vi.mock("../auth/AuthContext", () => ({
  useAuth: () => ({ user: { userId: "u1", email: "member@fitnova.test", role: "MEMBER", roles: ["MEMBER"] }, logout: vi.fn() }),
}));

const profile = {
  userId: "u1",
  firstName: "Fit",
  lastName: "Member",
  dateOfBirth: "1990-01-02",
  email: "member@fitnova.test",
  memberSince: "2026-11-01",
  roles: ["MEMBER"],
  gender: "Female",
  streetNumber: "12",
  streetName: "Main Road",
  suburbName: "Claremont",
  city: "Cape Town",
  postalCode: "7708",
  province: "Western Cape",
  country: "South Africa",
  cellphoneNumber: "0821234567",
  alternativeCellphoneNumber: null,
  nextOfKin: { firstName: "Jane", lastName: "Member", relationship: "Parent", cellphoneNumber: "0837654321" },
};

function mockFetch() {
  const fetchMock = vi.fn(async (url: string, init?: RequestInit) => {
    const method = init?.method ?? "GET";
    if (String(url).includes("/profile/me") && method === "PUT") {
      return new Response(JSON.stringify({ ...profile, ...JSON.parse(String(init?.body)) }), { status: 200, headers: { "Content-Type": "application/json" } });
    }
    if (String(url).includes("/profile/me") && method === "DELETE") return new Response(null, { status: 204 });
    if (String(url).includes("/profile/me")) return new Response(JSON.stringify(profile), { status: 200, headers: { "Content-Type": "application/json" } });
    return new Response("[]", { status: 200, headers: { "Content-Type": "application/json" } });
  });
  vi.stubGlobal("fetch", fetchMock);
  return fetchMock;
}

describe("ProfilePage", () => {
  afterEach(() => vi.restoreAllMocks());
  const renderPage = () => render(<MemoryRouter><ProfilePage /></MemoryRouter>);

  it("shows all of the user's details", async () => {
    mockFetch();
    renderPage();
    expect(await screen.findByText("Main Road", { exact: false })).toBeInTheDocument();
    expect(screen.getByText("Cape Town")).toBeInTheDocument();
    expect(screen.getByText("0821234567")).toBeInTheDocument();
    expect(screen.getByText("Jane Member")).toBeInTheDocument();
    expect(screen.getByText("Parent")).toBeInTheDocument();
  });

  it("saves profile changes", async () => {
    const fetchMock = mockFetch();
    const user = userEvent.setup();
    renderPage();
    await user.click(await screen.findByRole("button", { name: /edit profile/i }));
    const first = screen.getByLabelText("First name", { selector: "#pf-first-name" });
    await user.clear(first);
    await user.type(first, "Fitz");
    await user.click(screen.getByRole("button", { name: /save changes/i }));
    expect(await screen.findByText(/profile has been updated/i)).toBeInTheDocument();
    const put = fetchMock.mock.calls.find(([, init]) => (init as RequestInit | undefined)?.method === "PUT");
    expect(JSON.parse(String((put![1] as RequestInit).body))).toMatchObject({ firstName: "Fitz", city: "Cape Town" });
  });

  it("deletes the profile after confirming the password", async () => {
    const fetchMock = mockFetch();
    const user = userEvent.setup();
    renderPage();
    await user.click(await screen.findByRole("button", { name: /delete my profile/i }));
    await user.type(screen.getByLabelText(/^password$/i), "Password1");
    await user.click(screen.getByRole("button", { name: /delete forever/i }));
    await vi.waitFor(() => {
      const del = fetchMock.mock.calls.find(([, init]) => (init as RequestInit | undefined)?.method === "DELETE");
      expect(JSON.parse(String((del![1] as RequestInit).body))).toEqual({ password: "Password1" });
    });
  });
});
