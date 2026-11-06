// @vitest-environment jsdom
/** gymData.test.ts
 *  Author: Inga Plati (230126634)
 */
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import { act, cleanup, renderHook, waitFor } from "@testing-library/react";
import {
  derive,
  hhmm,
  initialsOf,
  invalidateGymData,
  minutes,
  pctOf,
  scopeBy,
  useGymData,
} from "./gymData";
import type { Booking, BookingStatus, User } from "../../types";

const mocks = vi.hoisted(() => ({
  users: vi.fn(),
  slots: vi.fn(),
  bookings: vi.fn(),
}));

vi.mock("../../api/fitnova", () => ({
  usersApi: { getAll: mocks.users },
  slotsApi: { getAll: mocks.slots },
  bookingsApi: { getAll: mocks.bookings },
}));

const person = (userId: string, firstName: string, lastName: string): User => ({
  userId,
  firstName,
  lastName,
});

const booking = (
    bookingId: string,
    status: BookingStatus,
    member?: User
): Booking => ({
  bookingId,
  bookingDateTime: "2026-11-06T10:00:00",
  status,
  member,
});

describe("gymData helpers", () => {
  it("pctOf returns 0 when the total is 0", () => {
    expect(pctOf(5, 0)).toBe(0);
    expect(pctOf(0, 0)).toBe(0);
    expect(pctOf(5, -3)).toBe(0);
  });

  it("pctOf rounds to a whole percentage", () => {
    expect(pctOf(1, 4)).toBe(25);
    expect(pctOf(1, 3)).toBe(33);
    expect(pctOf(4, 4)).toBe(100);
  });

  it("hhmm trims seconds", () => {
    expect(hhmm("09:30:00")).toBe("09:30");
    expect(hhmm("09:30")).toBe("09:30");
    expect(hhmm(undefined)).toBe("");
  });

  it("minutes returns the difference between times", () => {
    expect(minutes("09:00", "10:30")).toBe(90);
    expect(minutes("09:00:00", "09:45:00")).toBe(45);
  });

  it("minutes never returns a negative or invalid value", () => {
    expect(minutes("10:00", "09:00")).toBe(0);
    expect(minutes(undefined, "09:00")).toBe(0);
    expect(minutes("aa:bb", "09:00")).toBe(0);
  });

  it("initialsOf builds initials from a name", () => {
    expect(initialsOf("inga", "plati")).toBe("IP");
    expect(initialsOf("Inga", undefined)).toBe("I");
    expect(initialsOf(undefined, undefined)).toBe("");
  });

  it("scopeBy filters items by person", () => {
    const inga = person("u1", "Inga", "Plati");
    const avu = person("u2", "Avu", "Sitoyi");
    const items = [
      booking("b1", "CONFIRMED", inga),
      booking("b2", "CONFIRMED", avu),
      booking("b3", "CANCELLED", inga),
      booking("b4", "COMPLETED"),
    ];

    const mine = scopeBy(items, "u1", (b) => b.member);

    expect(mine.map((b) => b.bookingId)).toEqual(["b1", "b3"]);
  });

  it("scopeBy returns nothing when there is no current user", () => {
    const items = [booking("b1", "CONFIRMED", person("u1", "Inga", "Plati"))];

    expect(scopeBy(items, undefined, (b) => b.member)).toEqual([]);
  });

  it("derive counts bookings by status", () => {
    const counts = derive([
      booking("b1", "CONFIRMED"),
      booking("b2", "CONFIRMED"),
      booking("b3", "CANCELLED"),
      booking("b4", "COMPLETED"),
    ]);

    expect(counts).toEqual({
      total: 4,
      confirmed: 2,
      cancelled: 1,
      completed: 1,
    });
  });

  it("derive handles an empty list", () => {
    expect(derive([])).toEqual({
      total: 0,
      confirmed: 0,
      cancelled: 0,
      completed: 0,
    });
  });
});

describe("useGymData", () => {
  beforeEach(() => {
    invalidateGymData();
    mocks.users.mockReset();
    mocks.slots.mockReset();
    mocks.bookings.mockReset();
  });

  afterEach(() => {
    cleanup();
  });

  it("starts in a loading state", () => {
    mocks.users.mockResolvedValue([]);
    mocks.slots.mockResolvedValue([]);
    mocks.bookings.mockResolvedValue([]);

    const { result } = renderHook(() => useGymData());

    expect(result.current.loading).toBe(true);
  });

  it("loads users, slots and bookings from the API", async () => {
    const users = [person("u1", "Inga", "Plati")];
    const bookings = [booking("b1", "CONFIRMED", users[0])];
    mocks.users.mockResolvedValue(users);
    mocks.slots.mockResolvedValue([]);
    mocks.bookings.mockResolvedValue(bookings);

    const { result } = renderHook(() => useGymData());

    await waitFor(() => expect(result.current.loading).toBe(false));
    expect(result.current.users).toEqual(users);
    expect(result.current.bookings).toEqual(bookings);
    expect(result.current.slots).toEqual([]);
    expect(result.current.error).toBeNull();
  });

  it("surfaces an API failure instead of throwing", async () => {
    mocks.users.mockRejectedValue(new Error("Authentication is required."));
    mocks.slots.mockResolvedValue([]);
    mocks.bookings.mockResolvedValue([]);

    const { result } = renderHook(() => useGymData());

    await waitFor(() => expect(result.current.loading).toBe(false));
    expect(result.current.error).toBe("Authentication is required.");
  });

  it("treats a non-array response as empty", async () => {
    mocks.users.mockResolvedValue(null);
    mocks.slots.mockResolvedValue(undefined);
    mocks.bookings.mockResolvedValue("nope");

    const { result } = renderHook(() => useGymData());

    await waitFor(() => expect(result.current.loading).toBe(false));
    expect(result.current.users).toEqual([]);
    expect(result.current.slots).toEqual([]);
    expect(result.current.bookings).toEqual([]);
  });

  it("does not refetch when a second dashboard mounts", async () => {
    mocks.users.mockResolvedValue([]);
    mocks.slots.mockResolvedValue([]);
    mocks.bookings.mockResolvedValue([]);

    const first = renderHook(() => useGymData());
    await waitFor(() => expect(first.result.current.loading).toBe(false));
    first.unmount();

    const second = renderHook(() => useGymData());
    await waitFor(() => expect(second.result.current.loading).toBe(false));

    expect(mocks.users).toHaveBeenCalledTimes(1);
    expect(mocks.slots).toHaveBeenCalledTimes(1);
    expect(mocks.bookings).toHaveBeenCalledTimes(1);
  });

  it("refetches when refresh is called", async () => {
    mocks.users.mockResolvedValue([]);
    mocks.slots.mockResolvedValue([]);
    mocks.bookings.mockResolvedValue([]);

    const { result } = renderHook(() => useGymData());
    await waitFor(() => expect(result.current.loading).toBe(false));

    await act(async () => {
      await result.current.refresh();
    });

    expect(mocks.users).toHaveBeenCalledTimes(2);
  });
});