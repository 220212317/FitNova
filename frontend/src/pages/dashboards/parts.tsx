/** parts.tsx
 *  Author: Inga Plati (230126634)
 */
import { useEffect, useRef, useState } from "react";
import type { ReactNode } from "react";
import {
    Activity,
    AlertTriangle,
    ArrowRight,
    CalendarDays,
    CheckCircle2,
    Clock,
    Target,
    XCircle,
} from "lucide-react";
import { hhmm, initialsOf, minutes, pctOf } from "./gymData";
import type { AvailabilitySlot, Booking, BookingStatus } from "../../types";

export interface CountUpProps {
    value: number;
    duration?: number;
}

export function CountUp({ value, duration = 700 }: CountUpProps) {
    const [shown, setShown] = useState(0);
    const frame = useRef<number | null>(null);

    useEffect(() => {
        const target = Number.isFinite(value) ? value : 0;
        const start = performance.now();

        const tick = (now: number) => {
            const progress = Math.min(1, (now - start) / duration);
            const eased = 1 - Math.pow(1 - progress, 3);
            setShown(Math.round(target * eased));
            if (progress < 1) {
                frame.current = requestAnimationFrame(tick);
            }
        };

        frame.current = requestAnimationFrame(tick);
        return () => {
            if (frame.current !== null) cancelAnimationFrame(frame.current);
        };
    }, [value, duration]);

    return <>{shown}</>;
}

export interface DashTopProps {
    title: string;
    subtitle?: string;
    actions?: ReactNode;
}

export function DashTop({ title, subtitle, actions }: DashTopProps) {
    return (
        <header className="dash-top">
            <div>
                <h1 className="page-title">{title}</h1>
                {subtitle && <p className="page-sub">{subtitle}</p>}
            </div>
            {actions && <div className="dash-top-actions">{actions}</div>}
        </header>
    );
}

export interface StatCard {
    label: string;
    value: number;
    hint?: string;
    icon?: ReactNode;
}

export interface StatCardsProps {
    cards: StatCard[];
    loading?: boolean;
}

export function StatCards({ cards, loading = false }: StatCardsProps) {
    if (loading) {
        return (
            <div className="stat-cards">
                {[0, 1, 2, 3].map((i) => (
                    <div className="stat-card" key={i}>
                        <div className="skeleton skeleton-row" />
                    </div>
                ))}
            </div>
        );
    }

    if (cards.length === 0) {
        return null;
    }

    return (
        <div className="stat-cards">
            {cards.map((card) => (
                <div className="stat-card" key={card.label}>
                    <div className="stat-card-head">
                        <span className="stat-card-label">{card.label}</span>
                        {card.icon && <span className="stat-card-icon">{card.icon}</span>}
                    </div>
                    <strong className="stat-card-value">
                        <CountUp value={card.value} />
                    </strong>
                    {card.hint && <span className="stat-card-hint">{card.hint}</span>}
                </div>
            ))}
        </div>
    );
}

export interface NextHeroProps {
    slot?: AvailabilitySlot;
    heading?: string;
    emptyMessage?: string;
    loading?: boolean;
    action?: ReactNode;
}

export function NextHero({
    slot,
    heading = "Next available",
    emptyMessage = "Nothing scheduled yet.",
    loading = false,
    action,
}: NextHeroProps) {
    if (loading) {
        return (
            <div className="next-hero">
                <div className="skeleton skeleton-row" />
            </div>
        );
    }

    if (!slot) {
        return (
            <div className="next-hero next-hero-empty">
                <CalendarDays />
                <div>
                    <strong>{heading}</strong>
                    <span>{emptyMessage}</span>
                </div>
            </div>
        );
    }

    const length = minutes(slot.startTime, slot.endTime);
    const trainerName = slot.trainer
        ? `${slot.trainer.firstName} ${slot.trainer.lastName}`
        : "Unassigned";

    return (
        <div className="next-hero">
            <div className="next-hero-date">
                <CalendarDays />
                <span>{slot.date}</span>
            </div>
            <div className="next-hero-body">
                <span className="next-hero-heading">{heading}</span>
                <strong className="next-hero-time">
                    {hhmm(slot.startTime)} – {hhmm(slot.endTime)}
                </strong>
                <span className="next-hero-meta">
                    {trainerName}
                    {length > 0 && ` · ${length} min`}
                </span>
            </div>
            {action && <div className="next-hero-action">{action}</div>}
        </div>
    );
}

export interface SessionsPanelProps {
    title: string;
    bookings: Booking[];
    loading?: boolean;
    emptyMessage?: string;
    limit?: number;
}

export function SessionsPanel({
    title,
    bookings,
    loading = false,
    emptyMessage = "No sessions to show.",
    limit = 5,
}: SessionsPanelProps) {
    const shown = bookings.slice(0, limit);
    const remaining = bookings.length - shown.length;

    return (
        <section className="card panel">
            <h2 className="panel-title">{title}</h2>

            {loading ? (
                <div className="skeleton skeleton-row" />
            ) : bookings.length === 0 ? (
                <div className="empty-state">
                    <CalendarDays />
                    <strong>{emptyMessage}</strong>
                </div>
            ) : (
                <>
                    <ul className="session-list">
                        {shown.map((booking) => {
                            const slot = booking.slot;
                            const member = booking.member;
                            return (
                                <li className="session-row" key={booking.bookingId}>
                                    <span className="session-avatar">
                                        {initialsOf(member?.firstName, member?.lastName) || "—"}
                                    </span>
                                    <div className="session-detail">
                                        <strong>
                                            {slot?.date ?? "Date unknown"}
                                            {slot?.startTime && ` · ${hhmm(slot.startTime)}`}
                                        </strong>
                                        <span>
                                            {member
                                                ? `${member.firstName} ${member.lastName}`
                                                : "Unassigned"}
                                        </span>
                                    </div>
                                    <StatusBadge status={booking.status} />
                                </li>
                            );
                        })}
                    </ul>
                    {remaining > 0 && <p className="panel-more">+ {remaining} more</p>}
                </>
            )}
        </section>
    );
}

