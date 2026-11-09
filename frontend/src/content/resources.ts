export type ResourceCategory = "manual" | "nutrition" | "tip";

export interface ResourceItem {
  id: string;
  category: ResourceCategory;
  title: string;
  summary: string;
  body: string;
  tags: string[];
}

export const RESOURCES: ResourceItem[] = [
  {
    id: "m1",
    category: "manual",
    title: "Beginner strength foundation (4 weeks)",
    summary: "Full-body programme with progressive overload and recovery days.",
    body: "Week structure: 3 lifting days + 1 optional conditioning. Focus on squat, hinge, press, and pull patterns. Increase load only when all reps are clean. Rest 90–120s between compound sets.",
    tags: ["beginner", "strength"],
  },
  {
    id: "m2",
    category: "manual",
    title: "Barbell squat form checklist",
    summary: "Setup, brace, depth, and common faults.",
    body: "Feet roughly shoulder-width, toes slightly out. Brace as if about to take a punch. Sit between the hips, keep mid-foot pressure. Depth: hip crease below knee when mobility allows. Faults: heels rising, knees caving, lumbar rounding.",
    tags: ["technique", "squat"],
  },
  {
    id: "m3",
    category: "manual",
    title: "Upper / lower split overview",
    summary: "Intermediate 4-day template for hypertrophy.",
    body: "Upper A/B and Lower A/B. Prioritise compounds early, accessories later. Track weekly volume per muscle group. Deload every 6–8 weeks or when performance stalls.",
    tags: ["intermediate", "hypertrophy"],
  },
  {
    id: "n1",
    category: "nutrition",
    title: "Protein targets by goal",
    summary: "Simple formulas for gain, loss, and maintenance.",
    body: "Muscle gain: ~1.6–2.2 g/kg bodyweight. Fat loss: keep protein high (similar range) to preserve muscle. Maintenance: ~1.4–1.8 g/kg. Spread intake across 3–4 meals when possible.",
    tags: ["protein", "macros"],
  },
  {
    id: "n2",
    category: "nutrition",
    title: "Pre- and post-session fuel",
    summary: "What to eat around training without overthinking.",
    body: "Pre (1–2h): carbs + moderate protein. Post: protein + carbs to support recovery. Hydration: start the session already topped up; sip during longer sessions.",
    tags: ["timing", "recovery"],
  },
  {
    id: "n3",
    category: "nutrition",
    title: "Sample high-protein day plate",
    summary: "Practical food ideas, not a strict meal plan.",
    body: "Breakfast: eggs + oats + fruit. Lunch: chicken, rice, mixed veg. Snack: Greek yoghurt + berries. Dinner: fish or lean beef, potatoes, salad. Adjust portions to energy needs.",
    tags: ["meals", "examples"],
  },
  {
    id: "t1",
    category: "tip",
    title: "5-minute dynamic warm-up",
    summary: "Raise temperature and prepare the joints you will use.",
    body: "March or light bike 60s → hip openers → leg swings → band pull-aparts → bodyweight squats. Match the warm-up to the session pattern (upper vs lower).",
    tags: ["warm-up", "mobility"],
  },
  {
    id: "t2",
    category: "tip",
    title: "Sleep is a training tool",
    summary: "Consistency beats hero sessions when recovery is poor.",
    body: "Aim for a fixed sleep window. Dim screens late. Keep caffeine earlier in the day. Progressive training only compounds if recovery keeps up.",
    tags: ["recovery", "habits"],
  },
  {
    id: "t3",
    category: "tip",
    title: "Progressive overload without ego",
    summary: "Small weekly wins compound into big results.",
    body: "Add a rep, then a small load increase. Film a set monthly. If form breaks, reduce load. Trainers can help set realistic progression rules per member.",
    tags: ["progress", "mindset"],
  },
];
