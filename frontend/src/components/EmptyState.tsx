import type { ReactNode } from "react";

interface EmptyStateProps {
    icon?: ReactNode;
    title: string;
    hint?: string;
    action?: ReactNode;
}

export function EmptyState({ icon, title, hint, action }: EmptyStateProps) {
    return (
        <div className="empty-state">
            {icon}
            <strong>{title}</strong>
            {hint && <span>{hint}</span>}
            {action && <div style={{ marginTop: 12 }}>{action}</div>}
        </div>
    );
}

export default EmptyState;