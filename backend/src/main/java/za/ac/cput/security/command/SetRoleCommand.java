package za.ac.cput.security.command;

import jakarta.persistence.EntityManager;
import za.ac.cput.domain.User;
import za.ac.cput.domain.UserRole;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.service.IUserRoleService;

import java.util.List;
import java.util.UUID;

/**
 * Makes {@code roleType} the user's only role: any other role rows are removed and the
 * target role is added if the user does not have it yet.
 * Refuses to strip the last remaining administrator.
 */
public class SetRoleCommand implements RoleCommand {

    private final IUserRoleService userRoleService;
    private final EntityManager entityManager;
    private final String userId;
    private final RoleType roleType;

    public SetRoleCommand(IUserRoleService userRoleService,
                          EntityManager entityManager,
                          String userId,
                          RoleType roleType) {
        this.userRoleService = userRoleService;
        this.entityManager = entityManager;
        this.userId = userId;
        this.roleType = roleType;
    }

    @Override
    public void execute() {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID is required.");
        }
        if (roleType == null) {
            throw new IllegalArgumentException("Role type is required.");
        }

        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        List<UserRole> current = userRoleService.findByUser(userId);

        boolean losingAdmin = roleType != RoleType.ADMIN
                && current.stream().anyMatch(r -> r.getRoleId() == RoleType.ADMIN);
        if (losingAdmin && userRoleService.findByRole(RoleType.ADMIN).size() <= 1) {
            throw new IllegalStateException(
                    "At least one administrator must remain. Make someone else an admin first.");
        }

        boolean alreadyHasTarget = false;
        for (UserRole existing : current) {
            if (existing.getRoleId() == roleType && !alreadyHasTarget) {
                alreadyHasTarget = true;
            } else {
                userRoleService.delete(existing.getUserRoleId());
            }
        }

        if (!alreadyHasTarget) {
            userRoleService.create(new UserRole.Builder()
                    .setUserRoleId(UUID.randomUUID().toString())
                    .setUser(user)
                    .setRoleId(roleType)
                    .setDescription("Assigned role: " + roleType)
                    .build());
        }
    }
}
