/*
 * UserRoleServiceImpl.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.UserRole;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.repository.IUserRoleRepository;
import za.ac.cput.service.IUserRoleService;

import java.util.List;
import java.util.Optional;

@Service
public class UserRoleServiceImpl implements IUserRoleService {

    private final IUserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleServiceImpl(IUserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserRole create(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @Override
    public UserRole read(String userRoleId) {
        return userRoleRepository.findById(userRoleId).orElse(null);
    }

    @Override
    public UserRole update(UserRole userRole) {
        if (!userRoleRepository.existsById(userRole.getUserRoleId())) {
            return null;
        }
        return userRoleRepository.save(userRole);
    }

    @Override
    public boolean delete(String userRoleId) {
        if (!userRoleRepository.existsById(userRoleId)) {
            return false;
        }
        userRoleRepository.deleteById(userRoleId);
        return true;
    }

    @Override
    public List<UserRole> getAll() {
        return userRoleRepository.findAll();
    }

    @Override
    public List<UserRole> findByUser(String userId) {
        return userRoleRepository.findByUser_UserId(userId);
    }

    @Override
    public List<UserRole> findByRole(RoleType roleId) {
        return userRoleRepository.findByRoleId(roleId);
    }

    @Override
    public Optional<UserRole> findByUserAndRole(String userId, RoleType roleId) {
        return userRoleRepository.findByUser_UserIdAndRoleId(userId, roleId);
    }
}