/** MemberDashboard.tsx
 *  Author: Inga Plati (230126634)
 */
import { Link } from "react-router-dom";
import {
    CalendarCheck,
    CalendarDays,
    CheckCircle2,
    Users,
    XCircle,
} from "lucide-react";
import { useAuth } from "../../auth/AuthContext";
import {
    DashError,
    DashTop,
    NextHero,
    SessionsPanel,
    StatCards,
    WeeklyPanel,
} from "./parts";
import { derive, initialsOf, scopeBy, useGymData } from "./gymData";
import { byBookingStart, bySlotStart, isUpcoming, isUpcomingBooking } from "./schedule";
import type { User } from "../../types";
import "./dashboards.css";

function TrainersPanel({
                           trainers,
                           loading,
                       }: {
    trainers: User[];
    loading: boolean;
}) {
    return (
        <section className="card panel">
            <h2 className="panel-title">Trainers</h2>

            {loading ? (
                <div className="skeleton skeleton-row" />
            ) : trainers.length === 0 ? (
                <div className="empty-state">
                    <Users />
                    <strong>No trainers available yet.</strong>
                </div>
            ) : (
                <ul className="session-list">
                    {trainers.map((trainer) => (
                        <li className="session-row" key={trainer.userId}>
                            <span className="session-avatar">
                                {initialsOf(trainer.firstName, trainer.lastName) || "—"}
                            </span>
                            <div className="session-detail">
                                <strong>
                                    {trainer.firstName} {trainer.lastName}
                                </strong>
                            </div>
                        </li>
                    ))}
                </ul>
            )}
        </section>
    );
}

export default function MemberDashboard() {
    const { user } = useAuth();
    const { users, slots, bookings, loading, error, refresh } = useGymData();

    const userId = user?.userId;
    const me = users.find((person) => person.userId === userId);

    const mine = scopeBy(bookings, userId, (booking) => booking.member);
    const upcoming = mine
        .filter((booking) => isUpcomingBooking(booking))
        .sort(byBookingStart);
    const past = mine
        .filter((booking) => !isUpcomingBooking(booking))
        .sort((a, b) => byBookingStart(b, a));
    const counts = derive(mine);

    const openSlots = slots
        .filter((slot) => slot.status === "AVAILABLE" && isUpcoming(slot))
        .sort(bySlotStart);

    const trainers = users.filter((person) =>
        person.userRoles?.some((role) => role.roleId === "TRAINER")
    );

    const nextSession = upcoming[0]?.slot;

    return (
        <div>
            <DashTop
                title="My dashboard"
                subtitle={
                    me
                        ? `Welcome back, ${me.firstName}. Here is what is coming up.`
                        : "Your sessions and the slots open for booking."
                }
                actions={
                    <>
                        <button
                            className="btn"
                            type="button"
                            onClick={() => void refresh()}
                            disabled={loading}
                        >
                            Refresh
                        </button>
                        <Link className="btn" to="/bookings">
                            Manage bookings
                        </Link>
                    </>
                }
            />

            {error && <DashError message={error} />}

            <NextHero
                slot={nextSession}
                heading="Your next session"
                emptyMessage="No upcoming sessions. Pick an open slot from Bookings."
                loading={loading}
            />

            <StatCards
                loading={loading}
                cards={[
                    {
                        label: "Upcoming",
                        value: upcoming.length,
                        hint: nextSession ? `Next: ${nextSession.date}` : "Nothing booked",
                        icon: <CalendarCheck size={16} />,
                    },
                    {
                        label: "Completed",
                        value: counts.completed,
                        icon: <CheckCircle2 size={16} />,
                    },
                    {
                        label: "Cancelled",
                        value: counts.cancelled,
                        icon: <XCircle size={16} />,
                    },
                    {
                        label: "Open slots",
                        value: openSlots.length,
                        hint: `${trainers.length} trainers`,
                        icon: <CalendarDays size={16} />,
                    },
                ]}
            />

            <div className="dash-grid">
                <SessionsPanel
                    title="Upcoming sessions"
                    bookings={upcoming}
                    loading={loading}
                    emptyMessage="You have no upcoming sessions."
                    person="trainer"
                />

                <SessionsPanel
                    title="Past sessions"
                    bookings={past}
                    loading={loading}
                    emptyMessage="No past sessions yet."
                    person="trainer"
                />

                <WeeklyPanel
                    title="Open slots"
                    slots={openSlots}
                    loading={loading}
                    emptyMessage="No open slots right now."
                />

                <TrainersPanel trainers={trainers} loading={loading} />
            </div>
        </div>
    );
}