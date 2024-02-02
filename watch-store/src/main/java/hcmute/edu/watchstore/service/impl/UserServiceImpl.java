package hcmute.edu.watchstore.service.impl;

import java.text.ParseException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import hcmute.edu.watchstore.base.ServiceBase;
import hcmute.edu.watchstore.constants.ResponseCode;
import hcmute.edu.watchstore.dto.request.LoginRequest;
import hcmute.edu.watchstore.dto.request.UserRequest;
import hcmute.edu.watchstore.dto.response.LoginResponse;
import hcmute.edu.watchstore.entity.Cart;
import hcmute.edu.watchstore.entity.User;
import hcmute.edu.watchstore.exception.InvalidValueException;
import hcmute.edu.watchstore.exception.NoParamException;
import hcmute.edu.watchstore.repository.CartRepository;
import hcmute.edu.watchstore.repository.UserRepository;
import hcmute.edu.watchstore.service.UserService;
import hcmute.edu.watchstore.util.JwtUtils;
import hcmute.edu.watchstore.util.Validation;

@Service
public class UserServiceImpl extends ServiceBase implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public User findUserByUsername(String username) {
        Optional<User> currentUser = this.userRepository.findByUsername(username);
        return currentUser.orElse(null);
    }

    @Override
    public ResponseEntity<?> register(UserRequest userReq) {
        try {
            User saveUser = Validation.validateSaveUser(userReq);
            saveUser.setPassword(this.passwordEncoder.encode(saveUser.getPassword()));
            this.userRepository.save(saveUser);

            Cart cart = new Cart();
            cart.setUser(saveUser.getId());
            this.cartRepository.save(cart);

            return success(saveUser);
        } catch (InvalidValueException e) {
            throw new RuntimeException(e);
        } catch (NoParamException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginReq) {
        if (ObjectUtils.isEmpty(loginReq) || ObjectUtils.isEmpty(loginReq.getPassword())
                || ObjectUtils.isEmpty(loginReq.getUsername()))
            return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());

        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginReq.getUsername(), loginReq.getPassword()));

            String token = JwtUtils.generateToken(loginReq.getUsername());
            LoginResponse authResponse = new LoginResponse(token, "Login Successful !!!");
            return success(authResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return error(ResponseCode.INCORRECT_AUTHEN.getCode(), ResponseCode.INCORRECT_AUTHEN.getMessage());
        }
    }
    
}
