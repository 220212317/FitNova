import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Users, UserCog, CalendarDays, CalendarCheck, ListTree, BookOpen } from "lucide-react";
import { bookingsApi, slotsApi, usersApi } from "../../api/fitnova";
import type { Booking, AvailabilitySlot, User } from "../../types";
import "./dashboards.css";

export default function AdminDashboard() {
  const [users, setUsers] = useState<User[]>([]);
  const [slots, setSlots] = useState<AvailabilitySlot[]>([]);
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let cancelled = false;
    (async () => {
      try {
        const [u, s, b] = await Promise.all([
          usersApi.getAll().catch(() => [] as User[]),
          slotsApi.getAll().catch(() => [] as AvailabilitySlot[]),
          bookingsApi.getAll().catch(() => [] as Booking[]),
        ]);
        if (cancelled) return;
        setUsers(u);
        setSlots(s);
        setBookings(b);
      } finally {
        if (!cancelled) setLoading(false);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, []);

  const roleCount = (role: string) =>
    users.filter((u) => u.userRoles?.some((r) => r.roleId === role)).length;

  const pendingBookings = bookings.filter((b) => b.status === "CONFIRMED").length;
  const openSlots = slots.filter((s) => s.status === "AVAILABLE").length;

  return (
    <div className="dash">
      <header className="dash-head">
        <div>
          <h1 className="page-title">Admin dashboard</h1>
          <p className="page-sub">Monitor the platform and manage users, lookups, and gym resources.</p>
        </div>
      </header>

      <div className="stat-row">
        <div className="stat-card">
          <span>People</span>
          <strong>{loading ? "…" : users.length}</strong>
          <small>
            {roleCount("MEMBER")} members · {roleCount("TRAINER")} trainers
          </small>
        </div>
        <div className="stat-card">
          <span>Slots</span>
          <strong>{loading ? "…" : slots.length}</strong>
          <small>{openSlots} available</small>
        </div>
        <div className="stat-card">
          <span>Bookings</span>
          <strong>{loading ? "…" : bookings.length}</strong>
          <small>{pendingBookings} confirmed</small>
        </div>
        <div className="stat-card">
          <span>Admins</span>
          <strong>{loading ? "…" : roleCount("ADMIN")}</strong>
        </div>
      </div>

      <div className="quick-links">
        <Link to="/admin/users" className="quick-card">
          <Users size={20} />
          <div>
            <strong>Users</strong>
            <span>See users, change roles, remove accounts</span>
          </div>
        </Link>
        <Link to="/admin/applications" className="quick-card">
          <UserCog size={20} />
          <div>
            <strong>Trainer applications</strong>
            <span>Vet and approve new trainers</span>
          </div>
        </Link>
        <Link to="/admin/lookups" className="quick-card">
          <ListTree size={20} />
          <div>
            <strong>Lookups</strong>
            <span>Add, rename, and remove gender and race lists</span>
          </div>
        </Link>
        <Link to="/app/resources" className="quick-card">
          <BookOpen size={20} />
          <div>
            <strong>Gym resources</strong>
            <span>Add, edit, and remove manuals and tips</span>
          </div>
        </Link>
        <Link to="/admin/slots" className="quick-card">
          <CalendarDays size={20} />
          <div>
            <strong>Slots</strong>
            <span>Availability across trainers (view only)</span>
          </div>
        </Link>
        <Link to="/trainer/bookings" className="quick-card">
          <CalendarCheck size={20} />
          <div>
            <strong>All bookings</strong>
            <span>Platform-wide booking activity</span>
          </div>
        </Link>
      </div>
    </div>
  );
}
