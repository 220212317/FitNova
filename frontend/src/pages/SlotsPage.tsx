import { useEffect, useState } from "react";
import { getAllSlots } from "../features/slots/slotsApi";
import type { AvailabilitySlot } from "../types";

import SlotForm from "../features/slots/SlotForm";
import SlotList from "../features/slots/SlotList";

function SlotsPage() {
    const [slots, setSlots] = useState<AvailabilitySlot[]>([]);
    const [editingSlot, setEditingSlot] =
        useState<AvailabilitySlot | null>(null);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [toast, setToast] = useState("");

    useEffect(() => {
        let cancelled = false;

        async function fetchSlots() {
            try {
                setLoading(true);
                setError("");

                const data = await getAllSlots();

                if (!cancelled) {
                    setSlots(data);
                }
            } catch (err) {
                if (!cancelled) {
                    setError(
                        err instanceof Error
                            ? err.message
                            : "Could not load availability slots."
                    );
                }
            } finally {
                if (!cancelled) {
                    setLoading(false);
                }
            }
        }

        fetchSlots();

        return () => {
            cancelled = true;
        };
    }, []);

    async function handleRefresh() {
        try {
            setLoading(true);
            setError("");

            const data = await getAllSlots();
            setSlots(data);
        } catch (err) {
            setError(
                err instanceof Error
                    ? err.message
                    : "Could not load availability slots."
            );
        } finally {
            setLoading(false);
        }
    }

    function showToast(message: string) {
        setToast(message);

        window.setTimeout(() => {
            setToast("");
        }, 3000);
    }

    function handleEdit(slot: AvailabilitySlot) {
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

    return (
        <main>
            <section>
                <h1>Availability Slots</h1>

                <p>
                    Create, edit, view and delete trainer availability slots.
                </p>
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
                    <button
                        type="button"
                        onClick={handleRefresh}
                        disabled={loading}
                    >
                        {loading ? "Loading..." : "Refresh Slots"}
                    </button>
                </div>

                {error && (
                    <div role="alert">
                        <strong>Unable to load slots:</strong>{" "}
                        {error}
                    </div>
                )}

                {loading ? (
                    <p>Loading availability slots...</p>
                ) : (
                    <SlotList
                        slots={slots}
                        onEdit={handleEdit}
                        onDeleted={handleDeleted}
                    />
                )}
            </section>
        </main>
    );

}
export default SlotsPage;