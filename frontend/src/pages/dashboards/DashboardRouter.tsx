import { useAuth } from "../../auth/AuthContext";
import AdminDashboard from "./AdminDashboard";
import MemberDashboard from "./MemberDashboard";
import TrainerDashboard from "./TrainerDashboard";

export default function DashboardRouter() {
    const { user } = useAuth();

    if (user?.role === "ADMIN") return <AdminDashboard />;
    if (user?.role === "TRAINER") return <TrainerDashboard />;
    return <MemberDashboard />;
}
