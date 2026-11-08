import { useEffect, type RefObject } from "react";

/**
 * Drives the landing page's lighting from scroll + pointer:
 *  - `--p`   (0 → 1) hero scroll progress, consumed by CSS for the beam, arc and product tilt
 *  - `.reveal` elements fade/slide in once, staggered by `--i`
 *  - `.section` gets `.lit` when it enters, lighting its top edge
 *  - `.spot` cards get `--mx/--my` so a spotlight follows the cursor
 * Does nothing (content stays fully visible, static) under prefers-reduced-motion.
 */
export function useScrollLighting(rootRef: RefObject<HTMLElement | null>) {
  useEffect(() => {
    const root = rootRef.current;
    if (!root) return;
    if (window.matchMedia("(prefers-reduced-motion: reduce)").matches) {
      root.style.setProperty("--p", "1");
      root.querySelectorAll(".section").forEach((s) => s.classList.add("lit"));
      return;
    }

    root.classList.add("js-reveal");
    const hero = root.querySelector<HTMLElement>(".hero");

    // Scroll progress, rAF-throttled
    let frame = 0;
    const update = () => {
      frame = 0;
      const h = (hero?.offsetHeight ?? window.innerHeight) * 0.85;
      const p = Math.min(1, Math.max(0, window.scrollY / h));
      root.style.setProperty("--p", p.toFixed(3));
    };
    const onScroll = () => {
      if (!frame) frame = requestAnimationFrame(update);
    };
    update();
    window.addEventListener("scroll", onScroll, { passive: true });
    window.addEventListener("resize", onScroll);

    // Reveal + section edge light
    const io = new IntersectionObserver(
      (entries) => {
        for (const e of entries) {
          if (!e.isIntersecting) continue;
          e.target.classList.add(e.target.classList.contains("section") ? "lit" : "in");
          io.unobserve(e.target);
        }
      },
      { threshold: 0.12, rootMargin: "0px 0px -8% 0px" },
    );
    root.querySelectorAll(".reveal, .section").forEach((el) => io.observe(el));

    // Cursor spotlight
    const onMove = (ev: PointerEvent) => {
      const card = (ev.target as HTMLElement).closest<HTMLElement>(".spot");
      if (!card) return;
      const r = card.getBoundingClientRect();
      card.style.setProperty("--mx", `${ev.clientX - r.left}px`);
      card.style.setProperty("--my", `${ev.clientY - r.top}px`);
    };
    root.addEventListener("pointermove", onMove);

    return () => {
      if (frame) cancelAnimationFrame(frame);
      window.removeEventListener("scroll", onScroll);
      window.removeEventListener("resize", onScroll);
      root.removeEventListener("pointermove", onMove);
      io.disconnect();
      root.classList.remove("js-reveal");
    };
  }, [rootRef]);
}
