package za.ac.cput.security.command;
import za.ac.cput.domain.UserRole;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.service.IUserRoleService;

import java.util.Optional;
//Lisakhanya Tshokolo 220239215
public class RevokeRoleCommand implements RoleCommand{
        private final IUserRoleService userRoleService;
        private final String userId;
        private final RoleType roleType;

        public RevokeRoleCommand(
                IUserRoleService userRoleService,
                String userId,
                RoleType roleType
        ) {
            this.userRoleService = userRoleService;
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

            Optional<UserRole> existingRole =
                    userRoleService.findByUserAndRole(userId, roleType);

            if (existingRole.isEmpty()) {
                throw new IllegalStateException(
                        "User does not have the " + roleType + " role."
                );
            }

            UserRole userRole = existingRole.get();

            boolean deleted = userRoleService.delete(
                    userRole.getUserRoleId()
            );

            if (!deleted) {
                throw new IllegalStateException(
                        "Unable to revoke the requested role."
                );
            }
        }
    }