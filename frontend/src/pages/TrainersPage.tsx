import { useEffect, useState } from "react";
import { Dumbbell, UserRoundSearch } from "lucide-react";
import { trainersApi } from "../api/fitnova";
import type { TrainerSummary } from "../types";
import "./TrainersPage.css";

export default function TrainersPage() {
    const [trainers, setTrainers] = useState<TrainerSummary[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        let cancelled = false;
        trainersApi.getAll()
            .then((data) => {
                if (!cancelled) setTrainers(data);
            })
            .catch((caught) => {
                if (!cancelled) {
                    setError(caught instanceof Error ? caught.message : "Could not load trainers.");
                }
            })
            .finally(() => {
                if (!cancelled) setLoading(false);
            });
        return () => { cancelled = true; };
    }, []);

    return (
        <section className="trainer-directory" aria-labelledby="trainer-directory-title">
            <header className="trainer-directory__header">
                <div className="trainer-directory__icon"><UserRoundSearch /></div>
                <div>
                    <p className="trainer-directory__eyebrow">FITNOVA TEAM</p>
                    <h1 id="trainer-directory-title">Find a trainer</h1>
                    <p>Meet the trainers available to support your fitness journey.</p>
                </div>
            </header>

            {loading && <div className="trainer-directory__state" role="status">Loading trainers…</div>}
            {error && <div className="trainer-directory__state trainer-directory__state--error" role="alert">{error}</div>}
            {!loading && !error && trainers.length === 0 && (
                <div className="trainer-directory__state">No trainers are available yet.</div>
            )}
            {!loading && !error && trainers.length > 0 && (
                <div className="trainer-grid">
                    {trainers.map((trainer) => (
                        <article className="trainer-card" key={trainer.userId}>
                            <div className="trainer-card__avatar" aria-hidden="true">
                                {initials(trainer)}
                            </div>
                            <div>
                                <h2>{trainer.firstName} {trainer.lastName}</h2>
                                <p><Dumbbell size={15} /> FitNova trainer</p>
                            </div>
                        </article>
                    ))}
                </div>
            )}
        </section>
    );
}

function initials(trainer: TrainerSummary): string {
    return `${trainer.firstName?.[0] ?? ""}${trainer.lastName?.[0] ?? ""}`.toUpperCase() || "FT";
}
