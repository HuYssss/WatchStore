package hcmute.edu.watchstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hcmute.edu.watchstore.dto.request.ForgotPassword;
import hcmute.edu.watchstore.dto.request.LoginRequest;
import hcmute.edu.watchstore.dto.request.ResetPass;
import hcmute.edu.watchstore.dto.request.UserRequest;
import hcmute.edu.watchstore.service.UserService;



@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    // đăng ký
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest userRequest) {
        return this.userService.register(userRequest);
    }

    // đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginReq) {
        return this.userService.login(loginReq);
    }

    // quên mật khẩu
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPassword forgotPassword) {
        return this.userService.generateTokenReset(forgotPassword.getEmail());
    }
    
    // cập nhật lại mật khẩu
    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPass resetPass) {
        return this.userService.resetPassword(resetPass.getToken(), resetPass.getNewPass());
    }
}
