import { useCallback, useEffect, useState } from "react";

import { getAllSlots } from "../features/slots/slotsApi";

import type {
    AvailabilitySlot,
} from "../types";

import SlotForm from "../features/slots/SlotForm";
import SlotList from "../features/slots/SlotList";

function SlotsPage() {
    const [slots, setSlots] = useState<AvailabilitySlot[]>([]);
    const [editingSlot, setEditingSlot] =
        useState<AvailabilitySlot | null>(null);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const loadSlots = useCallback(async () => {
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
    }, []);

    useEffect(() => {
        loadSlots();
    }, []); // loadSlots is stable due to useCallback

    function handleEdit(slot: AvailabilitySlot) {
        setEditingSlot(slot);

        window.scrollTo({
            top: 0,
            behavior: "smooth",
        });
    }

    function handleSaved() {
        setEditingSlot(null);
        loadSlots();
    }

    function handleCancelEdit() {
        setEditingSlot(null);
    }

    return (
        <main>
            <section>
                <h1>Availability Slots</h1>

                <p>
                    Create, edit, view and delete trainer availability
                    slots.
                </p>
            </section>

            <section>
                <SlotForm
                    editingSlot={editingSlot}
                    onSaved={handleSaved}
                    onCancelEdit={handleCancelEdit}
                />
            </section>

            <section>
                {loading ? (
                    <p>Loading availability slots...</p>
                ) : (
                    <>
                        <div>
                            <button
                                type="button"
                                onClick={loadSlots}
                            >
                                Refresh Slots
                            </button>
                        </div>

                        {error && (
                            <p role="alert">
                                {error}
                            </p>
                        )}

                        {!error && (
                            <SlotList
                                slots={slots}
                                onEdit={handleEdit}
                                onDeleted={loadSlots}
                            />
                        )}
                    </>
                )}
            </section>
        </main>
    );
}

export default SlotsPage;