/** Phumelela Sakie (240040546) */
import type { ReactNode, MouseEvent, KeyboardEvent } from "react";
import "./modal.css";

interface ModalProps {
  title: string;
  subtitle?: string;
  onClose: () => void;
  children: ReactNode;
  width?: number;
  footer?: ReactNode;
}

export function Modal({
  title,
  subtitle,
  onClose,
  children,
  width = 520,
  footer,
}: ModalProps) {
  const handleBackdropClick = (e: MouseEvent<HTMLDivElement>) => {
    if (e.target === e.currentTarget) onClose();
  };

  const handleKeyDown = (e: KeyboardEvent<HTMLDivElement>) => {
    if (e.key === "Escape") onClose();
  };

  return (
    <div
      className="fn-modal-backdrop"
      onClick={handleBackdropClick}
      onKeyDown={handleKeyDown}
      role="presentation"
    >
      <div
        className="fn-modal"
        style={{ maxWidth: width }}
        role="dialog"
        aria-modal="true"
        aria-label={title}
      >
        <div className="fn-modal-header">
          <div>
            <h2>{title}</h2>
            {subtitle && <p className="fn-modal-subtitle">{subtitle}</p>}
          </div>
          <button
            type="button"
            className="fn-modal-close"
            onClick={onClose}
            aria-label="Close"
          >
            ×
          </button>
        </div>
        <div className="fn-modal-body">{children}</div>
        {footer && <div className="fn-modal-footer">{footer}</div>}
      </div>
    </div>
  );
}

interface ConfirmDialogProps {
  title: string;
  message: string;
  confirmLabel?: string;
  onConfirm: () => void;
  onCancel: () => void;
}

export function ConfirmDialog({
  title,
  message,
  confirmLabel = "Confirm",
  onConfirm,
  onCancel,
}: ConfirmDialogProps) {
  return (
    <Modal title={title} onClose={onCancel} width={420}>
      <p style={{ margin: "0 0 1.25rem", color: "var(--text)" }}>{message}</p>
      <div className="toolbar" style={{ justifyContent: "flex-end", gap: "0.5rem" }}>
        <button className="btn btn-ghost" type="button" onClick={onCancel}>
          Cancel
        </button>
        <button className="btn btn-danger" type="button" onClick={onConfirm}>
          {confirmLabel}
        </button>
      </div>
    </Modal>
  );
}

export default Modal;
