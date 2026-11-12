export function formatTime(t?: string) {
  if (!t) return "—";
  return t.length >= 5 ? t.slice(0, 5) : t;
}

export function formatDate(d?: string) {
  if (!d) return "—";
  try {
    return new Date(`${d}T00:00:00`).toLocaleDateString(undefined, {
      weekday: "short",
      month: "short",
      day: "numeric",
    });
  } catch {
    return d;
  }
}

/** Local date-time string expected by backend JsonFormat: yyyy-MM-dd'T'HH:mm:ss */
export function toBackendDateTime(d = new Date()): string {
  const pad = (n: number) => String(n).padStart(2, "0");
  return (
    `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}` +
    `T${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
  );
}

export function statusBadgeClass(status?: string) {
  const s = (status ?? "").toUpperCase();
  if (s === "AVAILABLE") return "badge badge-available";
  if (s === "BOOKED") return "badge badge-booked";
  if (s === "CONFIRMED" || s === "COMPLETED") return "badge badge-accepted";
  if (s === "IN_PROGRESS" || s === "IN-PROGRESS") return "badge badge-pending";
  if (s === "CANCELLED" || s === "UNAVAILABLE" || s === "REJECTED") return "badge badge-cancelled";
  if (s === "PENDING") return "badge badge-pending";
  return "badge";
}
