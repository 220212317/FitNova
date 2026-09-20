package za.ac.cput.factory;

import za.ac.cput.domain.User;
import za.ac.cput.domain.UserRole;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.util.Helper;

/*
 * UserRoleFactory.java
 * Author: Collins Shibambo
 * 230093183
 */
public class UserRoleFactory {

    public static UserRole createUserRole(String userRoleId, User user, RoleType roleId, String description) {

        if (user == null) {
            return null;
        }

        if (roleId == null) {
            return null;
        }

        if (Helper.isNullOrEmpty(userRoleId)) {
            userRoleId = Helper.generateId();
        }

        return new UserRole.Builder()
                .setUserRoleId(userRoleId)
                .setUser(user)
                .setRoleId(roleId)
                .setDescription(description)
                .build();
    }

    public static UserRole createUserRole(User user, RoleType roleId, String description) {
        return createUserRole(Helper.generateId(), user, roleId, description);
    }
}