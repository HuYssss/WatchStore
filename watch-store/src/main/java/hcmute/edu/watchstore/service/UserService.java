package hcmute.edu.watchstore.service;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import hcmute.edu.watchstore.dto.request.LoginRequest;
import hcmute.edu.watchstore.dto.request.UserRequest;
import hcmute.edu.watchstore.entity.User;

public interface UserService {
    User findUserByUsername(String username);
    User findUserById(ObjectId id);
    ResponseEntity<?> register(UserRequest userReq);
    ResponseEntity<?> login(LoginRequest loginReq);
    ResponseEntity<?> generateTokenReset(String email);
    ResponseEntity<?> resetPassword(String token, String password);
    ResponseEntity<?> editUserDetail(UserRequest userReq, ObjectId userId);
    ResponseEntity<?> getUserDetail(ObjectId userId);
    ResponseEntity<?> blockUser(ObjectId userId, String message);
    ResponseEntity<?> unBlockUser(ObjectId userId); 
    ResponseEntity<?> getAllUser();
    ResponseEntity<?> deleteUser(ObjectId userId);
}