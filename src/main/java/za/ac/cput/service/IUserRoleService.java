/*
 * IUserRoleService.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.service;

import za.ac.cput.domain.UserRole;
import za.ac.cput.domain.enums.RoleType;

import java.util.List;
import java.util.Optional;

public interface IUserRoleService extends IService<UserRole, String> {

    List<UserRole> findByUser(String userId);

    List<UserRole> findByRole(RoleType roleId);

    Optional<UserRole> findByUserAndRole(String userId, RoleType roleId);
}