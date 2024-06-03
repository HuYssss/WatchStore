package hcmute.edu.watchstore.service;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import hcmute.edu.watchstore.dto.request.AddressRequest;

public interface AddressService {
    AddressRequest findAddressById(ObjectId addressId);
    ResponseEntity<?> findAddressByUser(ObjectId userId);
    ResponseEntity<?> createAddress(AddressRequest addressRequest, ObjectId userId);
    ResponseEntity<?> deleteAddress(ObjectId addressId, ObjectId userId);
}
