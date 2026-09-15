import { describe, it, expect } from "vitest";
import { render, screen, within } from "@testing-library/react";
import App from "./App";

describe("App", () => {
  it("renders without crashing", () => {
    render(<App />);
  });

  it("renders the FitNova brand", () => {
    render(<App />);
    expect(screen.getAllByText("FitNova").length).toBeGreaterThan(0);
  });

  it("renders consistent main navigation on home", () => {
    render(<App />);
    const nav = screen.getByRole("navigation", { name: /Main/i });
    expect(within(nav).getByRole("link", { name: /Home/i })).toBeInTheDocument();
    expect(within(nav).getByRole("link", { name: /People/i })).toBeInTheDocument();
    expect(within(nav).getByRole("link", { name: /Availability/i })).toBeInTheDocument();
    expect(within(nav).getByRole("link", { name: /Bookings/i })).toBeInTheDocument();
    expect(within(nav).getByRole("link", { name: /Lookups/i })).toBeInTheDocument();
  });

  it("renders the hero heading", () => {
    render(<App />);
    expect(screen.getByRole("heading", { level: 1 })).toBeInTheDocument();
  });
});
