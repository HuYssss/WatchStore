package hcmute.edu.watchstore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import hcmute.edu.watchstore.base.ServiceBase;
import hcmute.edu.watchstore.entity.Address;
import hcmute.edu.watchstore.repository.AddressRepository;
import hcmute.edu.watchstore.service.AddressService;

@Service
public class AddressServiceImpl extends ServiceBase implements AddressService{

    @Autowired
    private AddressRepository repo;

    @Override
    public ResponseEntity<?> findAll() {
        List<Address> list = repo.findAll();
        return success(list);
    }
    
}
