package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Address;
import za.ac.cput.repository.IAddressRepository;
import za.ac.cput.service.IAddressService;

import java.util.List;

/*
 * Author: Athi Sintiya
 * 220212317
 */

@Service
public class AddressServiceImpl implements IAddressService {

    private final IAddressRepository repository;

    @Autowired
    public AddressServiceImpl(IAddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public Address create(Address address) {
        return repository.save(address);
    }

    @Override
    public Address read(String addressId) {
        return repository.findById(addressId).orElse(null);
    }

    @Override
    public Address update(Address address) {
        return repository.save(address);
    }

    @Override
    public void delete(String addressId) {
        repository.deleteById(addressId);
    }

    @Override
    public List<Address> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Address> findAddressByCity(String city) {
        return repository.findAddressByCity(city);
    }

    @Override
    public List<Address> findAddressByProvince(String province) {
        return repository.findAddressByProvince(province);
    }

    @Override
    public List<Address> findAddressByPostalCode(String postalCode) {
        return repository.findAddressByPostalCode(postalCode);
    }
}