export interface WeeklyPanelProps {
    title?: string;
    slots: AvailabilitySlot[];
    loading?: boolean;
    emptyMessage?: string;
}

export function WeeklyPanel({
    title = "This week",
    slots,
    loading = false,
    emptyMessage = "No slots scheduled.",
}: WeeklyPanelProps) {
    const byDate = new Map<string, AvailabilitySlot[]>();
    for (const slot of slots) {
        const key = slot.date ?? "Unscheduled";
        const list = byDate.get(key) ?? [];
        list.push(slot);
        byDate.set(key, list);
    }
    const days = Array.from(byDate.entries()).sort((a, b) =>
        a[0].localeCompare(b[0])
    );

    return (
        <section className="card panel">
            <h2 className="panel-title">{title}</h2>

            {loading ? (
                <div className="skeleton skeleton-row" />
            ) : days.length === 0 ? (
                <div className="empty-state">
                    <Clock />
                    <strong>{emptyMessage}</strong>
                </div>
            ) : (
                <ul className="week-list">
                    {days.map(([date, daySlots]) => (
                        <li className="week-row" key={date}>
                            <span className="week-date">{date}</span>
                            <div className="week-slots">
                                {daySlots.map((slot) => (
                                    <span
                                        className={`chip chip-${slot.status.toLowerCase()}`}
                                        key={slot.slotId}
                                    >
                                        {hhmm(slot.startTime)}
                                    </span>
                                ))}
                            </div>
                        </li>
                    ))}
                </ul>
            )}
        </section>
    );
}

export interface StatusRow {
    label: string;
    value: number;
    tone?: "good" | "bad" | "neutral";
}

export interface StatusPanelProps {
    title?: string;
    rows: StatusRow[];
    loading?: boolean;
}

export function StatusPanel({
    title = "Breakdown",
    rows,
    loading = false,
}: StatusPanelProps) {
    const total = rows.reduce((sum, row) => sum + row.value, 0);

    return (
        <section className="card panel">
            <h2 className="panel-title">{title}</h2>

            {loading ? (
                <div className="skeleton skeleton-row" />
            ) : total === 0 ? (
                <div className="empty-state">
                    <Activity />
                    <strong>Nothing recorded yet.</strong>
                </div>
            ) : (
                <ul className="status-list">
                    {rows.map((row) => {
                        const pct = pctOf(row.value, total);
                        return (
                            <li className="status-row" key={row.label}>
                                <div className="status-head">
                                    <span>{row.label}</span>
                                    <strong>
                                        {row.value} <small>({pct}%)</small>
                                    </strong>
                                </div>
                                <div className="status-bar">
                                    <div
                                        className={`status-fill status-${row.tone ?? "neutral"}`}
                                        style={{ width: `${pct}%` }}
                                    />
                                </div>
                            </li>
                        );
                    })}
                </ul>
            )}
        </section>
    );
}

export interface TargetRow {
    label: string;
    current: number;
    target: number;
}

export interface TargetsPanelProps {
    title?: string;
    targets: TargetRow[];
    loading?: boolean;
    emptyMessage?: string;
}

export function TargetsPanel({
    title = "Targets",
    targets,
    loading = false,
    emptyMessage = "No targets set.",
}: TargetsPanelProps) {
    return (
        <section className="card panel">
            <h2 className="panel-title">{title}</h2>

            {loading ? (
                <div className="skeleton skeleton-row" />
            ) : targets.length === 0 ? (
                <div className="empty-state">
                    <Target />
                    <strong>{emptyMessage}</strong>
                </div>
            ) : (
                <ul className="target-list">
                    {targets.map((row) => {
                        const pct = Math.min(100, pctOf(row.current, row.target));
                        const met = row.current >= row.target && row.target > 0;
                        return (
                            <li className="target-row" key={row.label}>
                                <div className="target-head">
                                    <span>{row.label}</span>
                                    <strong>
                                        {row.current} / {row.target}
                                    </strong>
                                </div>
                                <div className="status-bar">
                                    <div
                                        className={`status-fill ${met ? "status-good" : "status-neutral"}`}
                                        style={{ width: `${pct}%` }}
                                    />
                                </div>
                            </li>
                        );
                    })}
                </ul>
            )}
        </section>
    );
}

export function StatusBadge({ status }: { status: BookingStatus }) {
    const icon =
        status === "CONFIRMED" ? (
            <CheckCircle2 size={14} />
        ) : status === "CANCELLED" ? (
            <XCircle size={14} />
        ) : (
            <Activity size={14} />
        );

    return (
        <span className={`badge badge-${status.toLowerCase()}`}>
            {icon}
            {status}
        </span>
    );
}

export function DashError({ message }: { message: string }) {
    return (
        <div className="alert alert-error">
            <AlertTriangle />
            <span>{message}</span>
        </div>
    );
}

export function PanelLink({ to, children }: { to: string; children: ReactNode }) {
    return (
        <a className="panel-link" href={to}>
            {children}
            <ArrowRight size={14} />
        </a>
    );
}
