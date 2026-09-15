/** Phumelela Sakie (240040546) */
import { useEffect } from "react";
import "./toast.css";

interface ToastProps {
  message: string | null;
  onDismiss: () => void;
  duration?: number;
}

/** Simple one-shot toast used by pages (message + auto-dismiss). */
export function Toast({ message, onDismiss, duration = 3500 }: ToastProps) {
  useEffect(() => {
    if (!message) return;
    const t = window.setTimeout(onDismiss, duration);
    return () => window.clearTimeout(t);
  }, [message, duration, onDismiss]);

  if (!message) return null;

  return (
    <div className="fn-toast-stack" aria-live="polite">
      <div className="fn-toast fn-toast--success" role="status">
        <span>{message}</span>
        <button
          type="button"
          className="fn-toast-close"
          onClick={onDismiss}
          aria-label="Close"
        >
          ×
        </button>
      </div>
    </div>
  );
}

export default Toast;
