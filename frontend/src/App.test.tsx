import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import App from "./App";

describe("App", () => {
  it("renders without crashing", () => {
    render(<App />);
  });

  it("renders the FitNova brand", () => {
    render(<App />);
    expect(screen.getAllByText("FitNova").length).toBeGreaterThan(0);
  });

  it("renders landing navigation links", () => {
    render(<App />);
    expect(screen.getByRole("link", { name: /Sign in/i })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /Join FitNova/i })).toBeInTheDocument();
  });

  it("renders the hero heading", () => {
    render(<App />);
    expect(screen.getByRole("heading", { level: 1 })).toBeInTheDocument();
  });
});
