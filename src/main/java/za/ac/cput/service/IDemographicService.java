/*
 * IDemographicService.java
 * Author: Inga Plati
 * 230126634
 */
package za.ac.cput.service;

import za.ac.cput.domain.Demographic;

import java.util.List;

public interface IDemographicService {

    Demographic create(Demographic demographic);

    Demographic read(String demographyId);

    Demographic update(Demographic demographic);

    void delete(String demographyId);

    List<Demographic> getAll();
}