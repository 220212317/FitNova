package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Address;

import java.util.List;

/*
 * Author: Athi Sintiya
 * 220212317
 */

@Repository
public interface IAddressRepository extends JpaRepository<Address, String> {

    List<Address> findAddressByCity(String city);

    List<Address> findAddressByProvince(String province);

    List<Address> findAddressByPostalCode(String postalCode);
}