/** dashboardTestUtils.ts
 *  Author: Inga Plati (230126634)
 */
import { expect, vi } from "vitest";
import { screen, waitFor } from "@testing-library/react";
import type { UseGymData } from "./gymData";
import type {
    AvailabilitySlot,
    Booking,
    BookingStatus,
    RoleType,
    SlotStatus,
    User,
} from "../../types";

export function daysFromNow(days: number): string {
    const date = new Date();
    date.setDate(date.getDate() + days);
    const pad = (value: number) => String(value).padStart(2, "0");
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}`;
}

export function makeUser(
    userId: string,
    firstName: string,
    lastName: string,
    ...roles: RoleType[]
): User {
    return {
        userId,
        firstName,
        lastName,
        userRoles: roles.map((roleId) => ({
            userRoleId: `${userId}-${roleId}`,
            roleId,
        })),
    };
}

interface SlotOptions {
    status?: SlotStatus;
    trainer?: User;
    endTime?: string;
}

export function makeSlot(
    slotId: string,
    date: string,
    startTime: string,
    options: SlotOptions = {}
): AvailabilitySlot {
    return {
        slotId,
        date,
        startTime,
        endTime: options.endTime ?? "23:00:00",
        status: options.status ?? "AVAILABLE",
        trainer: options.trainer,
    };
}

interface BookingOptions {
    member?: User;
    slot?: AvailabilitySlot;
}

export function makeBooking(
    bookingId: string,
    status: BookingStatus,
    options: BookingOptions = {}
): Booking {
    return {
        bookingId,
        bookingDateTime: "2026-11-01T08:00:00",
        status,
        member: options.member,
        slot: options.slot,
    };
}

export function gym(overrides: Partial<UseGymData> = {}): UseGymData {
    return {
        users: [],
        slots: [],
        bookings: [],
        loading: false,
        error: null,
        refresh: vi.fn().mockResolvedValue(undefined),
        ...overrides,
    };
}

export function panel(title: string): HTMLElement {
    const section = screen.getByRole("heading", { name: title }).closest("section");
    if (!section) throw new Error(`No panel titled "${title}"`);
    return section;
}

function statCard(label: string): HTMLElement {
    const labelNode = Array.from(document.querySelectorAll(".stat-card-label")).find(
        (node) => node.textContent === label
    );
    const card = labelNode?.closest<HTMLElement>(".stat-card");
    if (!card) throw new Error(`No stat card labelled "${label}"`);
    return card;
}

export async function expectStat(label: string, value: string): Promise<void> {
    await waitFor(
        () => {
            expect(statCard(label).querySelector(".stat-card-value")?.textContent).toBe(
                value
            );
        },
        { timeout: 10000 }
    );
}

export function statHint(label: string): string | null | undefined {
    return statCard(label).querySelector(".stat-card-hint")?.textContent;
}