package za.ac.cput.security.directory;

import za.ac.cput.domain.User;

public record TrainerSummary(String userId, String firstName, String lastName) {
    public static TrainerSummary from(User user) {
        if (user == null) throw new IllegalArgumentException("user is required");
        return new TrainerSummary(user.getUserId(), user.getFirstName(), user.getLastName());
    }
}
