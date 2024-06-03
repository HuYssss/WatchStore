package hcmute.edu.watchstore.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    // tìm kiếm user theo username
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

    // đăng ký user
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
            
            return success(saveUser);
        } catch (InvalidValueException e) {
            throw new RuntimeException(e);
        } catch (NoParamException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // hàm đăng nhập
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

    // hàm tạo token reset password và gửi token về mail
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

    // update lại mật khẩu mới
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

    // chỉnh sửa thông tin user
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

    // trar về thông tin user cho FE
    @Override
    public ResponseEntity<?> getUserDetail(ObjectId userId) {
        // User user = findUserById(userId);
        // if (user == null) {
        //     return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
        // }
        // UserResp userResp = new UserResp(user);
        // userResp.setAddress(convertListString(user.getAddress()));
        // userResp.setOrder(convertListString(user.getOrder()));
        // return success(userResp);

        return success(findUserById(userId));
    }

    // hàm tìm kiếm user trong db
    @Override
    public User findUserById(ObjectId userId) {
        return this.userRepository.findById(userId).orElse(null);
    }

    // chuyển đổi danh sách ObjectId sang String
    public List<String> convertListString(List<ObjectId> idList) {
        List<String> result = new ArrayList<>();
        for(ObjectId id : idList) {
            result.add(id.toHexString());
        }
        return result;
    }
}