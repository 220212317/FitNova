// @vitest-environment jsdom
/** MemberDashboard.test.tsx
 *  Author: Inga Plati (230126634)
 */
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import { cleanup, fireEvent, render, screen, within } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import MemberDashboard from "./MemberDashboard";
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

const mary = makeUser("u1", "Mary", "Member", "MEMBER");
const omar = makeUser("u2", "Omar", "Other", "MEMBER");
const tina = makeUser("t1", "Tina", "Trainer", "TRAINER");
const oscar = makeUser("t2", "Oscar", "Outsider");

const nextSlot = makeSlot("s1", daysFromNow(3), "09:00:00", { trainer: tina });
const laterSlot = makeSlot("s2", daysFromNow(5), "10:00:00", { trainer: oscar });
const pastSlot = makeSlot("s3", daysFromNow(-3), "09:00:00", { trainer: tina });

const data = gym({
  users: [mary, omar, tina],
  slots: [nextSlot, laterSlot, pastSlot],
  bookings: [
    makeBooking("b1", "CONFIRMED", { member: mary, slot: nextSlot }),
    makeBooking("b2", "COMPLETED", { member: mary, slot: pastSlot }),
    makeBooking("b3", "CANCELLED", { member: mary, slot: nextSlot }),
    makeBooking("b4", "CONFIRMED", { member: omar, slot: laterSlot }),
  ],
});

function signIn(userId: string | null) {
  mocks.useAuth.mockReturnValue({
    user:
        userId === null
            ? null
            : { userId, email: "member@example.com", role: "MEMBER" },
    isAuthenticated: userId !== null,
    login: vi.fn(),
    logout: vi.fn(),
  });
}

function show(value = gym()) {
  mocks.useGymData.mockReturnValue(value);
  return render(
      <MemoryRouter>
        <MemberDashboard />
      </MemoryRouter>
  );
}

describe("MemberDashboard", () => {
  beforeEach(() => {
    mocks.useAuth.mockReset();
    mocks.useGymData.mockReset();
    signIn("u1");
  });

  afterEach(() => {
    cleanup();
  });

  it("shows a loading state", () => {
    show(gym({ loading: true }));

    expect(document.querySelectorAll(".stat-card").length).toBe(4);
    expect(document.querySelector(".stat-card-value")).toBeNull();
    expect(panel("Upcoming sessions").querySelector(".skeleton")).not.toBeNull();
    expect(panel("Trainers").querySelector(".skeleton")).not.toBeNull();
  });

  it("shows only the member own bookings with status", () => {
    show(data);

    const upcoming = panel("Upcoming sessions");
    expect(upcoming.querySelectorAll(".session-row").length).toBe(1);
    expect(within(upcoming).getByText("CONFIRMED")).toBeTruthy();
    expect(within(upcoming).getByText("Tina Trainer")).toBeTruthy();

    const past = panel("Past sessions");
    expect(past.querySelectorAll(".session-row").length).toBe(2);
    expect(within(past).getByText("COMPLETED")).toBeTruthy();
    expect(within(past).getByText("CANCELLED")).toBeTruthy();

    expect(screen.queryByText("Oscar Outsider")).toBeNull();
  });

  it("counts the member own sessions and the open slots", async () => {
    show(data);

    await expectStat("Upcoming", "1");
    await expectStat("Completed", "1");
    await expectStat("Cancelled", "1");
    await expectStat("Open slots", "2");
    expect(statHint("Upcoming")).toBe(`Next: ${nextSlot.date}`);
    expect(statHint("Open slots")).toBe("1 trainers");
  });

  it("highlights the next upcoming session", () => {
    show(data);

    expect(screen.getByText("Your next session")).toBeTruthy();
  });

  it("shows the list of trainers", () => {
    show(data);

    const trainers = panel("Trainers");
    expect(within(trainers).getByText("Tina Trainer")).toBeTruthy();
    expect(within(trainers).queryByText("Mary Member")).toBeNull();
    expect(within(trainers).queryByText("Omar Other")).toBeNull();
  });

  it("shows an empty state with no bookings", () => {
    show(gym({ users: [mary] }));

    expect(screen.getByText("You have no upcoming sessions.")).toBeTruthy();
    expect(screen.getByText("No past sessions yet.")).toBeTruthy();
    expect(screen.getByText("No open slots right now.")).toBeTruthy();
    expect(screen.getByText("No trainers available yet.")).toBeTruthy();
    expect(
        screen.getByText("No upcoming sessions. Pick an open slot from Bookings.")
    ).toBeTruthy();
  });

  it("shows nothing personal when nobody is signed in", () => {
    signIn(null);
    show(data);

    expect(screen.getByText("You have no upcoming sessions.")).toBeTruthy();
    expect(screen.getByText("No past sessions yet.")).toBeTruthy();
  });

  it("greets the member by name", () => {
    show(data);

    expect(
        screen.getByText("Welcome back, Mary. Here is what is coming up.")
    ).toBeTruthy();
  });

  it("shows an error state", () => {
    show(gym({ error: "Authentication is required." }));

    expect(screen.getByText("Authentication is required.")).toBeTruthy();
  });

  it("links to booking management", () => {
    show(data);

    expect(
        screen.getByRole("link", { name: "Manage bookings" }).getAttribute("href")
    ).toBe("/bookings");
  });

  it("refreshes the data on request", () => {
    const refresh = vi.fn();
    show(gym({ refresh }));

    fireEvent.click(screen.getByRole("button", { name: "Refresh" }));

    expect(refresh).toHaveBeenCalledTimes(1);
  });
});