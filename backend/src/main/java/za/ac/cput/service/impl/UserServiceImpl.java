/*
 * UserServiceImpl.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.domain.User;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.security.adapter.AuthenticatedUser;
import za.ac.cput.repository.IAccountRepository;
import za.ac.cput.repository.IAvailabilitySlotRepository;
import za.ac.cput.repository.IBookingRepository;
import za.ac.cput.repository.IUserRepository;
import za.ac.cput.repository.IUserRoleRepository;
import za.ac.cput.service.IUserService;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final IUserRoleRepository userRoleRepository;
    private final IBookingRepository bookingRepository;
    private final IAvailabilitySlotRepository slotRepository;
    private final IAccountRepository accountRepository;

    @Autowired
    public UserServiceImpl(IUserRepository userRepository,
                           IUserRoleRepository userRoleRepository,
                           IBookingRepository bookingRepository,
                           IAvailabilitySlotRepository slotRepository,
                           IAccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.bookingRepository = bookingRepository;
        this.slotRepository = slotRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User read(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User update(User user) {
        if (!userRepository.existsById(user.getUserId())) {
            return null;
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean delete(String userId) {
        if (!userRepository.existsById(userId)) {
            return false;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AuthenticatedUser) {
            AuthenticatedUser me = (AuthenticatedUser) auth.getPrincipal();
            if (userId.equals(me.getUserId())) {
                throw new IllegalStateException(
                        "You cannot delete your own account here. Use your profile page instead.");
            }
        }

        boolean targetIsAdmin = userRoleRepository.findByUser_UserId(userId).stream()
                .anyMatch(r -> r.getRoleId() == RoleType.ADMIN);
        if (targetIsAdmin && userRoleRepository.findByRoleId(RoleType.ADMIN).size() <= 1) {
            throw new IllegalStateException("At least one administrator must remain.");
        }

        String accountId = userRepository.findById(userId)
                .map(u -> u.getAccount() != null ? u.getAccount().getAccountId() : null)
                .orElse(null);

        // Remove rows that reference the user before the user itself (foreign keys).
        bookingRepository.deleteAll(bookingRepository.findByMember_UserId(userId));
        for (AvailabilitySlot slot : slotRepository.findByTrainer_UserId(userId)) {
            bookingRepository.deleteAll(bookingRepository.findBySlot_SlotId(slot.getSlotId()));
            slotRepository.delete(slot);
        }
        userRoleRepository.deleteAll(userRoleRepository.findByUser_UserId(userId));

        userRepository.deleteById(userId);
        userRepository.flush();

        // Free the login email so it can be registered again.
        if (accountId != null) {
            accountRepository.deleteById(accountId);
        }
        return true;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findByFirstNameAndLastName(String firstName, String lastName) {
        return userRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    @Override
    public List<User> searchByLastName(String lastName) {
        return userRepository.findByLastNameContainingIgnoreCase(lastName);
    }
}