import { BrowserRouter, Navigate, Route, Routes, useLocation } from "react-router-dom";
import type { ReactNode } from "react";
import { AuthProvider, useAuth, type UserRole } from "./auth/AuthContext";
import { AppShell } from "./components/AppShell";
import { ToastProvider } from "./components/Toast";

// --- Public & Auth ---
import { LandingPage } from "./pages/public/LandingPage";
import LoginPage from "./pages/auth/LoginPage";
import SignUpPage from "./pages/auth/SignUpPage";

// --- Dashboards ---
import DashboardRouter from "./pages/dashboards/DashboardRouter";

// --- Member ---
import ProfilePage from "./pages/member/ProfilePage";
import { ResourcesPage } from "./pages/member/ResourcesPage";
import ProgressPage from "./pages/member/ProgressPage";

// --- Bookings ---
import { BookSessionsPage } from "./pages/bookings/BookSessionsPage";
import { MyBookingsPage } from "./pages/bookings/MyBookingsPage";

// --- Trainer ---
import { ApplyTrainerPage } from "./pages/trainer/ApplyTrainerPage";
import { TrainerSlotsPage } from "./pages/trainer/TrainerSlotsPage";
import { TrainerBookingsPage } from "./pages/trainer/TrainerBookingsPage";

// --- Admin ---
import { AdminUsersPage } from "./pages/admin/AdminUsersPage";
import { AdminSlotsPage } from "./pages/admin/AdminSlotsPage";
import { AdminLookupsPage } from "./pages/admin/AdminLookupsPage";
import { AdminApplicationsPage } from "./pages/admin/AdminApplicationsPage";

function RequireAuth({ children }: { children: ReactNode }) {
    const { isAuthenticated } = useAuth();
    const location = useLocation();
    if (!isAuthenticated) {
        return <Navigate to="/login" replace state={{ from: location.pathname }} />;
    }
    return <>{children}</>;
}

function RequireRole({ roles, children }: { roles: UserRole[]; children: ReactNode }) {
    const { user, isAuthenticated } = useAuth();
    const location = useLocation();
    if (!isAuthenticated) {
        return <Navigate to="/login" replace state={{ from: location.pathname }} />;
    }
    if (!user || !roles.includes(user.role)) {
        return <Navigate to="/dashboard" replace />;
    }
    return <>{children}</>;
}

function GuestOnly({ children }: { children: ReactNode }) {
    const { isAuthenticated } = useAuth();
    return isAuthenticated ? <Navigate to="/dashboard" replace /> : <>{children}</>;
}

export default function App() {
    return (
        <AuthProvider>
            <ToastProvider>
                <BrowserRouter>
                    <Routes>
                        {/* ================================================= */}
                        {/* 1. PUBLIC ROUTES (Landing / Auth) */}
                        {/* ================================================= */}
                        <Route path="/" element={<LandingPage />} />
                        <Route path="/resources" element={<LandingPage section="resources" />} />
                        <Route path="/about" element={<LandingPage section="about" />} />

                        <Route path="/login" element={<GuestOnly><LoginPage /></GuestOnly>} />
                        <Route path="/signup" element={<GuestOnly><SignUpPage /></GuestOnly>} />

                        {/* ================================================= */}
                        {/* 2. AUTHENTICATED APP SHELL */}
                        {/* ================================================= */}
                        <Route element={<RequireAuth><AppShell /></RequireAuth>}>

                            {/* --- OVERVIEW (Everyone) --- */}
                            <Route path="/dashboard" element={<DashboardRouter />} />
                            <Route path="/profile" element={<ProfilePage />} />
                            <Route path="/app/resources" element={<ResourcesPage />} />

                            {/* --- MEMBER HUB --- */}
                            <Route path="/book" element={<RequireRole roles={["MEMBER"]}><BookSessionsPage /></RequireRole>} />
                            <Route path="/my-bookings" element={<RequireRole roles={["MEMBER"]}><MyBookingsPage /></RequireRole>} />
                            <Route path="/my-progress" element={<RequireRole roles={["MEMBER"]}><ProgressPage /></RequireRole>} />
                            <Route path="/apply-trainer" element={<RequireRole roles={["MEMBER"]}><ApplyTrainerPage /></RequireRole>} />

                            {/* --- TRAINER HUB --- */}
                            <Route path="/trainer/slots" element={<RequireRole roles={["TRAINER"]}><TrainerSlotsPage /></RequireRole>} />

                            {/* --- ADMIN HUB --- */}
                            <Route path="/admin/slots" element={<RequireRole roles={["ADMIN"]}><AdminSlotsPage /></RequireRole>} />
                            <Route path="/admin/users" element={<RequireRole roles={["ADMIN"]}><AdminUsersPage /></RequireRole>} />
                            <Route path="/admin/lookups" element={<RequireRole roles={["ADMIN"]}><AdminLookupsPage /></RequireRole>} />
                            <Route path="/admin/applications" element={<RequireRole roles={["ADMIN"]}><AdminApplicationsPage /></RequireRole>} />

                            {/* --- SHARED (Trainer sees own, Admin sees all) --- */}
                            <Route path="/trainer/bookings" element={<RequireRole roles={["TRAINER", "ADMIN"]}><TrainerBookingsPage /></RequireRole>} />

                        </Route>

                        {/* ================================================= */}
                        {/* 3. FALLBACK */}
                        {/* ================================================= */}
                        <Route path="*" element={<Navigate to="/" replace />} />
                    </Routes>
                </BrowserRouter>
            </ToastProvider>
        </AuthProvider>
    );
}