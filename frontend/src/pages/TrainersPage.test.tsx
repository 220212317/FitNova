import { render, screen } from "@testing-library/react";
import { afterEach, describe, expect, it, vi } from "vitest";
import TrainersPage from "./TrainersPage";

const mocks = vi.hoisted(() => ({ getAll: vi.fn() }));

vi.mock("../api/fitnova", () => ({
    trainersApi: { getAll: mocks.getAll },
}));

describe("TrainersPage", () => {
    afterEach(() => vi.clearAllMocks());

    it("renders safe trainer summaries", async () => {
        mocks.getAll.mockResolvedValue([
            { userId: "trainer-1", firstName: "Tara", lastName: "Trainer" },
        ]);
        render(<TrainersPage />);

        expect(screen.getByRole("status")).toHaveTextContent(/loading trainers/i);
        expect(await screen.findByRole("heading", { name: "Tara Trainer" })).toBeInTheDocument();
        expect(screen.queryByText(/password/i)).not.toBeInTheDocument();
        expect(screen.queryByText(/@/)).not.toBeInTheDocument();
    });

    it("shows API errors", async () => {
        mocks.getAll.mockRejectedValue(new Error("Directory unavailable"));
        render(<TrainersPage />);
        expect(await screen.findByRole("alert")).toHaveTextContent("Directory unavailable");
    });
});
