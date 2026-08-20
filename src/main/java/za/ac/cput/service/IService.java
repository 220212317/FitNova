package za.ac.cput.service;

import java.util.List;

/*
 * Author: Athi Sintiya
 * 220212317
 */

public interface IService<T, ID> {

    T create(T t);

    T read(ID id);

    T update(T t);

    void delete(ID id);

    List<T> getAll();
}