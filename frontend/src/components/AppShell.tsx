import { Outlet } from "react-router-dom";
import "./AppShell.css";

export function AppShell() {
    // TODO: sidebar, top bar and user menu
    return (
        <div className="app-shell">
            <main>
                <Outlet />
            </main>
        </div>
    );
}
