
import { useState } from "react";
import { NavLink, Outlet, useLocation, useNavigate } from "react-router-dom";
import { useAuth, type UserRole } from "../auth/AuthContext";
import "./AppShell.css";

interface NavigationItem {
    label: string;
    path: string;
    icon: string;
}

const navigation: Record<UserRole, NavigationItem[]> = {
    ADMIN: [
        { label: "Dashboard", path: "/admin/dashboard", icon: "⌂" },
        { label: "Members", path: "/admin/members", icon: "♙" },
        { label: "Trainers", path: "/admin/trainers", icon: "♟" },
        { label: "Memberships", path: "/admin/memberships", icon: "▤" },
        { label: "Reports", path: "/admin/reports", icon: "▥" },
        { label: "Settings", path: "/admin/settings", icon: "⚙" },
    ],

    TRAINER: [
        { label: "Dashboard", path: "/trainer/dashboard", icon: "⌂" },
        { label: "My Classes", path: "/trainer/classes", icon: "▤" },
        { label: "Class Schedule", path: "/trainer/schedule", icon: "▦" },
        { label: "Members", path: "/trainer/members", icon: "♙" },
    ],

    MEMBER: [
        { label: "Dashboard", path: "/member/dashboard", icon: "⌂" },
        { label: "Membership", path: "/member/membership", icon: "▤" },
        { label: "Classes", path: "/member/classes", icon: "▦" },
        { label: "Bookings", path: "/member/bookings", icon: "▣" },
        { label: "Profile", path: "/member/profile", icon: "♙" },
    ],
};

export function AppShell() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();

    const [sidebarOpen, setSidebarOpen] = useState(false);
    const [userMenuOpen, setUserMenuOpen] = useState(false);

    if (!user) {
        return null;
    }

    const menuItems = navigation[user.role];

    const currentPage =
        menuItems.find(
            (item) =>
                location.pathname === item.path ||
                location.pathname.startsWith(item.path + "/")
        )?.label ?? "Dashboard";

    const roleName =
        user.role.charAt(0) + user.role.slice(1).toLowerCase();

    const handleLogout = () => {
        logout();
        navigate("/login", { replace: true });
    };

    return (
        <div className="app-shell">

            {/* Sidebar */}
            <aside className={`sidebar ${sidebarOpen ? "sidebar-open" : ""}`}>

                <div className="sidebar-brand">
                    <h2>Fit<span>Nova</span></h2>

                    <button
                        className="mobile-close"
                        onClick={() => setSidebarOpen(false)}
                        aria-label="Close navigation"
                    >
                        ×
                    </button>
                </div>

                <div className="sidebar-user">
                    <div className="user-avatar">
                        {user.email.charAt(0).toUpperCase()}
                    </div>

                    <div className="sidebar-user-info">
                        <strong>{user.email.split("@")[0]}</strong>
                        <span>{roleName}</span>
                    </div>
                </div>

                <nav className="sidebar-navigation">
                    <p className="nav-heading">MAIN MENU</p>

                    {menuItems.map((item) => (
                        <NavLink
                            key={item.path}
                            to={item.path}
                            end
                            className={({ isActive }) =>
                                `nav-link ${isActive ? "active" : ""}`
                            }
                            onClick={() => setSidebarOpen(false)}
                        >
                            <span className="nav-icon">{item.icon}</span>
                            <span>{item.label}</span>
                        </NavLink>
                    ))}
                </nav>

                <div className="sidebar-footer">
                    <button
                        className="sidebar-logout"
                        onClick={handleLogout}
                    >
                        <span className="nav-icon">↪</span>
                        Logout
                    </button>
                </div>

            </aside>

            {/* Mobile overlay */}
            {sidebarOpen && (
                <div
                    className="sidebar-overlay"
                    onClick={() => setSidebarOpen(false)}
                />
            )}

            {/* Main container */}
            <div className="main-container">

                {/* Top bar */}
                <header className="topbar">

                    <div className="topbar-left">
                        <button
                            className="menu-toggle"
                            onClick={() => setSidebarOpen(true)}
                            aria-label="Open navigation"
                        >
                            ☰
                        </button>

                        <div className="page-heading">
                            <h1>{currentPage}</h1>
                            <p>Welcome to your FitNova workspace.</p>
                        </div>
                    </div>

                    {/* User menu */}
                    <div className="topbar-right">
                        <div className="user-menu-container">

                            <button
                                className="user-menu-button"
                                onClick={() => setUserMenuOpen(!userMenuOpen)}
                                aria-expanded={userMenuOpen}
                            >
                                <div className="user-avatar top-avatar">
                                    {user.email.charAt(0).toUpperCase()}
                                </div>

                                <div className="topbar-user-info">
                                    <strong>{user.email.split("@")[0]}</strong>
                                    <span>{roleName}</span>
                                </div>

                                <span className="dropdown-arrow">
                                    {userMenuOpen ? "▲" : "▼"}
                                </span>
                            </button>

                            {userMenuOpen && (
                                <div className="user-dropdown">
                                    <div className="dropdown-user-details">
                                        <strong>{user.email}</strong>
                                        <span>{roleName}</span>
                                    </div>

                                    <button
                                        className="dropdown-logout"
                                        onClick={handleLogout}
                                    >
                                        ↪ Logout
                                    </button>
                                </div>
                            )}

                        </div>
                    </div>

                </header>

                {/* Page content */}
                <main className="page-content">
                    <Outlet />
                </main>

            </div>
        </div>
    );
}
