// @vitest-environment jsdom
/** TrainerDashboard.test.tsx
 *  Author: Inga Plati (230126634)
 */
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import { cleanup, fireEvent, render, screen, within } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import TrainerDashboard from "./TrainerDashboard";
import {
  daysFromNow,
  expectStat,
  gym,
  makeBooking,
  makeSlot,
  makeUser,
  panel,
  statHint,
} from "./dashboardTestUtils";
vi.setConfig({ testTimeout: 30000, hookTimeout: 30000 });
const mocks = vi.hoisted(() => ({
  useAuth: vi.fn(),
  useGymData: vi.fn(),
}));

vi.mock("../../auth/AuthContext", () => ({ useAuth: mocks.useAuth }));

vi.mock("./gymData", async (importOriginal) => {
  const actual = await importOriginal<typeof import("./gymData")>();
  return { ...actual, useGymData: mocks.useGymData };
});

const tina = makeUser("t1", "Tina", "Trainer", "TRAINER");
const oscar = makeUser("t2", "Oscar", "Outsider", "TRAINER");
const mary = makeUser("u1", "Mary", "Member", "MEMBER");
const nora = makeUser("u2", "Nora", "Neighbour", "MEMBER");

const morning = makeSlot("s1", daysFromNow(3), "08:00:00", { trainer: tina });
const booked = makeSlot("s2", daysFromNow(3), "10:00:00", {
  trainer: tina,
  status: "BOOKED",
});
const earlier = makeSlot("s3", daysFromNow(-3), "09:00:00", { trainer: tina });
const theirs = makeSlot("s4", daysFromNow(3), "17:00:00", { trainer: oscar });

const data = gym({
  users: [tina, oscar, mary, nora],
  slots: [morning, booked, earlier, theirs],
  bookings: [
    makeBooking("b1", "CONFIRMED", { member: mary, slot: booked }),
    makeBooking("b2", "COMPLETED", { member: mary, slot: earlier }),
    makeBooking("b3", "CANCELLED", { member: nora, slot: booked }),
    makeBooking("b4", "CONFIRMED", { member: nora, slot: theirs }),
  ],
});

function signIn(userId: string | null) {
  mocks.useAuth.mockReturnValue({
    user:
        userId === null
            ? null
            : { userId, email: "trainer@example.com", role: "TRAINER" },
    isAuthenticated: userId !== null,
    login: vi.fn(),
    logout: vi.fn(),
  });
}

function show(value = gym()) {
  mocks.useGymData.mockReturnValue(value);
  return render(
      <MemoryRouter>
        <TrainerDashboard />
      </MemoryRouter>
  );
}

describe("TrainerDashboard", () => {
  beforeEach(() => {
    mocks.useAuth.mockReset();
    mocks.useGymData.mockReset();
    signIn("t1");
  });

  afterEach(() => {
    cleanup();
  });

  it("shows a loading state", () => {
    show(gym({ loading: true }));

    expect(document.querySelectorAll(".stat-card").length).toBe(4);
    expect(document.querySelector(".stat-card-value")).toBeNull();
    expect(panel("Upcoming bookings").querySelector(".skeleton")).not.toBeNull();
    expect(panel("My schedule").querySelector(".skeleton")).not.toBeNull();
  });

  it("shows only the trainer own slots and bookings", () => {
    show(data);

    const bookings = panel("Upcoming bookings");
    expect(bookings.querySelectorAll(".session-row").length).toBe(1);
    expect(within(bookings).getByText("Mary Member")).toBeTruthy();
    expect(within(bookings).getByText("CONFIRMED")).toBeTruthy();
    expect(screen.queryByText("Nora Neighbour")).toBeNull();

    const schedule = panel("My schedule");
    expect(within(schedule).getByText("08:00")).toBeTruthy();
    expect(within(schedule).getByText("10:00")).toBeTruthy();
    expect(within(schedule).queryByText("17:00")).toBeNull();
  });

  it("counts the trainer own bookings and slots", async () => {
    show(data);

    await expectStat("Upcoming bookings", "1");
    await expectStat("Open slots", "1");
    await expectStat("Completed", "1");
    await expectStat("All bookings", "3");
    expect(statHint("Open slots")).toBe("2 upcoming in total");
    expect(statHint("All bookings")).toBe("1 cancelled");
  });

  it("highlights the next slot", () => {
    show(data);

    expect(screen.getByText("Your next slot")).toBeTruthy();
    expect(
        screen.getByRole("heading", { name: "Bookings by status" })
    ).toBeTruthy();
  });

  it("shows an empty state with nothing scheduled", () => {
    show(gym({ users: [tina] }));

    expect(screen.getByText("No upcoming bookings.")).toBeTruthy();
    expect(screen.getByText("No upcoming slots.")).toBeTruthy();
    expect(screen.getByText("Nothing recorded yet.")).toBeTruthy();
    expect(
        screen.getByText("No upcoming slots. Add availability to get booked.")
    ).toBeTruthy();
  });

  it("shows nothing personal when nobody is signed in", () => {
    signIn(null);
    show(data);

    expect(screen.getByText("No upcoming bookings.")).toBeTruthy();
    expect(screen.getByText("No upcoming slots.")).toBeTruthy();
  });

  it("greets the trainer by name", () => {
    show(data);

    expect(
        screen.getByText("Welcome back, Tina. Your schedule and bookings.")
    ).toBeTruthy();
  });

  it("shows an error state", () => {
    show(gym({ error: "Authentication is required." }));

    expect(screen.getByText("Authentication is required.")).toBeTruthy();
  });

  it("links to availability management", () => {
    show(data);

    expect(
        screen.getByRole("link", { name: "Manage availability" }).getAttribute("href")
    ).toBe("/slots");
  });

  it("refreshes the data on request", () => {
    const refresh = vi.fn();
    show(gym({ refresh }));

    fireEvent.click(screen.getByRole("button", { name: "Refresh" }));

    expect(refresh).toHaveBeenCalledTimes(1);
  });
});