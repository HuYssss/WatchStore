package hcmute.edu.watchstore.service;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import hcmute.edu.watchstore.dto.request.AddressRequest;
import hcmute.edu.watchstore.entity.Address;

public interface AddressService {
    Address findAddressById(ObjectId addressId);
    ResponseEntity<?> findAddressByUser(ObjectId userId);
    ResponseEntity<?> createAddress(AddressRequest addressRequest, ObjectId userId);
    ResponseEntity<?> updateAddress(Address address, ObjectId userId);
    ResponseEntity<?> deleteAddress(ObjectId addressId, ObjectId userId);
}
