import { useEffect, useState } from "react";
import { bookingsApi } from "../../api/fitnova";
import { useAuth } from "../../auth/AuthContext";
import { useToast } from "../../components/ToastContext";
import type { Booking, User } from "../../types";
import { formatDate, formatTime, statusBadgeClass } from "../../lib/format";

function memberName(booking: Booking, all: Booking[]): string {
  let member = booking.member as User | string | undefined;

  if (typeof member === "string") {
    const id = member;
    member = all.find(
        (b) => typeof b.member === "object" && b.member?.userId === id,
    )?.member as User | undefined;
  }

  if (member && typeof member === "object") {
    const name = `${member.firstName ?? ""} ${member.lastName ?? ""}`.trim();
    if (name) return name;
  }

  return "Member";
}

export function TrainerBookingsPage() {
  const { user } = useAuth();
  const { toast } = useToast();

  const [bookings, setBookings] = useState<Booking[]>([]);
  const [loading, setLoading] = useState(true);
  const [busyBookingId, setBusyBookingId] = useState<string | null>(null);

  useEffect(() => {
    if (user?.role !== "TRAINER") {
      return;
    }

    let cancelled = false;

    const fetchBookings = async () => {
      setLoading(true);

      try {
        const trainerBookings = await bookingsApi.byTrainer();

        if (cancelled) return;

        trainerBookings.sort((a, b) => {
          const dateComparison = (a.slot?.date ?? "").localeCompare(
              b.slot?.date ?? "",
          );
          if (dateComparison !== 0) return dateComparison;

          return (a.slot?.startTime ?? "").localeCompare(
              b.slot?.startTime ?? "",
          );
        });

        setBookings(trainerBookings);
      } catch (error) {
        if (!cancelled) {
          toast(
              error instanceof Error
                  ? error.message
                  : "Could not load your bookings.",
              "error",
          );
        }
      } finally {
        if (!cancelled) {
          setLoading(false);
        }
      }
    };

    void fetchBookings();

    return () => {
      cancelled = true;
    };
  }, [user?.role, toast]);

  const startSession = async (booking: Booking) => {
    setBusyBookingId(booking.bookingId);

    try {
      await bookingsApi.startSession(booking.bookingId);
      toast("Session started.", "success");

      const trainerBookings = await bookingsApi.byTrainer();
      trainerBookings.sort((a, b) => {
        const dateComparison = (a.slot?.date ?? "").localeCompare(
            b.slot?.date ?? "",
        );
        if (dateComparison !== 0) return dateComparison;

        return (a.slot?.startTime ?? "").localeCompare(
            b.slot?.startTime ?? "",
        );
      });

      setBookings(trainerBookings);
    } catch (error) {
      toast(
          error instanceof Error ? error.message : "Could not start the session.",
          "error",
      );
    } finally {
      setBusyBookingId(null);
    }
  };

  const completeSession = async (booking: Booking) => {
    setBusyBookingId(booking.bookingId);

    try {
      await bookingsApi.completeSession(booking.bookingId);
      toast(
          "Session completed and added to the member's progress.",
          "success",
      );

      const trainerBookings = await bookingsApi.byTrainer();
      trainerBookings.sort((a, b) => {
        const dateComparison = (a.slot?.date ?? "").localeCompare(
            b.slot?.date ?? "",
        );
        if (dateComparison !== 0) return dateComparison;

        return (a.slot?.startTime ?? "").localeCompare(
            b.slot?.startTime ?? "",
        );
      });

      setBookings(trainerBookings);
    } catch (error) {
      toast(
          error instanceof Error
              ? error.message
              : "Could not complete the session.",
          "error",
      );
    } finally {
      setBusyBookingId(null);
    }
  };

  const visibleBookings = user?.role === "TRAINER" ? bookings : [];
  const isLoading = user?.role === "TRAINER" && loading;

  return (
      <div>
        <h1 className="page-title">Bookings on my slots</h1>
        <p className="page-sub">
          Start a session during its scheduled time. Complete it when the slot
          ends; completed sessions appear in the member&apos;s My Progress.
        </p>

        {isLoading ? (
            <div className="skeleton" style={{ height: 100 }} />
        ) : visibleBookings.length === 0 ? (
            <div className="card empty">No bookings on your slots yet.</div>
        ) : (
            <div className="card" style={{ padding: 0, overflow: "hidden" }}>
              <table
                  style={{
                    width: "100%",
                    borderCollapse: "collapse",
                    fontSize: "0.9rem",
                  }}
              >
                <thead>
                <tr
                    style={{
                      textAlign: "left",
                      borderBottom: "1px solid var(--fn-border)",
                    }}
                >
                  <th style={{ padding: "0.85rem 1rem" }}>Member</th>
                  <th style={{ padding: "0.85rem 1rem" }}>Date</th>
                  <th style={{ padding: "0.85rem 1rem" }}>Time</th>
                  <th style={{ padding: "0.85rem 1rem" }}>Status</th>
                  <th style={{ padding: "0.85rem 1rem" }}>Action</th>
                </tr>
                </thead>
                <tbody>
                {visibleBookings.map((booking) => (
                    <tr
                        key={booking.bookingId}
                        style={{ borderBottom: "1px solid var(--fn-border)" }}
                    >
                      <td style={{ padding: "0.85rem 1rem" }}>
                        {memberName(booking, visibleBookings)}
                      </td>
                      <td style={{ padding: "0.85rem 1rem" }}>
                        {formatDate(booking.slot?.date)}
                      </td>
                      <td style={{ padding: "0.85rem 1rem" }}>
                        {formatTime(booking.slot?.startTime)}–
                        {formatTime(booking.slot?.endTime)}
                      </td>
                      <td style={{ padding: "0.85rem 1rem" }}>
                    <span className={statusBadgeClass(booking.status)}>
                      {booking.status}
                    </span>
                      </td>
                      <td style={{ padding: "0.85rem 1rem" }}>
                        {booking.status === "CONFIRMED" ? (
                            <button
                                type="button"
                                className="btn"
                                disabled={busyBookingId === booking.bookingId}
                                onClick={() => void startSession(booking)}
                            >
                              {busyBookingId === booking.bookingId
                                  ? "Starting..."
                                  : "Start session"}
                            </button>
                        ) : booking.status === "IN_PROGRESS" ? (
                            <button
                                type="button"
                                className="btn"
                                disabled={busyBookingId === booking.bookingId}
                                onClick={() => void completeSession(booking)}
                            >
                              {busyBookingId === booking.bookingId
                                  ? "Completing..."
                                  : "End session"}
                            </button>
                        ) : (
                            <span style={{ color: "var(--fn-muted)" }}>—</span>
                        )}
                      </td>
                    </tr>
                ))}
                </tbody>
              </table>
            </div>
        )}
      </div>
  );
}