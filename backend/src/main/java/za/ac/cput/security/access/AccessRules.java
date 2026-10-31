package za.ac.cput.security.access;

import org.springframework.stereotype.Component;
import org.springframework.security.access.AccessDeniedException;
import za.ac.cput.domain.enums.RoleType;
//Lisakhanya Tshokolo 220239215

@Component("accessRules")
public class AccessRules {
        public boolean isAdmin(AccessContext context) {
            return context != null
                    && context.hasRole(RoleType.ADMIN);
        }

        /**
         * Allows a member to access their own data.
         * Administrators may also access member data.
         */
        public boolean canAccessMemberData(
                AccessContext context,
                String memberUserId
        ) {
            return context != null
                    && (
                    isAdmin(context)
                            || (
                            context.hasRole(RoleType.MEMBER)
                                    && context.ownsResource(memberUserId)
                    )
            );
        }

        /**
         * Allows a trainer to manage their own availability.
         * Administrators may also manage trainer availability.
         */
        public boolean canManageAvailability(
                AccessContext context,
                String trainerUserId
        ) {
            return context != null
                    && (
                    isAdmin(context)
                            || (
                            context.hasRole(RoleType.TRAINER)
                                    && context.ownsResource(trainerUserId)
                    )
            );
        }

        /**
         * Checks whether the user owns a resource.
         */
        public boolean isOwner(
                AccessContext context,
                String resourceOwnerId
        ) {
            return context != null
                    && context.ownsResource(resourceOwnerId);
        }

        /**
         * Allows access to a resource owned by the user
         * or permits an administrator.
         */
        public boolean isOwnerOrAdmin(
                AccessContext context,
                String resourceOwnerId
        ) {
            return isAdmin(context)
                    || isOwner(context, resourceOwnerId);
        }

        /**
         * Allows administrators only.
         */
        public boolean canManageUsers(AccessContext context) {
            return isAdmin(context);
        }

        /**
         * Enforces a rule and throws an exception if access is denied.
         */
        public void requireAccess(
                boolean allowed,
                String message
        ) {
            if (!allowed) {
                throw new AccessDeniedException(message);
            }
        }
}
