import { useState } from "react";
import { NavLink, Outlet, useNavigate } from "react-router-dom";
import {
  Dumbbell,
  LayoutDashboard,
  CalendarPlus,
  CalendarCheck,
  CalendarDays,
  BookOpen,
  Users,
  ListTree,
  UserCog,
  ClipboardList,
  LogOut,
  Menu,
  X,
  User,
  BadgeCheck,
  Activity,
} from "lucide-react";
import { useAuth, type UserRole } from "../auth/AuthContext";
import "./AppShell.css";

type NavItem = {
  to: string;
  label: string;
  icon: typeof LayoutDashboard;
  roles: UserRole[];
  /** Optional per-role label overrides (e.g. admins see "All bookings"). */
  labels?: Partial<Record<UserRole, string>>;
};

const NAV: NavItem[] = [
  { to: "/dashboard", label: "Dashboard", icon: LayoutDashboard, roles: ["MEMBER", "TRAINER", "ADMIN"] },
  { to: "/book", label: "Book session", icon: CalendarPlus, roles: ["MEMBER"] },
  { to: "/my-bookings", label: "My bookings", icon: CalendarCheck, roles: ["MEMBER"] },
  { to: "/my-progress", label: "My Progress", icon: Activity, roles: ["MEMBER"] },
  {
    to: "/app/resources",
    label: "Resources",
    icon: BookOpen,
    roles: ["MEMBER", "TRAINER", "ADMIN"],
    labels: { ADMIN: "Gym resources" },
  },
  { to: "/apply-trainer", label: "Become a trainer", icon: BadgeCheck, roles: ["MEMBER"] },
  { to: "/trainer/slots", label: "My slots", icon: CalendarPlus, roles: ["TRAINER"] },
  { to: "/admin/slots", label: "Slots", icon: CalendarDays, roles: ["ADMIN"] },
  {
    to: "/trainer/bookings",
    label: "Session bookings",
    icon: ClipboardList,
    roles: ["TRAINER", "ADMIN"],
    labels: { ADMIN: "All bookings" },
  },
  { to: "/admin/users", label: "Users", icon: Users, roles: ["ADMIN"] },
  { to: "/admin/applications", label: "Trainer apps", icon: UserCog, roles: ["ADMIN"] },
  { to: "/admin/lookups", label: "Lookups", icon: ListTree, roles: ["ADMIN"] },
  { to: "/profile", label: "Profile", icon: User, roles: ["MEMBER", "TRAINER", "ADMIN"] },
];

export function AppShell() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);

  const role = user?.role ?? "MEMBER";
  const items = NAV.filter((n) => n.roles.includes(role));

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
      <div className="shell">
        <aside className={`sidebar ${open ? "open" : ""}`}>
          <div className="sidebar-brand">
          <span className="brand-icon">
            <Dumbbell size={18} strokeWidth={2.5} />
          </span>
            <div>
              <strong>FitNova</strong>
              <small>{role.toLowerCase()}</small>
            </div>
          </div>

          <nav className="sidebar-nav">
            {items.map(({ to, label, icon: Icon, labels }) => (
                <NavLink
                    key={to}
                    to={to}
                    onClick={() => setOpen(false)}
                    className={({ isActive }) => (isActive ? "nav-item active" : "nav-item")}
                >
                  <Icon size={18} />
                  <span>{labels?.[role] ?? label}</span>
                </NavLink>
            ))}
          </nav>

          <div className="sidebar-foot">
            <div className="user-chip">
              <div className="avatar">{(user?.email?.[0] ?? "U").toUpperCase()}</div>
              <div>
                <strong>{user?.email}</strong>
                <small>{role}</small>
              </div>
            </div>
            <button type="button" className="btn-ghost logout-btn" onClick={handleLogout}>
              <LogOut size={16} /> Sign out
            </button>
          </div>
        </aside>

        {open && <div className="sidebar-backdrop" onClick={() => setOpen(false)} />}

        <div className="shell-main">
          <header className="topbar">
            <button
                type="button"
                className="menu-btn"
                aria-label="Toggle menu"
                onClick={() => setOpen((v) => !v)}
            >
              {open ? <X size={20} /> : <Menu size={20} />}
            </button>
            <NavLink to="/" className="topbar-brand">
              FitNova
            </NavLink>
            <div className="topbar-right">
              <span className="role-pill">{role}</span>
            </div>
          </header>
          <main className="content">
            <Outlet />
          </main>
        </div>
      </div>
  );
}