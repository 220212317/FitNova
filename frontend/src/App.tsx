import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { Layout } from "./components/Layout";
import { HomePage } from "./pages/HomePage";
import { UsersPage } from "./pages/UsersPage";
import { SlotsPage } from "./pages/SlotsPage";
import { BookingsPage } from "./pages/BookingsPage";
import { LookupsPage } from "./pages/LookupsPage";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route index element={<HomePage />} />
          <Route path="people" element={<UsersPage />} />
          <Route path="slots" element={<SlotsPage />} />
          <Route path="bookings" element={<BookingsPage />} />
          <Route path="lookups" element={<LookupsPage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
