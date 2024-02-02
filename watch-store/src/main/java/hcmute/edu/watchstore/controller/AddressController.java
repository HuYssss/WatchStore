package hcmute.edu.watchstore.controller;

import org.springframework.web.bind.annotation.RestController;

import hcmute.edu.watchstore.service.AddressService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService service;

    @GetMapping("")
    public ResponseEntity<?> findAllAddress() {
        return this.service.findAll();
    }
}
