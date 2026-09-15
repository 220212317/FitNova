import { useState } from "react";
import "./Layout.css";
import { NavLink, Outlet } from "react-router-dom";
import { Dumbbell, LayoutDashboard, Users, CalendarClock, CalendarCheck, ListTree, Menu, X } from "lucide-react";

const NAV_ITEMS = [
    { to: "/", label: "Home", icon: LayoutDashboard, end: true },
    { to: "/people", label: "People", icon: Users },
    { to: "/slots", label: "Availability", icon: CalendarClock },
    { to: "/bookings", label: "Bookings", icon: CalendarCheck },
    { to: "/lookups", label: "Lookups", icon: ListTree },
];

export function Layout() {
    const [menuOpen, setMenuOpen] = useState(false);

    return (
        <div className="app-shell">
            <header className="navbar">
                <NavLink to="/" className="brand" onClick={() => setMenuOpen(false)}>
          <span className="brand-mark">
            <Dumbbell size={16} strokeWidth={2.5} />
          </span>
                    FitNova
                </NavLink>

                <nav className={menuOpen ? "open" : ""} aria-label="Main">
                    {NAV_ITEMS.map(({ to, label, icon: Icon, end }) => (
                        <NavLink key={to} to={to} end={end} onClick={() => setMenuOpen(false)}>
                            <Icon />
                            {label}
                        </NavLink>
                    ))}
                </nav>


                <button
                    className="nav-toggle"
                    type="button"
                    aria-label={menuOpen ? "Close menu" : "Open menu"}
                    aria-expanded={menuOpen}
                    onClick={() => setMenuOpen((o) => !o)}
                >
                    {menuOpen ? <X size={18} /> : <Menu size={18} />}
                </button>
            </header>
            <main className="main">
                <Outlet />
            </main>
        </div>
    );
}