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
import hcmute.edu.watchstore.dto.request.UserRequest;
import hcmute.edu.watchstore.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController extends ControllerBase {
    @Autowired
    private UserService userService;

    @PostMapping("/editDetail")
    public ResponseEntity<?> editUserDetail(@RequestBody UserRequest userRequest, Principal principal) {
        return this.userService.editUserDetail(userRequest, findIdByUsername(principal.getName()));
    }

    @GetMapping("")
    public ResponseEntity<?> getUserDetail(Principal principal) {
        return this.userService.getUserDetail(findIdByUsername(principal.getName()));
    }
}
