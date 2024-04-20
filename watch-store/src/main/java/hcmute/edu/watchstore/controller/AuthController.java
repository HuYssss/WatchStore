package hcmute.edu.watchstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hcmute.edu.watchstore.dto.request.LoginRequest;
import hcmute.edu.watchstore.dto.request.UserRequest;
import hcmute.edu.watchstore.service.UserService;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest userRequest) {
        return this.userService.register(userRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginReq) {
        return this.userService.login(loginReq);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam(value = "email", required = true) String email) {
        return this.userService.generateTokenReset(email);
    }
    
    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam(value = "token", required = true) String token, String password) {
        return this.userService.resetPassword(token, password);
    }
}
