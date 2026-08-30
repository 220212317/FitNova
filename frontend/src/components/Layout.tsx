/** TODO — Lisakhanya Tshokolo (220239215) */
import React from "react";
import "./HomePage.css";

interface LayoutProps {
    children: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
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


            {/* ================= PAGE CONTENT ================= */}

            <main>
                {children}
            </main>


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

export default Layout;