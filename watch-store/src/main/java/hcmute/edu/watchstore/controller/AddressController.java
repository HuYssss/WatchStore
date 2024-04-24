package hcmute.edu.watchstore.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hcmute.edu.watchstore.base.ControllerBase;
import hcmute.edu.watchstore.dto.request.AddressRequest;
import hcmute.edu.watchstore.service.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController extends ControllerBase{

    @Autowired
    private AddressService addressService;
    
    @GetMapping("")
    public ResponseEntity<?> getAllAddress(Principal principal) {
        return this.addressService.findAddressByUser(findIdByUsername(principal.getName()));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAddress(@RequestBody AddressRequest addressRequest, Principal principal) {
        return this.addressService.createAddress(addressRequest, findIdByUsername(principal.getName()));
    }
}
