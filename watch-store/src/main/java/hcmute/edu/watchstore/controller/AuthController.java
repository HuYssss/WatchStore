package hcmute.edu.watchstore.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hcmute.edu.watchstore.dto.request.ForgotPassword;
import hcmute.edu.watchstore.dto.request.LoginRequest;
import hcmute.edu.watchstore.dto.request.ResetPassword;
import hcmute.edu.watchstore.dto.request.UserRequest;
import hcmute.edu.watchstore.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;



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
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPassword forgotPassword) {
        return this.userService.generateTokenReset(forgotPassword.getEmail());
    }
    
    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPassword resetPassword) {
        return this.userService.resetPassword(resetPassword.getToken(), resetPassword.getPassword());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/testAuthUser")
    public String testAuthUser(Principal principal) {
        return "Hello user " + principal.getName();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/testAuthAdmin")
    public String testAuthAdmin(Principal principal) {
        return "Hello admin " + principal.getName();
    }
}
