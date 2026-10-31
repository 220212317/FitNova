package za.ac.cput.security.command;
import jakarta.persistence.EntityManager;
import za.ac.cput.domain.User;
import za.ac.cput.domain.UserRole;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.service.IUserRoleService;

import java.util.UUID;
//Lisakhanya Tshokolo 220239215

public class AssignRoleCommand implements RoleCommand {
    private final IUserRoleService userRoleService;
    private final EntityManager entityManager;
    private final String userId;
    private final RoleType roleType;

    public AssignRoleCommand(
            IUserRoleService userRoleService,
            EntityManager entityManager,
            String userId,
            RoleType roleType
    ) {
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

        // Prevent duplicate role assignments.
        if (userRoleService.findByUserAndRole(userId, roleType).isPresent()) {
            throw new IllegalStateException(
                    "User already has the " + roleType + " role."
            );
        }

        // Obtain a managed reference to the existing user.
        User user = entityManager.getReference(User.class, userId);

        UserRole userRole = new UserRole.Builder()
                .setUserRoleId(UUID.randomUUID().toString())
                .setUser(user)
                .setRoleId(roleType)
                .setDescription("Assigned role: " + roleType)
                .build();

        userRoleService.create(userRole);
    }
}
