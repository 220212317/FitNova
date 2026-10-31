package za.ac.cput.security.access;
import za.ac.cput.domain.enums.RoleType;

import java.util.Objects;
import java.util.Set;
//Lisakhanya Tshokolo 220239215

public record AccessContext(String userId, Set<RoleType> roles) {

    public AccessContext {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be empty.");
        }

        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("At least one role is required.");
        }

        roles = Set.copyOf(roles);

        if (roles.contains(null)) {
            throw new IllegalArgumentException("Roles cannot contain null.");
        }
    }

    public boolean hasRole(RoleType role) {
        return roles.contains(Objects.requireNonNull(role));
    }

    public boolean isAdmin() {
        return hasRole(RoleType.ADMIN);
    }

    public boolean ownsResource(String resourceOwnerId) {
        return resourceOwnerId != null
                && userId.equals(resourceOwnerId);
    }
}