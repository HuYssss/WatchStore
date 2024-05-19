package hcmute.edu.watchstore.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.mongodb.MongoException;

import hcmute.edu.watchstore.base.ServiceBase;
import hcmute.edu.watchstore.constants.ResponseCode;
import hcmute.edu.watchstore.dto.request.LoginRequest;
import hcmute.edu.watchstore.dto.request.UserRequest;
import hcmute.edu.watchstore.dto.response.LoginResponse;
import hcmute.edu.watchstore.dto.response.UserResp;
import hcmute.edu.watchstore.entity.Cart;
import hcmute.edu.watchstore.entity.Role;
import hcmute.edu.watchstore.entity.User;
import hcmute.edu.watchstore.exception.InvalidValueException;
import hcmute.edu.watchstore.exception.NoParamException;
import hcmute.edu.watchstore.helper.MailService;
import hcmute.edu.watchstore.helper.ResetTokenGenerator;
import hcmute.edu.watchstore.repository.CartRepository;
import hcmute.edu.watchstore.repository.RoleRepository;
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

    @Autowired
    private MailService mailService;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User findUserByUsername(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        if (user.isPresent()) {
            Set<Role> roles = new HashSet<>();
            for(Role r : user.get().getRole()){
                roles.add(this.roleRepository.findById(r.getId()).orElse(null));
            }
            user.get().setRole(roles);
        }
        return user.orElse(null);
    }

    @Override
    public ResponseEntity<?> register(UserRequest userReq) {
        if (this.userRepository.findByEmail(userReq.getEmail()).isPresent())
            return error(ResponseCode.EMAIL_ALREADY_REGISTERED.getCode(), ResponseCode.EMAIL_ALREADY_REGISTERED.getMessage());
        
        if (this.userRepository.findByUsername(userReq.getUsername()).isPresent()) {
            return error(ResponseCode.DUPLICATED_USERNAME.getCode(), ResponseCode.DUPLICATED_USERNAME.getMessage());
        }
            
        try {
            User saveUser = Validation.validateSaveUser(userReq);
            saveUser.setPassword(this.passwordEncoder.encode(saveUser.getPassword()));

            Cart cart = new Cart();
            cart.setUser(saveUser.getId());
            cart.setProductItems(new ArrayList<>());
            this.cartRepository.save(cart);

            saveUser.setCart(cart.getId());
            this.userRepository.save(saveUser);
            
            return success("Register User Success !!!");
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

    @Override
    public ResponseEntity<?> generateTokenReset(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        String resetToken = ResetTokenGenerator.generateRandomString();

        String[] cc = new String[1];
        cc[0] = email;

        if (user.isPresent()) {
            try {
                this.mailService.sendMail(null, email, cc
                , "Token Reset Password"
                , "Hi "+ user.get().getUsername() + ",\r\n" + "\r\n" +
                                    "There was a request to change your password!\r\n" + "\r\n" +
                                    "If you did not make this request then please ignore this email.\r\n" + "\r\n" +
                                    "Otherwise, please use this token to reset your password: " + resetToken);

                user.get().setToken(resetToken);
                
                this.userRepository.save(user.get());
                
                return success("Reset password token has been sent");
            } catch (Exception e) {
                return error(ResponseCode.CANNOT_SEND_EMAIL.getCode(), ResponseCode.CANNOT_SEND_EMAIL.getMessage());
            } 
        }
        else
            return error(ResponseCode.USER_NOT_FOUND.getCode(), ResponseCode.USER_NOT_FOUND.getMessage());
    }

    @Override
    public ResponseEntity<?> resetPassword(String token, String password) {
        if (password.isEmpty()) {
            return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
        }

        Optional<User> user = this.userRepository.findByToken(token);

        if (user.isPresent() && !user.get().getToken().isEmpty()) {
            user.get().setPassword(this.passwordEncoder.encode(password));
            user.get().setToken(null);
            this.userRepository.save(user.get());

            return success(user.get());
        }
        else
            return error(ResponseCode.USER_NOT_FOUND.getCode(), ResponseCode.USER_NOT_FOUND.getMessage());
    }

    @Override
    public ResponseEntity<?> editUserDetail(UserRequest userReq, ObjectId userId) {
        User currentUser = this.userRepository.findById(userId).get();
        
        if (userReq.getPhone() != null) {
            currentUser.setPhone(userReq.getPhone());
        }
        if (userReq.getAvatarImg() != null) {
            currentUser.setAvatarImg(userReq.getAvatarImg());
        }
        if (userReq.getBackgroundImg() != null) {
            currentUser.setBackgroundImg(userReq.getBackgroundImg());
        }
        if (userReq.getFirstname() != null) {
            currentUser.setFirstname(userReq.getFirstname());
        }
        if (userReq.getLastname() != null) {
            currentUser.setLastname(userReq.getLastname());
        }
        
        try {
            this.userRepository.save(currentUser);
            return success(currentUser);
        } catch (MongoException e) {
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getUserDetail(ObjectId userId) {
        User user = findUserById(userId);
        if (user == null) {
            return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
        }
        UserResp userResp = new UserResp(user);

        for(Role r : user.getRole()){
            Optional<Role> role = this.roleRepository.findById(r.getId());
            if (role.isPresent() && role.get().getRoleName().equals("ADMIN")) {
                userResp.setAdmin(true);
            }
        }
        return success(userResp);
    }

    @Override
    public User findUserById(ObjectId userId) {
        return this.userRepository.findById(userId).orElse(null);
    }

    @Override
    public ResponseEntity<?> blockUser(ObjectId userId) {
        User user = findUserById(userId);
        if (user == null) {
            return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage()); 
        }

        try {
            user.setState("block");
            this.userRepository.save(user);
            return success("Block user success !!!");
        } catch (MongoException e) {
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> logout(ObjectId userId) {
        return null;
    }
}