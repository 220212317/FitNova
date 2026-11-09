import { useCallback, useMemo, useState, type ReactNode } from "react";
import { ToastContext, type ToastKind } from "./ToastContext";

interface ToastItem {
  id: number;
  message: string;
  kind: ToastKind;
}

export function ToastProvider({ children }: { children: ReactNode }) {
  const [items, setItems] = useState<ToastItem[]>([]);

  const toast = useCallback((message: string, kind: ToastKind = "info") => {
    const id = Date.now() + Math.random();
    setItems((prev) => [...prev, { id, message, kind }]);
    window.setTimeout(() => {
      setItems((prev) => prev.filter((item) => item.id !== id));
    }, 3800);
  }, []);

  const value = useMemo(() => ({ toast }), [toast]);

  return (
      <ToastContext.Provider value={value}>
        {children}
        <div className="toast-stack" aria-live="polite">
          {items.map((item) => (
              <div key={item.id} className={`toast toast-${item.kind}`}>
                {item.message}
              </div>
          ))}
        </div>
      </ToastContext.Provider>
  );
}