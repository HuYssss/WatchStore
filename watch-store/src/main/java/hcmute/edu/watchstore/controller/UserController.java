package hcmute.edu.watchstore.controller;

import java.security.Principal;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hcmute.edu.watchstore.base.ControllerBase;
import hcmute.edu.watchstore.dto.request.UserRequest;
import hcmute.edu.watchstore.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController extends ControllerBase {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/editDetail")
    public ResponseEntity<?> editUserDetail(@RequestBody UserRequest userRequest, Principal principal) {
        return this.userService.editUserDetail(userRequest, findIdByUsername(principal.getName()));
    }

    @GetMapping("")
    public ResponseEntity<?> getUserDetail(Principal principal) {
        return this.userService.getUserDetail(findIdByUsername(principal.getName()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllUser() {
        return this.userService.getAllUser();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/blockUser/{id}")
    public ResponseEntity<?> blockUser(@PathVariable ObjectId id,
                                        @RequestParam(value = "message", defaultValue = "Vi phạm điều khoản sử dụng") String message) {
        return this.userService.blockUser(id, message);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/unBlockUser/{id}")
    public ResponseEntity<?> unBlockUser(@PathVariable ObjectId id) {
        return this.userService.unBlockUser(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable ObjectId id) {
        return this.userService.deleteUser(id);
    }
}
