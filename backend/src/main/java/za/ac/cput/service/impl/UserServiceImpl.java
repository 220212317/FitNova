/*
 * UserServiceImpl.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.User;
import za.ac.cput.repository.IUserRepository;
import za.ac.cput.service.IUserService;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;

    @Autowired
    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
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
    public boolean delete(String userId) {
        if (!userRepository.existsById(userId)) {
            return false;
        }
        userRepository.deleteById(userId);
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