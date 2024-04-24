package hcmute.edu.watchstore.service.impl;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import hcmute.edu.watchstore.base.ServiceBase;
import hcmute.edu.watchstore.constants.ResponseCode;
import hcmute.edu.watchstore.dto.request.AddressRequest;
import hcmute.edu.watchstore.entity.Address;
import hcmute.edu.watchstore.entity.User;
import hcmute.edu.watchstore.repository.AddressRepository;
import hcmute.edu.watchstore.repository.UserRepository;
import hcmute.edu.watchstore.service.AddressService;

@Service
public class AddressServiceImpl extends ServiceBase implements AddressService{

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> findAddressByUser(ObjectId userId) {
        Iterable<Address> list = this.addressRepository.findByUser(userId);
        return success(list);
    }

    @Override
    public ResponseEntity<?> createAddress(AddressRequest addressRequest, ObjectId userId) {
        if (addressRequest.getAddress() != null && addressRequest.getCity() != null && addressRequest.getCountry() != null) {
            Address newAddress = new Address();
            newAddress.setId(new ObjectId());
            newAddress.setAddress(addressRequest.getAddress());
            newAddress.setCity(addressRequest.getCity());
            newAddress.setCountry(addressRequest.getCountry());
            newAddress.setUser(userId);

            try {
                this.addressRepository.save(newAddress);
                handleManageAddressUser(newAddress.getId(), userId, "create");
                return success("Add new address success !!!");
            } catch (Exception e) {
                return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
            }
        }
        return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
    }

    @Override
    public ResponseEntity<?> updateAddress(Address address, ObjectId userId) {
        Optional<Address> currentAddress = this.addressRepository.findById(address.getId());

        if (!currentAddress.isPresent() && currentAddress.get().getUser().equals(userId)) {
            return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
        }

        if (address.getAddress() != null && address.getCity() != null && address.getCountry() != null) {
            Address newAddress = currentAddress.get();
            newAddress.setAddress(address.getAddress());
            newAddress.setCity(address.getCity());
            newAddress.setCountry(address.getCountry());

            try {
                this.addressRepository.save(newAddress);
                return success("Edit address success !!!");
            } catch (Exception e) {
                return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
            }
        }
        return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
    }

    @Override
    public ResponseEntity<?> deleteAddress(ObjectId addressId, ObjectId userId) {
        Optional<Address> currentAddress = this.addressRepository.findById(addressId);

        if (!currentAddress.isPresent() && currentAddress.get().getUser().equals(userId)) {
            return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
        }

        try {
            handleManageAddressUser(addressId, userId, "delete");
            this.addressRepository.deleteById(addressId);
            return success("Delete address success !!!");
        } catch (Exception e) {
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
        }
    }
    
    public boolean handleManageAddressUser(ObjectId addressId, ObjectId userId, String message) {
        Optional<User> currentUser = this.userRepository.findById(userId);

        if (currentUser.isPresent()) {
            try {
                List<ObjectId> addresses = currentUser.get().getAddress();

                if (message.equals("delete")) 
                    addresses.remove(addressId);

                if (message.equals("create")) 
                    addresses.add(addressId);
                    
                currentUser.get().setAddress(addresses);
                this.userRepository.save(currentUser.get());
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
