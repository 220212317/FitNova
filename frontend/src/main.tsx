/** TODO — Collins Shibambo (230093183) */
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import "./pages/dashboard.css";
import "./pages/users.css";
import "./pages/admin-shared.css";
import App from "./App.tsx";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <App />
  </StrictMode>
);