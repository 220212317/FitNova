import { createContext, useContext } from "react";

export type ToastKind = "success" | "error" | "info";

export interface ToastContextValue {
    toast: (message: string, kind?: ToastKind) => void;
}

export const ToastContext = createContext<ToastContextValue | undefined>(undefined);

export function useToast() {
    const ctx = useContext(ToastContext);
    if (!ctx) throw new Error("useToast must be used within ToastProvider");
    return ctx;
}