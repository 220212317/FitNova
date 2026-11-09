import { BrowserRouter, Navigate, Route, Routes, useLocation } from "react-router-dom";
import type { ReactNode } from "react";
import { Layout } from "./components/Layout";
import { AuthProvider, useAuth } from "./auth/AuthContext";
import { HomePage } from "./pages/HomePage";
import { UsersPage } from "./pages/UsersPage";
import  SlotsPage  from "./pages/SlotsPage";
import { BookingsPage } from "./pages/BookingsPage";
import { LookupsPage } from "./pages/LookupsPage";
import LoginPage from "./pages/LoginPage";
import SignUpPage from "./pages/SignUpPage";
import DashboardRouter from "./pages/dashboards/DashboardRouter";
import TrainersPage from "./pages/TrainersPage";

function RequireAuth({ children }: { children: ReactNode }) {
  const { isAuthenticated } = useAuth();
  const location = useLocation();
  return isAuthenticated ? children : <Navigate to="/login" replace state={{ from: location.pathname }} />;
}

function GuestOnly({ children }: { children: ReactNode }) {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? <Navigate to="/dashboard" replace /> : children;
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="login" element={<GuestOnly><LoginPage /></GuestOnly>} />
          <Route path="signup" element={<GuestOnly><SignUpPage /></GuestOnly>} />
          <Route path="dashboard" element={<RequireAuth><DashboardRouter /></RequireAuth>} />
          <Route element={<Layout />}>
            <Route index element={<HomePage />} />
            <Route path="people" element={<UsersPage />} />
            <Route path="slots" element={<SlotsPage />} />
            <Route path="bookings" element={<BookingsPage />} />
            <Route path="trainers" element={<RequireAuth><TrainersPage /></RequireAuth>} />
            <Route path="lookups" element={<LookupsPage />} />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
