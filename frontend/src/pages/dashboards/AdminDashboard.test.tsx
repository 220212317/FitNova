// @vitest-environment jsdom
/** AdminDashboard.test.tsx
 *  Author: Inga Plati (230126634)
 */
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import { cleanup, fireEvent, render, screen, within } from "@testing-library/react";
import AdminDashboard from "./AdminDashboard";
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
  useGymData: vi.fn(),
}));

vi.mock("./gymData", async (importOriginal) => {
  const actual = await importOriginal<typeof import("./gymData")>();
  return { ...actual, useGymData: mocks.useGymData };
});

const mary = makeUser("u1", "Mary", "Member", "MEMBER");
const nora = makeUser("u2", "Nora", "Neighbour", "MEMBER");
const tina = makeUser("t1", "Tina", "Trainer", "TRAINER");
const avu = makeUser("a1", "Avu", "Admin", "ADMIN");

const first = makeSlot("s1", daysFromNow(3), "08:00:00", { trainer: tina });
const second = makeSlot("s2", daysFromNow(4), "09:00:00", { trainer: tina });
const full = makeSlot("s3", daysFromNow(5), "10:00:00", {
  trainer: tina,
  status: "BOOKED",
});

const data = gym({
  users: [mary, nora, tina, avu],
  slots: [first, second, full],
  bookings: [
    makeBooking("b1", "CONFIRMED", { member: mary, slot: first }),
    makeBooking("b2", "CONFIRMED", { member: nora, slot: full }),
    makeBooking("b3", "CANCELLED", { member: mary, slot: second }),
  ],
});

function show(value = gym()) {
  mocks.useGymData.mockReturnValue(value);
  return render(<AdminDashboard />);
}

describe("AdminDashboard", () => {
  beforeEach(() => {
    mocks.useGymData.mockReset();
  });

  afterEach(() => {
    cleanup();
  });

  it("shows a loading state", () => {
    show(gym({ loading: true }));

    expect(document.querySelectorAll(".stat-card").length).toBe(4);
    expect(document.querySelector(".stat-card-value")).toBeNull();
    expect(panel("Recent bookings").querySelector(".skeleton")).not.toBeNull();
  });

  it("shows people, slots and bookings totals", async () => {
    show(data);

    await expectStat("People", "4");
    await expectStat("Trainers", "1");
    await expectStat("Slots", "3");
    await expectStat("Bookings", "3");
    expect(statHint("People")).toBe("2 members · 1 trainers");
    expect(statHint("Slots")).toBe("2 available");
    expect(statHint("Bookings")).toBe("2 confirmed");
  });

  it("breaks bookings down by status", () => {
    show(data);

    const status = panel("Bookings by status");
    expect(within(status).getByText("(67%)")).toBeTruthy();
    expect(within(status).getByText("(33%)")).toBeTruthy();
  });

  it("lists the slot schedule and the recent bookings", () => {
    show(data);

    expect(panel("Slot schedule").querySelectorAll(".week-row").length).toBe(3);
    expect(panel("Recent bookings").querySelectorAll(".session-row").length).toBe(3);
  });

  it("shows an empty state when nothing exists yet", () => {
    show(gym());

    expect(screen.getByText("No bookings recorded yet.")).toBeTruthy();
    expect(screen.getByText("No slots have been created yet.")).toBeTruthy();
    expect(screen.getByText("Nothing recorded yet.")).toBeTruthy();
  });

  it("shows an error state", () => {
    show(gym({ error: "Authentication is required." }));

    expect(screen.getByText("Authentication is required.")).toBeTruthy();
  });

  it("links to reference data and roles", () => {
    show(data);

    expect(
        screen
            .getByRole("link", { name: "Manage reference data and roles" })
            .getAttribute("href")
    ).toBe("/lookups");
  });

  it("refreshes the data on request", () => {
    const refresh = vi.fn();
    show(gym({ refresh }));

    fireEvent.click(screen.getByRole("button", { name: "Refresh" }));

    expect(refresh).toHaveBeenCalledTimes(1);
  });

  it("disables refresh while loading", () => {
    show(gym({ loading: true }));

    expect(screen.getByRole("button", { name: "Refresh" }).hasAttribute("disabled")).toBe(
        true
    );
  });
});