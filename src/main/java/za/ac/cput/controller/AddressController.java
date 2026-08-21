package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Address;
import za.ac.cput.service.IAddressService;

import java.util.List;

/*
 * AddressController.java
 * Author: Athi Sintiya
 * 220212317
 */

@RestController
@RequestMapping("/address")
public class AddressController {

    private final IAddressService addressService;

    @Autowired
    public AddressController(IAddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/create")
    public ResponseEntity<Address> create(@RequestBody Address address) {
        Address created = addressService.create(address);
        if (created == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/read/{addressId}")
    public ResponseEntity<Address> read(@PathVariable String addressId) {
        Address address = addressService.read(addressId);
        if (address == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(address);
    }

    @PutMapping("/update")
    public ResponseEntity<Address> update(@RequestBody Address address) {
        Address updated = addressService.update(address);
        if (updated == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{addressId}")
    public ResponseEntity<Void> delete(@PathVariable String addressId) {
        boolean deleted = addressService.delete(addressId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findByCity/{city}")
    public ResponseEntity<List<Address>> findAddressByCity(@PathVariable String city) {
        List<Address> addresses = addressService.findAddressByCity(city);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/findByProvince/{province}")
    public ResponseEntity<List<Address>> findAddressByProvince(@PathVariable String province) {
        List<Address> addresses = addressService.findAddressByProvince(province);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/findByPostalCode/{postalCode}")
    public ResponseEntity<List<Address>> findAddressByPostalCode(@PathVariable String postalCode) {
        List<Address> addresses = addressService.findAddressByPostalCode(postalCode);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Address>> getAll() {
        List<Address> addresses = addressService.getAll();
        return ResponseEntity.ok(addresses);
    }
}