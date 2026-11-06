/** TrainerDashboard.tsx
 *  Author: Inga Plati (230126634)
 */
import { Link } from "react-router-dom";
import {
    Activity,
    CalendarCheck,
    CalendarDays,
    CheckCircle2,
} from "lucide-react";
import { useAuth } from "../../auth/AuthContext";
import {
    DashError,
    DashTop,
    NextHero,
    SessionsPanel,
    StatCards,
    StatusPanel,
    WeeklyPanel,
} from "./parts";
import { derive, scopeBy, useGymData } from "./gymData";
import { byBookingStart, bySlotStart, isUpcoming, isUpcomingBooking } from "./schedule";
import "./dashboards.css";

export default function TrainerDashboard() {
    const { user } = useAuth();
    const { users, slots, bookings, loading, error, refresh } = useGymData();

    const userId = user?.userId;
    const me = users.find((person) => person.userId === userId);

    const mySlots = scopeBy(slots, userId, (slot) => slot.trainer);
    const myBookings = scopeBy(bookings, userId, (booking) => booking.slot?.trainer);

    const upcomingSlots = mySlots
        .filter((slot) => isUpcoming(slot))
        .sort(bySlotStart);
    const openSlots = upcomingSlots.filter((slot) => slot.status === "AVAILABLE");
    const nextSlot = upcomingSlots.find((slot) => slot.status !== "UNAVAILABLE");

    const upcomingBookings = myBookings
        .filter((booking) => isUpcomingBooking(booking))
        .sort(byBookingStart);
    const counts = derive(myBookings);

    return (
        <div>
            <DashTop
                title="Trainer dashboard"
                subtitle={
                    me
                        ? `Welcome back, ${me.firstName}. Your schedule and bookings.`
                        : "Your schedule and bookings."
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
                        <Link className="btn" to="/slots">
                            Manage availability
                        </Link>
                    </>
                }
            />

            {error && <DashError message={error} />}

            <NextHero
                slot={nextSlot}
                heading="Your next slot"
                emptyMessage="No upcoming slots. Add availability to get booked."
                loading={loading}
            />

            <StatCards
                loading={loading}
                cards={[
                    {
                        label: "Upcoming bookings",
                        value: upcomingBookings.length,
                        icon: <CalendarCheck size={16} />,
                    },
                    {
                        label: "Open slots",
                        value: openSlots.length,
                        hint: `${upcomingSlots.length} upcoming in total`,
                        icon: <CalendarDays size={16} />,
                    },
                    {
                        label: "Completed",
                        value: counts.completed,
                        icon: <CheckCircle2 size={16} />,
                    },
                    {
                        label: "All bookings",
                        value: counts.total,
                        hint: `${counts.cancelled} cancelled`,
                        icon: <Activity size={16} />,
                    },
                ]}
            />

            <div className="dash-grid">
                <SessionsPanel
                    title="Upcoming bookings"
                    bookings={upcomingBookings}
                    loading={loading}
                    emptyMessage="No upcoming bookings."
                    limit={6}
                />

                <WeeklyPanel
                    title="My schedule"
                    slots={upcomingSlots}
                    loading={loading}
                    emptyMessage="No upcoming slots."
                />

                <StatusPanel
                    title="Bookings by status"
                    loading={loading}
                    rows={[
                        { label: "Confirmed", value: counts.confirmed, tone: "good" },
                        { label: "Completed", value: counts.completed, tone: "neutral" },
                        { label: "Cancelled", value: counts.cancelled, tone: "bad" },
                    ]}
                />
            </div>
        </div>
    );
}