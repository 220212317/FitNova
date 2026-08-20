/*
 * IUserService.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.service;

import za.ac.cput.domain.User;

import java.util.List;

public interface IUserService extends IService<User, String> {

    List<User> findByFirstNameAndLastName(String firstName, String lastName);

    List<User> searchByLastName(String lastName);
}