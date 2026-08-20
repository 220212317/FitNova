package za.ac.cput.service;

import za.ac.cput.domain.Address;

import java.util.List;

/*
 * Author: Athi Sintiya
 * 220212317
 */

public interface IAddressService extends IService<Address, String> {

    List<Address> findAddressByCity(String city);

    List<Address> findAddressByProvince(String province);

    List<Address> findAddressByPostalCode(String postalCode);
}