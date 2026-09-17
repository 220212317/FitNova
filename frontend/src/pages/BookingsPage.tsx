import { useEffect, useMemo, useState } from "react";
import type { FormEvent } from "react";
import { AlertTriangle, CalendarCheck, Pencil, Plus, RefreshCw, Trash2, XCircle } from "lucide-react";
import { bookingsApi, slotsApi, usersApi } from "../api/fitnova";
import { Modal, ConfirmDialog } from "../components/Modal";
import { Toast } from "../components/Toast";
import type { AvailabilitySlot, Booking, BookingStatus, User } from "../types";

const STATUS_OPTIONS: BookingStatus[] = ["CONFIRMED", "CANCELLED", "COMPLETED"];

interface BookingForm {
    bookingId?: string;
    memberId: string;
    slotId: string;
    status: BookingStatus;
}

/** LocalDateTime-friendly: 2026-08-24T15:30:00 (no Z, no millis) */
function nowLocalDateTime(): string {
    const d = new Date();
    const pad = (n: number) => String(n).padStart(2, "0");
    return (
        `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}` +
        `T${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
    );
}

export function BookingsPage() {
    const [bookings, setBookings] = useState<Booking[]>([]);
    const [members, setMembers] = useState<User[]>([]);
    const [slots, setSlots] = useState<AvailabilitySlot[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [message, setMessage] = useState<string | null>(null);
    const [statusFilter, setStatusFilter] = useState("");

    const [form, setForm] = useState<BookingForm | null>(null);
    const [saving, setSaving] = useState(false);
    const [deleting, setDeleting] = useState<Booking | null>(null);

    async function load() {
        setLoading(true);
        setError(null);
        try {
            const [b, u, s] = await Promise.all([
                bookingsApi.getAll(),
                usersApi.getAll(),
                slotsApi.getAll(),
            ]);
            setBookings(Array.isArray(b) ? b : []);
            setMembers(Array.isArray(u) ? u : []);
            setSlots(Array.isArray(s) ? s : []);
        } catch (e) {
            setError(e instanceof Error ? e.message : "Failed to load bookings");
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        void load();
    }, []);

    const filtered = useMemo(
        () => (statusFilter ? bookings.filter((b) => b.status === statusFilter) : bookings),
        [bookings, statusFilter]
    );

    const bookableSlots = useMemo(
        () => slots.filter((s) => s.status !== "UNAVAILABLE"),
        [slots]
    );

    function openCreate() {
        setForm({
            memberId: "",
            slotId: "",
            status: "CONFIRMED",
        });
    }

    function openEdit(b: Booking) {
        setForm({
            bookingId: b.bookingId,
            memberId: b.member?.userId ?? "",
            slotId: b.slot?.slotId ?? "",
            status: b.status,
        });
    }

    async function submit(e: FormEvent) {
        e.preventDefault();
        if (!form) return;
        if (!form.memberId.trim()) {
            setError("Select a member.");
            return;
        }
        if (!form.slotId.trim()) {
            setError("Select a slot.");
            return;
        }
        setSaving(true);
        setError(null);
        try {
            const payload: Booking = {
                bookingId: form.bookingId ?? "",
                bookingDateTime: nowLocalDateTime(),
                status: form.status,
                member: { userId: form.memberId, firstName: "", lastName: "" },
                slot: {
                    slotId: form.slotId,
                    date: "",
                    startTime: "",
                    endTime: "",
                    status: "AVAILABLE",
                },
            };
            if (form.bookingId) {
                await bookingsApi.update(payload);
                setMessage("Booking updated.");
            } else {
                await bookingsApi.create(payload);
                setMessage("Booking created.");
            }
            setForm(null);
            await load();
        } catch (e) {
            setError(e instanceof Error ? e.message : "Could not save this booking");
        } finally {
            setSaving(false);
        }
    }

    async function confirmDelete() {
        if (!deleting) return;
        setSaving(true);
        try {
            await bookingsApi.delete(deleting.bookingId);
            setMessage("Booking deleted.");
            setDeleting(null);
            await load();
        } catch (e) {
            setError(e instanceof Error ? e.message : "Delete failed");
        } finally {
            setSaving(false);
        }
    }

    async function cancel(b: Booking) {
        setSaving(true);
        setError(null);
        try {
            await bookingsApi.update({
                ...b,
                status: "CANCELLED",
                bookingDateTime: (b.bookingDateTime || nowLocalDateTime())
                    .replace(/\.\d+Z?$/, "")
                    .replace(/Z$/, ""),
                member: b.member
                    ? { userId: b.member.userId, firstName: b.member.firstName, lastName: b.member.lastName }
                    : undefined,
                slot: b.slot
                    ? {
                        slotId: b.slot.slotId,
                        date: b.slot.date,
                        startTime: b.slot.startTime,
                        endTime: b.slot.endTime,
                        status: b.slot.status,
                    }
                    : undefined,
            });
            setMessage("Booking cancelled.");
            await load();
        } catch (e) {
            setError(e instanceof Error ? e.message : "Could not cancel this booking");
        } finally {
            setSaving(false);
        }
    }

    return (
        <div>
            <div className="toolbar" style={{ justifyContent: "space-between" }}>
                <div>
                    <h1 className="page-title">Bookings</h1>
                    <p className="page-sub">Member bookings linked to trainer availability slots.</p>
                </div>
                <button className="btn" type="button" onClick={openCreate}>
                    <Plus size={16} /> New booking
                </button>
            </div>

            {error && (
                <div className="alert alert-error">
                    <AlertTriangle />
                    <span>{error}</span>
                </div>
            )}
            <Toast message={message} onDismiss={() => setMessage(null)} />

            <div className="toolbar">
                <select
                    value={statusFilter}
                    onChange={(e) => setStatusFilter(e.target.value)}
                    style={{ maxWidth: 200 }}
                >
                    <option value="">All statuses</option>
                    {STATUS_OPTIONS.map((s) => (
                        <option key={s} value={s}>
                            {s}
                        </option>
                    ))}
                </select>
                <button className="btn btn-ghost" type="button" onClick={load} disabled={loading}>
                    <RefreshCw size={15} /> Refresh
                </button>
            </div>

            <div className="card table-wrap">
                {loading ? (
                    <>
                        <div className="skeleton skeleton-row" />
                        <div className="skeleton skeleton-row" />
                        <div className="skeleton skeleton-row" />
                    </>
                ) : filtered.length === 0 ? (
                    <div className="empty-state">
                        <CalendarCheck />
                        <strong>No bookings yet</strong>
                        <span>Bookings made against an available slot will show up here.</span>
                    </div>
                ) : (
                    <table>
                        <thead>
                        <tr>
                            <th>When</th>
                            <th>Status</th>
                            <th>Member</th>
                            <th>Slot</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        {filtered.map((b) => (
                            <tr key={b.bookingId}>
                                <td className="mono">
                                    {b.bookingDateTime?.replace("T", " ").slice(0, 16)}
                                </td>
                                <td>
                                    <span className={`badge ${b.status?.toLowerCase()}`}>{b.status}</span>
                                </td>
                                <td>
                                    {b.member ? `${b.member.firstName} ${b.member.lastName}` : "—"}
                                </td>
                                <td className="mono">
                                    {b.slot
                                        ? `${b.slot.date} ${b.slot.startTime}–${b.slot.endTime}`
                                        : "—"}
                                </td>
                                <td className="row-actions">
                                    <button className="btn-icon" type="button" onClick={() => openEdit(b)}>
                                        <Pencil /> Edit
                                    </button>
                                    {b.status !== "CANCELLED" && (
                                        <button className="btn-icon" type="button" onClick={() => cancel(b)}>
                                            <XCircle /> Cancel
                                        </button>
                                    )}
                                    <button
                                        className="btn-icon danger"
                                        type="button"
                                        onClick={() => setDeleting(b)}
                                    >
                                        <Trash2 /> Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                )}
            </div>

            {form && (
                <Modal
                    title={form.bookingId ? "Edit booking" : "New booking"}
                    onClose={() => setForm(null)}
                >
                    <form onSubmit={submit}>
                        <div className="form-row">
                            <label>Member</label>
                            <select
                                value={form.memberId}
                                onChange={(e) => setForm({ ...form, memberId: e.target.value })}
                                required
                            >
                                <option value="">Select a member…</option>
                                {members.map((m) => (
                                    <option key={m.userId} value={m.userId}>
                                        {m.firstName} {m.lastName}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div className="form-row">
                            <label>Slot</label>
                            <select
                                value={form.slotId}
                                onChange={(e) => setForm({ ...form, slotId: e.target.value })}
                                required
                            >
                                <option value="">Select a slot…</option>
                                {bookableSlots.map((s) => (
                                    <option key={s.slotId} value={s.slotId}>
                                        {s.date} {s.startTime}–{s.endTime}
                                        {s.trainer ? ` · ${s.trainer.firstName} ${s.trainer.lastName}` : ""}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div className="form-row">
                            <label>Status</label>
                            <select
                                value={form.status}
                                onChange={(e) =>
                                    setForm({ ...form, status: e.target.value as BookingStatus })
                                }
                            >
                                {STATUS_OPTIONS.map((s) => (
                                    <option key={s} value={s}>
                                        {s}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div className="toolbar" style={{ justifyContent: "flex-end", marginTop: "0.5rem" }}>
                            <button className="btn btn-ghost" type="button" onClick={() => setForm(null)}>
                                Cancel
                            </button>
                            <button className="btn" type="submit" disabled={saving}>
                                {saving ? "Saving…" : form.bookingId ? "Save changes" : "Create booking"}
                            </button>
                        </div>
                    </form>
                </Modal>
            )}

            {deleting && (
                <ConfirmDialog
                    title="Delete booking"
                    message="Delete this booking? This cannot be undone."
                    confirmLabel="Delete"
                    onConfirm={confirmDelete}
                    onCancel={() => setDeleting(null)}
                />
            )}
        </div>
    );
}