// @vitest-environment jsdom
/** parts.test.tsx
 *  Author: Inga Plati (230126634)
 */
import { afterEach, describe, expect, it, vi } from "vitest";
import { cleanup, render, screen, waitFor } from "@testing-library/react";
import {
    CountUp,
    DashError,
    DashTop,
    NextHero,
    PanelLink,
    SessionsPanel,
    StatCards,
    StatusBadge,
    StatusPanel,
    TargetsPanel,
    WeeklyPanel,
} from "./parts";
import { makeBooking, makeSlot, makeUser } from "./dashboardTestUtils";

vi.setConfig({ testTimeout: 30000, hookTimeout: 30000 });

afterEach(() => {
    cleanup();
});

describe("CountUp", () => {
    it("counts up to the value", async () => {
        const { container } = render(<CountUp value={7} duration={50} />);

        await waitFor(() => expect(container.textContent).toBe("7"), {
            timeout: 10000,
        });
    });

    it("shows 0 when the value is not a number", async () => {
        const { container } = render(<CountUp value={Number.NaN} duration={50} />);

        await waitFor(() => expect(container.textContent).toBe("0"), {
            timeout: 10000,
        });
    });
});

describe("DashTop", () => {
    it("renders the title, subtitle and actions", () => {
        render(
            <DashTop
                title="Admin dashboard"
                subtitle="Everything at a glance"
                actions={<button type="button">Refresh</button>}
            />
        );

        expect(screen.getByRole("heading", { name: "Admin dashboard" })).toBeTruthy();
        expect(screen.getByText("Everything at a glance")).toBeTruthy();
        expect(screen.getByRole("button", { name: "Refresh" })).toBeTruthy();
    });

    it("leaves out the subtitle and actions when they are not given", () => {
        const { container } = render(<DashTop title="Trainer dashboard" />);

        expect(container.querySelector(".page-sub")).toBeNull();
        expect(container.querySelector(".dash-top-actions")).toBeNull();
    });
});

describe("StatCards", () => {
    it("shows each card's label, value and hint", async () => {
        const { container } = render(
            <StatCards
                cards={[
                    { label: "People", value: 4, hint: "2 members" },
                    { label: "Slots", value: 0 },
                ]}
            />
        );

        expect(screen.getByText("People")).toBeTruthy();
        expect(screen.getByText("2 members")).toBeTruthy();
        expect(container.querySelectorAll(".stat-card").length).toBe(2);
        await waitFor(
            () =>
                expect(container.querySelector(".stat-card-value")?.textContent).toBe("4"),
            { timeout: 10000 }
        );
    });

    it("shows placeholders while loading", () => {
        const { container } = render(<StatCards cards={[]} loading />);

        expect(container.querySelectorAll(".stat-card").length).toBe(4);
        expect(container.querySelectorAll(".skeleton").length).toBe(4);
        expect(container.querySelector(".stat-card-value")).toBeNull();
    });

    it("renders nothing when there are no cards", () => {
        const { container } = render(<StatCards cards={[]} />);

        expect(container.firstChild).toBeNull();
    });
});

describe("NextHero", () => {
    it("shows the date, time range, trainer and length of the slot", () => {
        const trainer = makeUser("t1", "Tina", "Trainer", "TRAINER");
        const slot = makeSlot("s1", "2026-11-09", "09:00:00", {
            trainer,
            endTime: "10:00:00",
        });

        render(<NextHero slot={slot} heading="Your next session" />);

        expect(screen.getByText("Your next session")).toBeTruthy();
        expect(screen.getByText("2026-11-09")).toBeTruthy();
        expect(screen.getByText("09:00 – 10:00")).toBeTruthy();
        expect(screen.getByText("Tina Trainer · 60 min")).toBeTruthy();
    });

    it("says the trainer is unassigned when the slot has none", () => {
        const slot = makeSlot("s2", "2026-11-09", "09:00:00", { endTime: "09:30:00" });

        render(<NextHero slot={slot} />);

        expect(screen.getByText("Unassigned · 30 min")).toBeTruthy();
    });

    it("shows the empty message when there is no slot", () => {
        render(<NextHero heading="Your next slot" emptyMessage="Nothing booked." />);

        expect(screen.getByText("Your next slot")).toBeTruthy();
        expect(screen.getByText("Nothing booked.")).toBeTruthy();
    });

    it("shows a placeholder while loading", () => {
        const { container } = render(<NextHero loading />);

        expect(container.querySelector(".skeleton")).not.toBeNull();
    });
});

