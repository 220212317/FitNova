import React from "react";
import "./HomePage.css";

const HomePage: React.FC = () => {
  return (
    <div className="fitnova-home">

      {/* ================= NAVIGATION ================= */}

      <header className="fn-navbar">
        <div className="fn-navbar-container">

          <a href="/" className="fn-brand">
            <span className="fn-brand-icon">F</span>
            <span>FitNova</span>
          </a>

          <nav className="fn-nav-menu">
            <a href="/">Home</a>
            <a href="/booking">Booking</a>
            <a href="/lookup">Lookup</a>
            <a href="/slot">Slots</a>
            <a href="/users">Users</a>
          </nav>

          <div className="fn-nav-actions">
            <button className="fn-login-button">
              Log In
            </button>

            <button className="fn-nav-cta">
              Get Started
            </button>
          </div>

        </div>
      </header>


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
              FitNova makes it simple to manage your fitness
              journey. Find available slots, make bookings,
              and keep your fitness activities organised
              in one place.
            </p>

            <div className="fn-hero-buttons">

              <a
                href="/booking"
                className="fn-primary-button"
              >
                Book a Session
                <span>→</span>
              </a>

              <a
                href="/lookup"
                className="fn-outline-button"
              >
                View Available Slots
              </a>

            </div>

          </div>


          {/* ================= SIMPLE FITNESS CARD ================= */}

          <div className="fn-hero-preview">

            <div className="fn-fitness-card">

              <div className="fn-card-top">
                <div>
                  <h3>Stay on track</h3>
                </div>

                <div className="fn-card-icon">
                  ✓
                </div>
              </div>


              <div className="fn-card-progress">

                <div className="fn-progress-circle">
                  <strong>75%</strong>
                  <small>Progress</small>
                </div>

                <div className="fn-progress-info">

                  <div>
                    <span>Sessions</span>
                    <strong>8</strong>
                  </div>

                  <div>
                    <span>Completed</span>
                    <strong>6</strong>
                  </div>

                  <div>
                    <span>Upcoming</span>
                    <strong>2</strong>
                  </div>

                </div>

              </div>


              <div className="fn-upcoming">

                <small>NEXT SESSION</small>

                <div className="fn-session-row">

                  <div className="fn-session-date">
                    <strong>24</strong>
                    <span>JUN</span>
                  </div>

                  <div>
                    <strong>Fitness Session</strong>
                    <small>09:00 AM</small>
                  </div>

                  <span className="fn-booked">
                    Booked
                  </span>

                </div>

              </div>

            </div>

          </div>

        </div>

      </section>


      {/* ================= QUICK FEATURES ================= */}

      <section className="fn-features">

        <div className="fn-section-container">

          <div className="fn-center-heading">

            <span className="fn-section-label">
            </span>

            <h2>
              Everything you need
              <br />
              <strong>in one place.</strong>
            </h2>

            <p>
              FitNova helps you manage your fitness activities
              quickly and easily.
            </p>

          </div>


          <div className="fn-feature-grid">


            <div className="fn-feature-card">

              <div className="fn-feature-icon">
                📅
              </div>

              <h3>
                Easy Booking
              </h3>

              <p>
                Book fitness sessions quickly and
                keep your appointments organised.
              </p>

              <a href="/booking">
                Go to Booking →
              </a>

            </div>


            <div className="fn-feature-card">

              <div className="fn-feature-icon">
                🔍
              </div>

              <h3>
                Quick Lookup
              </h3>

              <p>
                Find the information you need
                without wasting time.
              </p>

              <a href="/lookup">
                Go to Lookup →
              </a>

            </div>


            <div className="fn-feature-card">

              <div className="fn-feature-icon">
                🕐
              </div>

              <h3>
                Available Slots
              </h3>

              <p>
                View available fitness slots and
                choose a time that works for you.
              </p>

              <a href="/slot">
                View Slots →
              </a>

            </div>


            <div className="fn-feature-card">

              <div className="fn-feature-icon">
                👤
              </div>

              <h3>
                User Management
              </h3>

              <p>
                Manage user information and keep
                your FitNova profile organised.
              </p>

              <a href="/users">
                View Users →
              </a>

            </div>

          </div>

        </div>

      </section>


      {/* ================= HOW IT WORKS ================= */}

      <section className="fn-how" id="how-it-works">

        <div className="fn-section-container">

          <div className="fn-center-heading">

            <span className="fn-section-label">
              HOW IT WORKS
            </span>

            <h2>
              Fitness made
              <br />
              <strong>simple.</strong>
            </h2>

          </div>


          <div className="fn-steps">


            <div className="fn-step">

              <div className="fn-step-number">
                01
              </div>

              <h3>
                Find a slot
              </h3>

              <p>
                Check available fitness slots
                that suit your schedule.
              </p>

            </div>


            <div className="fn-step-arrow">
              →
            </div>


            <div className="fn-step">

              <div className="fn-step-number">
                02
              </div>

              <h3>
                Make a booking
              </h3>

              <p>
                Select your preferred time and
                make your booking.
              </p>

            </div>


            <div className="fn-step-arrow">
              →
            </div>


            <div className="fn-step">

              <div className="fn-step-number">
                03
              </div>

              <h3>
                Stay consistent
              </h3>

              <p>
                Keep track of your sessions and
                continue working towards your goals.
              </p>

            </div>

          </div>

        </div>

      </section>


      {/* ================= CTA ================= */}

      <section className="fn-final-cta">

        <div>

          <span className="fn-section-label">
            GET STARTED
          </span>

          <h2>
            Ready to start
            <br />
            your <strong>fitness journey?</strong>
          </h2>

          <p>
            Manage your fitness sessions with FitNova.
          </p>

          <a
            href="/booking"
            className="fn-primary-button fn-large-button"
          >
            Book a Session
            <span>→</span>
          </a>

        </div>

      </section>


      {/* ================= FOOTER ================= */}

      <footer className="fn-footer">

        <div className="fn-footer-container">

          <div className="fn-footer-brand">

            <a href="/" className="fn-brand">
              <span className="fn-brand-icon">F</span>
              <span>FitNova</span>
            </a>

            <p>
              Making fitness management simple,
              organised and accessible.
            </p>

          </div>


          <div className="fn-footer-column">

            <h4>Pages</h4>

            <a href="/">Home</a>
            <a href="/booking">Booking</a>
            <a href="/lookup">Lookup</a>
            <a href="/slot">Slots</a>
            <a href="/users">Users</a>

          </div>


          <div className="fn-footer-column">

            <h4>FitNova</h4>

            <a href="/booking">Get Started</a>
            <a href="/slot">Available Slots</a>
            <a href="/lookup">Lookup</a>

          </div>

        </div>


        <div className="fn-footer-bottom">

          <span>
            © 2026 FitNova. All rights reserved.
          </span>

          <span>
            Fitness made simple.
          </span>

        </div>

      </footer>

    </div>
  );
};

export default HomePage;
