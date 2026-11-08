import { useEffect, useRef, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import {
  BookOpen,
  Apple,
  Flame,
  Shield,
  ArrowRight,
  Play,
  TrendingUp,
  Target,
  Trophy,
  HeartPulse,
  Zap,
} from "lucide-react";
import { useAuth } from "../../auth/AuthContext";
import { useScrollLighting } from "../shared/useScrollLighting";
import { FitNovaLogo } from "../../components/FitNovaLogo";
import "./LandingPage.css";

const FEATURES = [
  {
    icon: TrendingUp,
    title: "Conflict-free booking",
    desc: "Book only available future slots. Confirmed in one step — no double-booking for you or your trainer.",
  },
  {
    icon: Target,
    title: "Trainers in control",
    desc: "Coaches publish their own availability. Members see real open slots, not outdated spreadsheets.",
  },
  {
    icon: BookOpen,
    title: "Gym manuals & programmes",
    desc: "Form guides and progressive plans from the coaching team — unlocked after you create an account.",
  },
  {
    icon: Apple,
    title: "Nutrition guidance",
    desc: "Protein targets, timing, and simple plates so training and diet stay aligned.",
  },
  {
    icon: Flame,
    title: "Daily tips",
    desc: "Short, practical habits that keep members consistent without the fluff.",
  },
  {
    icon: Shield,
    title: "Role-based access",
    desc: "Members book. Trainers manage slots. Admins oversee users and lookups.",
  },
];

const RESOURCES = [
  {
    tag: "Manual",
    title: "Beginner strength foundation",
    body: "A 4-week full-body programme with progressive loading and rest-day guidance.",
  },
  {
    tag: "Nutrition",
    title: "Protein targets by goal",
    body: "Simple formulas for muscle gain, fat loss, and maintenance — plus sample day plates.",
  },
  {
    tag: "Tip",
    title: "Warm-up that actually works",
    body: "5-minute dynamic sequence that raises temperature and primes the joints you need.",
  },
  {
    tag: "Manual",
    title: "Barbell squat checklist",
    body: "Setup, bracing, depth, and common faults — clear enough for members and trainers.",
  },
];

const STEPS = [
  {
    title: "Create your free account",
    body: "Sign up in under a minute. Choose your role and get instant access to the dashboard.",
  },
  {
    title: "Browse trainers & open slots",
    body: "See real availability from coaches. Filter by time, style, or trainer and book with one tap.",
  },
  {
    title: "Train with clarity",
    body: "Show up prepared. Unlock manuals, nutrition guides, and daily tips that match your sessions.",
  },
];

type Props = { section?: "resources" | "about" };

export function LandingPage({ section }: Props) {
  const { isAuthenticated } = useAuth();
  const location = useLocation();
  const rootRef = useRef<HTMLDivElement>(null);

  // State to track the active section for the navbar underline
  const [activeSection, setActiveSection] = useState("home");

  useScrollLighting(rootRef);

  // Handle URL hash on mount/change
  useEffect(() => {
    const hash = section ?? location.hash?.replace("#", "");

    // If the hash is "home" or empty, scroll to the top
    if (!hash || hash === "home") {
      window.scrollTo({ top: 0, behavior: "smooth" });
      setActiveSection("home");
    } else {
      const el = document.getElementById(hash);
      if (el) {
        // Offset for the fixed navbar (approx 82px)
        const yOffset = -90;
        const y = el.getBoundingClientRect().top + window.scrollY + yOffset;
        window.scrollTo({ top: y, behavior: "smooth" });
      }
      setActiveSection(hash);
    }
  }, [section, location.hash]);

  // Scroll listener to update the active nav link
  useEffect(() => {
    const handleScroll = () => {
      const sections = ["home", "features", "about", "faqs"];
      let current = "home";

      for (const sectionId of sections) {
        const element = document.getElementById(sectionId);
        if (element) {
          const rect = element.getBoundingClientRect();
          // If the top of the section is above the middle of the screen, mark it active
          if (rect.top <= window.innerHeight / 2) {
            current = sectionId;
          }
        }
      }
      setActiveSection(current);
    };

    window.addEventListener("scroll", handleScroll);
    handleScroll(); // Initial check
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  return (
      <div className="landing" ref={rootRef}>
        {/* Invisible anchor for the "Home" section so the browser doesn't jump to the middle of the page */}
        <div id="home" style={{ position: "absolute", top: 0 }} />

        <header className="land-nav">
          <div className="land-nav-inner">
            {/* NAVBAR BRAND */}
            <Link to="/" className="land-brand" style={{ gap: "0.75rem" }} onClick={() => setActiveSection("home")}>
              <FitNovaLogo size={40} />
              <span style={{ fontSize: "1.6rem", letterSpacing: "-0.03em" }}>
              <span style={{ color: "#ffffff" }}>Fit</span>
              <span style={{ color: "#8df34b" }}>Nova</span>
            </span>
            </Link>

            <nav className="land-links">
              <a href="#home" className={activeSection === "home" ? "active" : ""}>Home</a>
              <a href="#features" className={activeSection === "features" ? "active" : ""}>Features</a>
              <a href="#about" className={activeSection === "about" ? "active" : ""}>About</a>
              <a href="#faqs" className={activeSection === "faqs" ? "active" : ""}>FAQs</a>
            </nav>

            <div className="land-actions">
              {isAuthenticated ? (
                  <Link to="/dashboard" className="btn">
                    Open app <ArrowRight size={16} />
                  </Link>
              ) : (
                  <>
                    <Link to="/login" className="btn btn-outline">
                      Sign In
                    </Link>
                    <Link to="/signup" className="btn">
                      Get Started
                    </Link>
                  </>
              )}
            </div>
          </div>
        </header>

        {/* Hero */}
        <section className="hero">
          <div className="hero-bg" aria-hidden="true" />

          <div className="hero-center">
            <div className="hero-pills">
            <span className="pill pill-accent">
              <Zap size={14} /> Train Smarter. Live Healthier.
            </span>
            </div>

            <h1>
              Your Fitness
              <br />
              Journey <em>Starts Here</em>
            </h1>
            <p>
              Track your workouts, set goals, and reach your fitness potential with FitNova.
            </p>

            <div className="hero-cta">
              {isAuthenticated ? (
                  <Link to="/dashboard" className="btn">
                    Go to dashboard <ArrowRight size={16} />
                  </Link>
              ) : (
                  <>
                    <Link to="/signup" className="btn">
                      Get Started Free <ArrowRight size={16} />
                    </Link>
                    <a href="#features" className="btn btn-outline">
                      Learn More <Play size={14} fill="currentColor" />
                    </a>
                  </>
              )}
            </div>
          </div>

          {/* HERO HIGHLIGHTS */}
          <div className="hero-highlights" aria-label="FitNova benefits">
            <div className="hero-highlight">
              <TrendingUp size={26} />
              <span>Track Progress</span>
            </div>
            <div className="hero-highlight">
              <Target size={26} />
              <span>Set Goals</span>
            </div>
            <div className="hero-highlight">
              <Trophy size={26} />
              <span>Earn Achievements</span>
            </div>
            <div className="hero-highlight">
              <HeartPulse size={26} />
              <span>Build a Healthier You</span>
            </div>
          </div>
        </section>

        <section className="section" id="features">
          <div className="container">
            <div className="section-head reveal">
              <h2>Everything a modern gym needs</h2>
              <p>
                One platform for booking, coaching, and content — built so members,
                trainers, and admins stay in sync without the usual friction.
              </p>
            </div>
            <div className="feature-grid">
              {FEATURES.map(({ icon: Icon, title, desc }, i) => (
                  <article
                      key={title}
                      className="feature-card spot reveal"
                      style={{ "--i": i } as React.CSSProperties}
                  >
                    <div className="feature-icon">
                      <Icon size={20} />
                    </div>
                    <h3>{title}</h3>
                    <p>{desc}</p>
                  </article>
              ))}
            </div>
          </div>
        </section>

        <section className="section section-alt" id="how">
          <div className="container">
            <div className="section-head reveal">
              <h2>Get started in three simple steps</h2>
              <p>From signup to your first session — clear, fast, and conflict-free.</p>
            </div>
            <ol className="steps">
              {STEPS.map((step, i) => (
                  <li key={step.title} className="spot reveal" style={{ "--i": i } as React.CSSProperties}>
                    <span className="step-num">{i + 1}</span>
                    <div>
                      <strong>{step.title}</strong>
                      <p>{step.body}</p>
                    </div>
                  </li>
              ))}
            </ol>
          </div>
        </section>

        <section className="section" id="resources">
          <div className="container">
            <div className="section-head reveal">
              <h2>Manuals, nutrition & tips</h2>
              <p>Preview the library. Full content unlocks after you create an account.</p>
            </div>
            <div className="resource-grid">
              {RESOURCES.map((r, i) => (
                  <article
                      key={r.title}
                      className="resource-card spot reveal"
                      style={{ "--i": i } as React.CSSProperties}
                  >
                    <span className="res-tag">{r.tag}</span>
                    <h3>{r.title}</h3>
                    <p>{r.body}</p>
                  </article>
              ))}
            </div>
            <div className="section-cta reveal">
              {isAuthenticated ? (
                  <Link to="/app/resources" className="btn">
                    Open full library
                  </Link>
              ) : (
                  <Link to="/signup" className="btn">
                    Create account to access library
                  </Link>
              )}
            </div>
          </div>
        </section>

        <section className="section section-alt" id="about">
          <div className="container about-grid">
            <div className="reveal">
              <h2>About FitNova</h2>
              <p>
                FitNova was built for real gym floors — the ones where trainers juggle
                paper schedules, members miss slots, and resources live in scattered
                folders. We replace that friction with one calm system.
              </p>
              <p>
                Members book confirmed sessions. Trainers own their calendars.
                Admins keep everything tidy. Everyone gets the manuals, nutrition
                guidance, and daily habits that turn good intentions into consistent
                training.
              </p>
            </div>
            <div className="about-panel glass reveal" style={{ "--i": 1 } as React.CSSProperties}>
              <h3>Who it is for</h3>
              <ul>
                <li>
                  <strong>Members</strong>
                  Book open slots, track sessions, unlock programmes & tips.
                </li>
                <li>
                  <strong>Trainers</strong>
                  Publish availability, manage bookings, share resources.
                </li>
                <li>
                  <strong>Gym admins</strong>
                  Oversee users, roles, and the lookup data that powers the floor.
                </li>
              </ul>
            </div>
          </div>
        </section>

        <footer className="land-footer" id="faqs">
          <div className="land-footer-inner">
            {/* FOOTER BRAND */}
            <span className="land-brand" style={{ gap: "0.75rem" }}>
            <FitNovaLogo size={36} />
            <span style={{ fontSize: "1.4rem", letterSpacing: "-0.03em" }}>
              <span style={{ color: "#ffffff" }}>Fit</span>
              <span style={{ color: "#8df34b" }}>Nova</span>
            </span>
          </span>

            <p>© {new Date().getFullYear()} FitNova. Train with purpose.</p>
            <div className="land-actions">
              {!isAuthenticated && (
                  <>
                    <Link to="/login" className="btn btn-ghost">
                      Sign in
                    </Link>
                    <Link to="/signup" className="btn btn-outline">
                      Join
                    </Link>
                  </>
              )}
            </div>
          </div>
        </footer>
      </div>
  );
}