describe("SessionsPanel", () => {
    const tina = makeUser("t1", "Tina", "Trainer", "TRAINER");
    const mary = makeUser("u1", "Mary", "Member", "MEMBER");
    const slot = makeSlot("s1", "2026-11-09", "09:00:00", { trainer: tina });
    const bookings = [
        makeBooking("b1", "CONFIRMED", { member: mary, slot }),
        makeBooking("b2", "CANCELLED", { member: mary, slot }),
        makeBooking("b3", "COMPLETED", { member: mary, slot }),
    ];

    it("lists each booking with its status", () => {
        const { container } = render(
            <SessionsPanel title="Recent bookings" bookings={bookings} />
        );

        expect(screen.getByRole("heading", { name: "Recent bookings" })).toBeTruthy();
        expect(container.querySelectorAll(".session-row").length).toBe(3);
        expect(screen.getByText("CONFIRMED")).toBeTruthy();
        expect(screen.getByText("CANCELLED")).toBeTruthy();
        expect(screen.getByText("COMPLETED")).toBeTruthy();
    });

    it("shows the member by default", () => {
        render(<SessionsPanel title="Sessions" bookings={bookings.slice(0, 1)} />);

        expect(screen.getByText("Mary Member")).toBeTruthy();
    });

    it("shows the trainer when asked", () => {
        render(
            <SessionsPanel
                title="Sessions"
                bookings={bookings.slice(0, 1)}
                person="trainer"
            />
        );

        expect(screen.getByText("Tina Trainer")).toBeTruthy();
    });

    it("caps the rows and says how many more there are", () => {
        const { container } = render(
            <SessionsPanel title="Sessions" bookings={bookings} limit={2} />
        );

        expect(container.querySelectorAll(".session-row").length).toBe(2);
        expect(screen.getByText("+ 1 more")).toBeTruthy();
    });

    it("shows the empty message when there are no bookings", () => {
        render(
            <SessionsPanel
                title="Sessions"
                bookings={[]}
                emptyMessage="Nothing booked yet."
            />
        );

        expect(screen.getByText("Nothing booked yet.")).toBeTruthy();
    });

    it("shows a placeholder while loading", () => {
        const { container } = render(
            <SessionsPanel title="Sessions" bookings={bookings} loading />
        );

        expect(container.querySelector(".skeleton")).not.toBeNull();
        expect(container.querySelector(".session-row")).toBeNull();
    });

    it("falls back gracefully when a booking has no slot or member", () => {
        render(<SessionsPanel title="Sessions" bookings={[makeBooking("b9", "CONFIRMED")]} />);

        expect(screen.getByText("Date unknown")).toBeTruthy();
        expect(screen.getByText("Unassigned")).toBeTruthy();
    });
});

describe("WeeklyPanel", () => {
    it("groups slots under their date in date order", () => {
        const slots = [
            makeSlot("s1", "2026-11-10", "09:00:00"),
            makeSlot("s2", "2026-11-09", "15:00:00", { status: "BOOKED" }),
            makeSlot("s3", "2026-11-09", "08:00:00"),
        ];

        const { container } = render(<WeeklyPanel slots={slots} />);

        const dates = Array.from(container.querySelectorAll(".week-date")).map(
            (node) => node.textContent
        );
        expect(dates).toEqual(["2026-11-09", "2026-11-10"]);

        const booked = container.querySelectorAll(".chip-booked");
        expect(booked.length).toBe(1);
        expect(booked[0]?.textContent).toBe("15:00");
    });

    it("shows the empty message when there are no slots", () => {
        render(<WeeklyPanel slots={[]} emptyMessage="Nothing scheduled." />);

        expect(screen.getByRole("heading", { name: "This week" })).toBeTruthy();
        expect(screen.getByText("Nothing scheduled.")).toBeTruthy();
    });

    it("shows a placeholder while loading", () => {
        const { container } = render(<WeeklyPanel slots={[]} loading />);

        expect(container.querySelector(".skeleton")).not.toBeNull();
    });
});

describe("StatusPanel", () => {
    it("shows each row with its share of the total", () => {
        const { container } = render(
            <StatusPanel
                title="Bookings by status"
                rows={[
                    { label: "Confirmed", value: 3, tone: "good" },
                    { label: "Cancelled", value: 1, tone: "bad" },
                ]}
            />
        );

        expect(screen.getByText("(75%)")).toBeTruthy();
        expect(screen.getByText("(25%)")).toBeTruthy();
        expect(container.querySelector(".status-good")).not.toBeNull();
        expect(container.querySelector(".status-bad")).not.toBeNull();
    });

    it("shows an empty state when every value is zero", () => {
        render(<StatusPanel rows={[{ label: "Confirmed", value: 0 }]} />);

        expect(screen.getByText("Nothing recorded yet.")).toBeTruthy();
    });

    it("shows a placeholder while loading", () => {
        const { container } = render(<StatusPanel rows={[]} loading />);

        expect(container.querySelector(".skeleton")).not.toBeNull();
    });
});

describe("TargetsPanel", () => {
    it("shows progress against each target", () => {
        const { container } = render(
            <TargetsPanel
                targets={[
                    { label: "Sessions", current: 3, target: 5 },
                    { label: "Classes", current: 4, target: 4 },
                ]}
            />
        );

        expect(screen.getByText("3 / 5")).toBeTruthy();
        expect(screen.getByText("4 / 4")).toBeTruthy();
        expect(container.querySelectorAll(".status-good").length).toBe(1);
    });

    it("never fills the bar past 100%", () => {
        const { container } = render(
            <TargetsPanel targets={[{ label: "Sessions", current: 9, target: 5 }]} />
        );

        const fill = container.querySelector<HTMLElement>(".status-fill");
        expect(fill?.style.width).toBe("100%");
    });

    it("shows the empty message when there are no targets", () => {
        render(<TargetsPanel targets={[]} emptyMessage="No targets set yet." />);

        expect(screen.getByText("No targets set yet.")).toBeTruthy();
    });
});

describe("StatusBadge", () => {
    it("labels the status and styles it", () => {
        render(<StatusBadge status="CANCELLED" />);

        expect(screen.getByText("CANCELLED").className).toContain("badge-cancelled");
    });
});

describe("DashError", () => {
    it("shows the message", () => {
        render(<DashError message="Authentication is required." />);

        expect(screen.getByText("Authentication is required.")).toBeTruthy();
    });
});

describe("PanelLink", () => {
    it("links to the page", () => {
        render(<PanelLink to="/lookups">Manage lookups</PanelLink>);

        expect(
            screen.getByRole("link", { name: "Manage lookups" }).getAttribute("href")
        ).toBe("/lookups");
    });
});