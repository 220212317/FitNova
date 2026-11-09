import { useCallback, useEffect, useState } from "react";
import { useAuth } from "../../auth/AuthContext";
import { getAllSlots } from "../features/slots/slotsApi";
import type { AvailabilitySlot } from "../../types";
import SlotForm from "../features/slots/SlotForm";
import SlotList from "../features/slots/SlotList";

function SlotsPage() {
    const { user } = useAuth();

    const [slots, setSlots] = useState<AvailabilitySlot[]>([]);
    const [editingSlot, setEditingSlot] = useState<AvailabilitySlot | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [toast, setToast] = useState("");

    const handleRefresh = useCallback(async () => {
        if (!user || user.role !== "TRAINER") {
            setSlots([]);
            setLoading(false);
            return;
        }

        try {
            setLoading(true);
            setError("");

            const data = await getAllSlots();

            const ownSlots = data.filter(
                (slot) => slot.trainer?.userId === user.userId
            );

            setSlots(ownSlots);
        } catch (err: unknown) {
            setError(
                err instanceof Error
                    ? err.message
                    : "Could not load availability slots."
            );
        } finally {
            setLoading(false);
        }
    }, [user]);

    useEffect(() => {
        if (!user || user.role !== "TRAINER") return;

        let cancelled = false;

        const fetchSlots = async () => {
            try {
                setLoading(true);
                setError("");

                const data = await getAllSlots();
                if (!cancelled) {
                    const ownSlots = data.filter(
                        (slot) => slot.trainer?.userId === user.userId
                    );
                    setSlots(ownSlots);
                }
            } catch (err: unknown) {
                if (!cancelled) {
                    setError(
                        err instanceof Error
                            ? err.message
                            : "Could not load availability slots."
                    );
                }
            } finally {
                if (!cancelled) setLoading(false);
            }
        };

        void fetchSlots();
        return () => {
            cancelled = true;
        };
    }, [user]);

    function showToast(message: string) {
        setToast(message);

        window.setTimeout(() => {
            setToast("");
        }, 3000);
    }

    function handleEdit(slot: AvailabilitySlot) {
        if (slot.trainer?.userId !== user?.userId) {
            setError("You cannot edit another trainer's availability.");
            return;
        }

        if (slot.status === "BOOKED") {
            setError("Booked availability cannot be edited.");
            return;
        }

        setError("");
        setEditingSlot(slot);

        window.scrollTo({
            top: 0,
            behavior: "smooth",
        });
    }

    async function handleSaved() {
        setEditingSlot(null);
        await handleRefresh();
        showToast("Availability slot saved successfully.");
    }

    function handleCancelEdit() {
        setEditingSlot(null);
        setError("");
    }

    async function handleDeleted() {
        await handleRefresh();
        showToast("Availability slot deleted successfully.");
    }

    if (!user || user.role !== "TRAINER") {
        return (
            <main>
                <h1>Availability Slots</h1>
                <div role="alert">
                    Only authenticated trainers can manage availability.
                </div>
            </main>
        );
    }

    return (
        <main className="slots-page">
            <section>
                <h1>My Availability Slots</h1>
                <p>Create, edit, view and manage your trainer availability.</p>
            </section>

            {toast && (
                <div role="status" aria-live="polite">
                    {toast}
                </div>
            )}

            <section>
                <SlotForm
                    editingSlot={editingSlot}
                    onSaved={handleSaved}
                    onCancelEdit={handleCancelEdit}
                />
            </section>

            <section>
                <div>
                    <button type="button" onClick={handleRefresh} disabled={loading}>
                        {loading ? "Loading..." : "Refresh Slots"}
                    </button>
                </div>

                {error && (
                    <div role="alert">
                        <strong>Availability error:</strong> {error}
                    </div>
                )}

                {loading ? (
                    <p>Loading your availability slots...</p>
                ) : (
                    <SlotList slots={slots} onEdit={handleEdit} onDeleted={handleDeleted} />
                )}
            </section>
        </main>
    );
}

export default SlotsPage;