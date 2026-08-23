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
        if (address == null || address.getAddressId() == null) {
            return null;
        }
        return repository.save(address);
    }

    @Override
    public Address read(String addressId) {
        if (addressId == null) {
            return null;
        }
        return repository.findById(addressId).orElse(null);
    }

    @Override
    public Address update(Address address) {
        if (address == null || address.getAddressId() == null) {
            return null;
        }
        if (!repository.existsById(address.getAddressId())) {
            return null;
        }
        return repository.save(address);
    }

    @Override
    public boolean delete(String addressId) {
        if (addressId == null || !repository.existsById(addressId)) {
            return false;
        }
        repository.deleteById(addressId);
        return true;
    }

    @Override
    public List<Address> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Address> findAddressByCity(String city) {
        if (city == null) {
            return List.of();
        }
        return repository.findAddressByCity(city);
    }

    @Override
    public List<Address> findAddressByProvince(String province) {
        if (province == null) {
            return List.of();
        }
        return repository.findAddressByProvince(province);
    }

    @Override
    public List<Address> findAddressByPostalCode(String postalCode) {
        if (postalCode == null) {
            return List.of();
        }
        return repository.findAddressByPostalCode(postalCode);
    }
}