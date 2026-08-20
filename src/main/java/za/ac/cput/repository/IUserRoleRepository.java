/*
 * IUserRoleRepository.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.UserRole;
import za.ac.cput.domain.enums.RoleType;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRoleRepository extends JpaRepository<UserRole, String> {

    List<UserRole> findByUser_UserId(String userId);

    List<UserRole> findByRoleId(RoleType roleId);

    Optional<UserRole> findByUser_UserIdAndRoleId(String userId, RoleType roleId);
}