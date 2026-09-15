import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { bookingsApi, slotsApi, usersApi } from "../api/fitnova";
import type { AvailabilitySlot, Booking } from "../types";
import "./HomePage.css";

export function HomePage() {
  const [counts, setCounts] = useState({
    people: 0,
    slots: 0,
    available: 0,
    bookings: 0,
  });
  const [nextSlot, setNextSlot] = useState<AvailabilitySlot | null>(null);
  const [recentBooking, setRecentBooking] = useState<Booking | null>(null);
  const [loading, setLoading] = useState(true);
  const [apiError, setApiError] = useState(false);

  useEffect(() => {
    let cancelled = false;
    (async () => {
      try {
        const [users, slots, bookings] = await Promise.all([
          usersApi.getAll().catch(() => []),
          slotsApi.getAll().catch(() => []),
          bookingsApi.getAll().catch(() => []),
        ]);
        if (cancelled) return;

        const availableSlots = slots
          .filter((s) => s.status === "AVAILABLE")
          .sort((a, b) =>
            `${a.date}${a.startTime}`.localeCompare(`${b.date}${b.startTime}`)
          );

        setCounts({
          people: users.length,
          slots: slots.length,
          available: availableSlots.length,
          bookings: bookings.length,
        });
        setNextSlot(availableSlots[0] ?? null);

        const sortedBookings = [...bookings].sort((a, b) =>
          (b.bookingDateTime ?? "").localeCompare(a.bookingDateTime ?? "")
        );
        setRecentBooking(sortedBookings[0] ?? null);
        setApiError(false);
      } catch {
        if (!cancelled) setApiError(true);
      } finally {
        if (!cancelled) setLoading(false);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, []);

  const monthLabel = (dateStr?: string) => {
    if (!dateStr) return "";
    const d = new Date(`${dateStr}T00:00:00`);
    if (Number.isNaN(d.getTime())) return dateStr.slice(5, 7);
    return d.toLocaleString(undefined, { month: "short" }).toUpperCase();
  };

  const dayLabel = (dateStr?: string) => {
    if (!dateStr) return "—";
    return dateStr.slice(8, 10) || "—";
  };

  const formatTime = (t?: string) => {
    if (!t) return "";
    return t.length >= 5 ? t.slice(0, 5) : t;
  };

  return (
    <div className="fitnova-home fitnova-home--embedded">
      {/* ================= HERO ================= */}
      <section className="fn-hero" id="home">
        <div className="fn-hero-container">
          <div className="fn-hero-content">
            <h1>
              Your fitness.
              <br />
              <strong>Your way.</strong>
            </h1>

            <p>
              FitNova makes it simple to manage your fitness journey. Find
              available slots, make bookings, and keep your fitness activities
              organised in one place.
            </p>

            <div className="fn-hero-buttons">
              <Link to="/bookings" className="fn-primary-button">
                Book a Session
                <span>→</span>
              </Link>

              <Link to="/slots" className="fn-outline-button">
                View Available Slots
              </Link>
            </div>
          </div>

          {/* Live preview card — API data only, no mock numbers */}
          <div className="fn-hero-preview">
            <div className="fn-fitness-card">
              <div className="fn-card-top">

              </div>

              <div className="fn-card-progress">
                <div
                  className="fn-progress-circle"
                  style={
                    counts.slots > 0
                      ? {
                          background: `radial-gradient(circle, #ffffff 57%, transparent 58%), conic-gradient(#2f8f63 ${Math.round(
                            (counts.available / counts.slots) * 100
                          )}%, #dce9e2 0)`,
                        }
                      : undefined
                  }
                >
                  <strong>
                    {loading
                      ? "…"
                      : counts.slots === 0
                        ? "—"
                        : `${Math.round((counts.available / counts.slots) * 100)}%`}
                  </strong>
                  <small>Open slots</small>
                </div>

                <div className="fn-progress-info">
                  <div>
                    <span>People</span>
                    <strong>{loading ? "…" : counts.people}</strong>
                  </div>
                  <div>
                    <span>Slots</span>
                    <strong>{loading ? "…" : counts.slots}</strong>
                  </div>
                  <div>
                    <span>Bookings</span>
                    <strong>{loading ? "…" : counts.bookings}</strong>
                  </div>
                </div>
              </div>

              <div className="fn-upcoming">
                <small>NEXT AVAILABLE</small>
                {loading ? (
                  <div className="fn-session-row">
                    <div className="fn-session-date">
                      <strong>—</strong>
                      <span>…</span>
                    </div>
                    <div>
                      <strong>Loading…</strong>
                      <small>Fetching from FitNova API</small>
                    </div>
                  </div>
                ) : nextSlot ? (
                  <div className="fn-session-row">
                    <div className="fn-session-date">
                      <strong>{dayLabel(nextSlot.date)}</strong>
                      <span>{monthLabel(nextSlot.date)}</span>
                    </div>
                    <div>
                      <strong>
                        {nextSlot.trainer
                          ? `${nextSlot.trainer.firstName} ${nextSlot.trainer.lastName}`
                          : "Open slot"}
                      </strong>
                      <small>
                        {formatTime(nextSlot.startTime)}
                        {nextSlot.endTime ? `–${formatTime(nextSlot.endTime)}` : ""}
                      </small>
                    </div>
                    <span className="fn-booked">{nextSlot.status}</span>
                  </div>
                ) : recentBooking ? (
                  <div className="fn-session-row">
                    <div className="fn-session-date">
                      <strong>
                        {recentBooking.bookingDateTime
                          ? recentBooking.bookingDateTime.slice(8, 10)
                          : "—"}
                      </strong>
                      <span>BK</span>
                    </div>
                    <div>
                      <strong>
                        {recentBooking.member
                          ? `${recentBooking.member.firstName} ${recentBooking.member.lastName}`
                          : "Recent booking"}
                      </strong>
                      <small>
                        {recentBooking.bookingDateTime
                          ?.replace("T", " ")
                          .slice(0, 16) ?? "—"}
                      </small>
                    </div>
                    <span className="fn-booked">{recentBooking.status}</span>
                  </div>
                ) : (
                  <div className="fn-session-row">
                    <div className="fn-session-date">
                      <strong>—</strong>
                      <span>N/A</span>
                    </div>
                    <div>
                      <strong>
                        {apiError ? "API unreachable" : "No slots yet"}
                      </strong>
                      <small>
                        {apiError
                          ? "Start the backend on port 8080"
                          : "Publish availability to see it here"}
                      </small>
                    </div>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* ================= FEATURES ================= */}
      <section className="fn-features">
        <div className="fn-section-container">
          <div className="fn-center-heading">
            <span className="fn-section-label">FEATURES</span>
            <h2>
              Everything you need to
              <br />
              <strong>run a fitness facility</strong>
            </h2>
            <p>
              Members, trainers, availability, and conflict-free bookings — in
              one place.
            </p>
          </div>

          <div className="fn-feature-grid">
            <div className="fn-feature-card">
              <div className="fn-feature-icon">👤</div>
              <h3>People</h3>
              <p>
                Register members and trainers with full profiles, roles, and
                contact details.
              </p>
              <Link to="/people">Manage people →</Link>
            </div>

            <div className="fn-feature-card">
              <div className="fn-feature-icon">📅</div>
              <h3>Availability</h3>
              <p>
                Trainers publish discrete time slots that members can book
                against.
              </p>
              <Link to="/slots">View slots →</Link>
            </div>

            <div className="fn-feature-card">
              <div className="fn-feature-icon">✓</div>
              <h3>Bookings</h3>
              <p>
                Create, cancel, and track sessions with status and overlap
                protection.
              </p>
              <Link to="/bookings">View bookings →</Link>
            </div>

            <div className="fn-feature-card">
              <div className="fn-feature-icon">☰</div>
              <h3>Lookups</h3>
              <p>
                Maintain gender, race, and role reference data used across
                profiles.
              </p>
              <Link to="/lookups">Open lookups →</Link>
            </div>
          </div>
        </div>
      </section>


      {/* ================= CTA ================= */}
      <section className="fn-final-cta">
        <div>
          <span className="fn-section-label">GET STARTED</span>
          <h2>
            Ready to start
            <br />
            your <strong>fitness journey?</strong>
          </h2>
          <p>Manage your fitness sessions with FitNova.</p>
          <Link to="/bookings" className="fn-primary-button fn-large-button">
            Book a Session
            <span>→</span>
          </Link>
        </div>
      </section>
    </div>
  );
}

export default HomePage;
