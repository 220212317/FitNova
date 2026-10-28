package za.ac.cput.security.auth;

public record RegisterRequest(String firstName, String lastName, String email, String password, java.time.LocalDate dateOfBirth) {
    // TODO: implement
}
