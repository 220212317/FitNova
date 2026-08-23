package za.ac.cput.service;

import za.ac.cput.domain.Demographic;

import java.util.List;

/*
 * Author: Inga Plati
 * 230126634
 */

public interface IDemographicService {

    Demographic create(Demographic demographic);

    Demographic read(String demographyId);

    Demographic update(Demographic demographic);

    boolean delete(String demographyId);

    List<Demographic> getAll();
}