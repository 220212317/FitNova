/*
 * AdminLookupsPage.tsx
 * Author: Inga Plati
 * 230126634
 */
import { useEffect, useState } from "react";
import { lookupsApi } from "../../api/fitnova";
import { useToast } from "../../components/ToastContext";
import type { Gender, Race } from "../../types";
import { ensureId } from "../../api/ids";
import { LookupPicker } from "./LookupPicker";
import { GENDER_PRESETS, RACE_PRESETS, type LookupItem } from "./lookupNames";

export function AdminLookupsPage() {
  const { toast } = useToast();
  const [genders, setGenders] = useState<Gender[]>([]);
  const [races, setRaces] = useState<Race[]>([]);
  const [loading, setLoading] = useState(true);

  const fetchLookups = async () => {
    try {
      const [g, r] = await Promise.all([
        lookupsApi.genders().catch(() => [] as Gender[]),
        lookupsApi.races().catch(() => [] as Race[]),
      ]);
      setGenders(g);
      setRaces(r);
    } finally {
      setLoading(false);
    }
  };

  const load = async () => {
    setLoading(true);
    await fetchLookups();
  };

  useEffect(() => {
    void fetchLookups();
  }, []);

  const run = async (action: () => Promise<unknown>, success: string) => {
    await action();
    toast(success, "success");
    await load();
  };

  const genderItems: LookupItem[] = genders.map((g) => ({
    id: g.genderId,
    description: g.description,
  }));

  const raceItems: LookupItem[] = races.map((r) => ({
    id: r.raceId,
    description: r.description,
  }));

  return (
    <div>
      <h1 className="page-title">Lookups</h1>
      <p className="page-sub">Admin-only reference data used across member profiles.</p>

      {loading ? (
        <div className="skeleton" style={{ height: 100 }} />
      ) : (
        <div className="grid-2">
          <LookupPicker
            title="Genders"
            noun="gender"
            presets={GENDER_PRESETS}
            items={genderItems}
            onCreate={(description) =>
              run(
                () => lookupsApi.createGender({ genderId: ensureId(""), description }),
                "Gender added."
              )
            }
            onUpdate={(item) =>
              run(
                () => lookupsApi.updateGender({ genderId: item.id, description: item.description }),
                "Gender updated."
              )
            }
            onDelete={(item) => run(() => lookupsApi.deleteGender(item.id), "Gender deleted.")}
          />
          <LookupPicker
            title="Races"
            noun="race"
            presets={RACE_PRESETS}
            items={raceItems}
            onCreate={(description) =>
              run(
                () => lookupsApi.createRace({ raceId: ensureId(""), description }),
                "Race added."
              )
            }
            onUpdate={(item) =>
              run(
                () => lookupsApi.updateRace({ raceId: item.id, description: item.description }),
                "Race updated."
              )
            }
            onDelete={(item) => run(() => lookupsApi.deleteRace(item.id), "Race deleted.")}
          />
        </div>
      )}
    </div>
  );
}
