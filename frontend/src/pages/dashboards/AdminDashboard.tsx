/** AdminDashboard.tsx
 *  Author: Inga Plati (230126634)
 */
import { CalendarDays, CalendarCheck, Users, UserCog } from "lucide-react";
import {
    DashError,
    DashTop,
    PanelLink,
    SessionsPanel,
    StatCards,
    StatusPanel,
    WeeklyPanel,
} from "./parts";
import { derive, useGymData } from "./gymData";
import type { RoleType } from "../../types";
import "./dashboards.css";

function countRole(
    users: ReturnType<typeof useGymData>["users"],
    role: RoleType
): number {
    return users.filter((user) =>
        user.userRoles?.some((userRole) => userRole.roleId === role)
    ).length;
}

export default function AdminDashboard() {
    const { users, slots, bookings, loading, error, refresh } = useGymData();

    const counts = derive(bookings);
    const members = countRole(users, "MEMBER");
    const trainers = countRole(users, "TRAINER");
    const openSlots = slots.filter((slot) => slot.status === "AVAILABLE").length;

    return (
        <div>
            <DashTop
                title="Admin dashboard"
                subtitle="System-wide activity across members, trainers, slots and bookings."
                actions={
                    <button
                        className="btn"
                        type="button"
                        onClick={() => void refresh()}
                        disabled={loading}
                    >
                        Refresh
                    </button>
                }
            />

            {error && <DashError message={error} />}

            <StatCards
                loading={loading}
                cards={[
                    {
                        label: "People",
                        value: users.length,
                        hint: `${members} members · ${trainers} trainers`,
                        icon: <Users size={16} />,
                    },
                    {
                        label: "Trainers",
                        value: trainers,
                        icon: <UserCog size={16} />,
                    },
                    {
                        label: "Slots",
                        value: slots.length,
                        hint: `${openSlots} available`,
                        icon: <CalendarDays size={16} />,
                    },
                    {
                        label: "Bookings",
                        value: counts.total,
                        hint: `${counts.confirmed} confirmed`,
                        icon: <CalendarCheck size={16} />,
                    },
                ]}
            />

            <div className="dash-grid">
                <StatusPanel
                    title="Bookings by status"
                    loading={loading}
                    rows={[
                        { label: "Confirmed", value: counts.confirmed, tone: "good" },
                        { label: "Completed", value: counts.completed, tone: "neutral" },
                        { label: "Cancelled", value: counts.cancelled, tone: "bad" },
                    ]}
                />

                <WeeklyPanel
                    title="Slot schedule"
                    slots={slots}
                    loading={loading}
                    emptyMessage="No slots have been created yet."
                />

                <SessionsPanel
                    title="Recent bookings"
                    bookings={bookings}
                    loading={loading}
                    emptyMessage="No bookings recorded yet."
                    limit={6}
                />
            </div>

            <p className="panel-more">
                <PanelLink to="/lookups">Manage reference data and roles</PanelLink>
            </p>
        </div>
    );
}