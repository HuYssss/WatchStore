package hcmute.edu.watchstore.service.impl;

import java.util.ArrayList;
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

    // lấy toàn bộ address của user
    @Override
    public ResponseEntity<?> findAddressByUser(ObjectId userId) {
        List<Address> list = this.addressRepository.findByUser(userId);
        List<AddressRequest> result = new ArrayList<>();
        if (!list.isEmpty()) {
            for(Address a : list)
            {
                AddressRequest resp = new AddressRequest(a);
                result.add(resp);
            }
            return success(result);
        }
        return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
    }

    // tạo một address mới
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

    // xóa address
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
    
    // handle các hành động thêm hoặc xóa address
    public void handleManageAddressUser(ObjectId addressId, ObjectId userId, String message) throws Exception {
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
            } catch (Exception e) {
                throw new Exception(e);
            }
        }
    }

    // tìm 1 address bằng id
    @Override
    public AddressRequest findAddressById(ObjectId addressId) {
        Optional<Address> address = this.addressRepository.findById(addressId);
        if (address.isPresent()) {
            return new AddressRequest(address.get());
        }
        return null;
    }
}
