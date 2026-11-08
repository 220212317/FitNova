package za.ac.cput.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import za.ac.cput.domain.GymResource;
import za.ac.cput.domain.enums.ResourceCategory;
import za.ac.cput.repository.IGymResourceRepository;

import java.util.List;

/**
 * Loads the starter resources that used to be hard-coded in the React app,
 * but only when the table is empty.
 */
@Component
public class GymResourceSeeder implements ApplicationRunner {

    private static final String DASH = "\u2013";
    private static final String ARROW = "\u2192";

    private final IGymResourceRepository repository;

    public GymResourceSeeder(IGymResourceRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (repository.count() > 0) {
            return;
        }
        repository.saveAll(List.of(
                item("m1", ResourceCategory.MANUAL, "Beginner strength foundation (4 weeks)",
                        "Full-body programme with progressive overload and recovery days.",
                        "Week structure: 3 lifting days + 1 optional conditioning. Focus on squat, hinge, press, and pull patterns. "
                                + "Increase load only when all reps are clean. Rest 90" + DASH + "120s between compound sets.",
                        "beginner", "strength"),
                item("m2", ResourceCategory.MANUAL, "Barbell squat form checklist",
                        "Setup, brace, depth, and common faults.",
                        "Feet roughly shoulder-width, toes slightly out. Brace as if about to take a punch. "
                                + "Sit between the hips, keep mid-foot pressure. Depth: hip crease below knee when mobility allows. "
                                + "Faults: heels rising, knees caving, lumbar rounding.",
                        "technique", "squat"),
                item("m3", ResourceCategory.MANUAL, "Upper / lower split overview",
                        "Intermediate 4-day template for hypertrophy.",
                        "Upper A/B and Lower A/B. Prioritise compounds early, accessories later. "
                                + "Track weekly volume per muscle group. Deload every 6" + DASH + "8 weeks or when performance stalls.",
                        "intermediate", "hypertrophy"),
                item("n1", ResourceCategory.NUTRITION, "Protein targets by goal",
                        "Simple formulas for gain, loss, and maintenance.",
                        "Muscle gain: ~1.6" + DASH + "2.2 g/kg bodyweight. Fat loss: keep protein high (similar range) to preserve muscle. "
                                + "Maintenance: ~1.4" + DASH + "1.8 g/kg. Spread intake across 3" + DASH + "4 meals when possible.",
                        "protein", "macros"),
                item("n2", ResourceCategory.NUTRITION, "Pre- and post-session fuel",
                        "What to eat around training without overthinking.",
                        "Pre (1" + DASH + "2h): carbs + moderate protein. Post: protein + carbs to support recovery. "
                                + "Hydration: start the session already topped up; sip during longer sessions.",
                        "timing", "recovery"),
                item("n3", ResourceCategory.NUTRITION, "Sample high-protein day plate",
                        "Practical food ideas, not a strict meal plan.",
                        "Breakfast: eggs + oats + fruit. Lunch: chicken, rice, mixed veg. Snack: Greek yoghurt + berries. "
                                + "Dinner: fish or lean beef, potatoes, salad. Adjust portions to energy needs.",
                        "meals", "examples"),
                item("t1", ResourceCategory.TIP, "5-minute dynamic warm-up",
                        "Raise temperature and prepare the joints you will use.",
                        "March or light bike 60s " + ARROW + " hip openers " + ARROW + " leg swings " + ARROW
                                + " band pull-aparts " + ARROW + " bodyweight squats. "
                                + "Match the warm-up to the session pattern (upper vs lower).",
                        "warm-up", "mobility"),
                item("t2", ResourceCategory.TIP, "Sleep is a training tool",
                        "Consistency beats hero sessions when recovery is poor.",
                        "Aim for a fixed sleep window. Dim screens late. Keep caffeine earlier in the day. "
                                + "Progressive training only compounds if recovery keeps up.",
                        "recovery", "habits"),
                item("t3", ResourceCategory.TIP, "Progressive overload without ego",
                        "Small weekly wins compound into big results.",
                        "Add a rep, then a small load increase. Film a set monthly. If form breaks, reduce load. "
                                + "Trainers can help set realistic progression rules per member.",
                        "progress", "mindset")
        ));
    }

    private static GymResource item(String id, ResourceCategory category, String title,
                                    String summary, String body, String... tags) {
        return new GymResource.Builder()
                .setResourceId(id)
                .setCategory(category)
                .setTitle(title)
                .setSummary(summary)
                .setBody(body)
                .setTags(List.of(tags))
                .build();
    }
}
