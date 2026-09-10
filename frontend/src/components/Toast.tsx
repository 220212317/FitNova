
/** Phumelela Sakie (240040546) */

import React, {
    createContext,
    useState,
    useCallback,
} from "react";

interface Toast {
    id: string;
    message: string;
    type: "success" | "error" | "info" | "warning";
    duration?: number;
}

interface ToastContextType {
    toasts: Toast[];
    addToast: (
        message: string,
        type: Toast["type"],
        duration?: number
    ) => void;
    removeToast: (id: string) => void;
}

const ToastContext = createContext<ToastContextType | undefined>(
    undefined
);

export const ToastProvider: React.FC<{
    children: React.ReactNode;
}> = ({ children }) => {
    const [toasts, setToasts] = useState<Toast[]>([]);

    // Remove a toast
    const removeToast = useCallback((id: string) => {
        setToasts((prev) =>
            prev.filter((toast) => toast.id !== id)
        );
    }, []);

    // Add a toast
    const addToast = useCallback(
        (
            message: string,
            type: Toast["type"],
            duration = 3000
        ) => {
            const id = Date.now().toString();

            const newToast: Toast = {
                id,
                message,
                type,
                duration,
            };

            setToasts((prev) => [...prev, newToast]);

            if (duration > 0) {
                setTimeout(() => {
                    removeToast(id);
                }, duration);
            }
        },
        [removeToast]
    );

    return (
        <ToastContext.Provider
            value={{
                toasts,
                addToast,
                removeToast,
            }}
        >
            {children}

            <ToastContainer
                toasts={toasts}
                onRemove={removeToast}
            />
        </ToastContext.Provider>
    );
};
interface ToastContainerProps {
    toasts: Toast[];
    onRemove: (id: string) => void;
}

const ToastContainer: React.FC<ToastContainerProps> = ({
    toasts,
    onRemove,
}) => {
    return (
        <div className="fixed top-4 right-4 z-50 space-y-2">
            {toasts.map((toast) => (
                <div
                    key={toast.id}
                    className={`px-4 py-3 rounded-lg text-white shadow-lg animate-fade-in ${
    toast.type === "success"
        ? "bg-green-500"
        : toast.type === "error"
            ? "bg-red-500"
            : toast.type === "warning"
                ? "bg-yellow-500"
                : "bg-blue-500"
}`}
                >
                    <div className="flex justify-between items-center">
                        <span>{toast.message}</span>

                        <button
                            type="button"
                            onClick={() => onRemove(toast.id)}
                            className="ml-4 text-white hover:opacity-75"
                            aria-label="Close toast"
                        >
                            ×
                        </button>
                    </div>
                </div>
            ))}
        </div>
    );
};

