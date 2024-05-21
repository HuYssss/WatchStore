package hcmute.edu.watchstore.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.springframework.util.ObjectUtils;

import hcmute.edu.watchstore.dto.request.UserRequest;
import hcmute.edu.watchstore.entity.Role;
import hcmute.edu.watchstore.entity.User;
import hcmute.edu.watchstore.exception.InvalidValueException;
import hcmute.edu.watchstore.exception.NoParamException;

public class Validation {
    public static User validateSaveUser(UserRequest userRequest) throws InvalidValueException, NoParamException, ParseException {
        validate(userRequest);

        User parsedUser = new User();
        parsedUser.setId(new ObjectId());
        parsedUser.setEmail(userRequest.getEmail());
        parsedUser.setPhone(userRequest.getPhone());
        parsedUser.setUsername(userRequest.getUsername());
        parsedUser.setPassword(userRequest.getPassword());
        parsedUser.setOrder(new ArrayList<>());
        parsedUser.setState("active");
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setId(new ObjectId("65bb1b854c79c0063ff039e2"));
        roles.add(role);
        parsedUser.setRole(roles);
        return parsedUser;
    }

    public static void validate(UserRequest userRequest)
            throws InvalidValueException, NoParamException, ParseException{
        if (ObjectUtils.isEmpty(userRequest))
            throw new NoParamException();
        if (ObjectUtils.isEmpty(userRequest.getEmail()) || ObjectUtils.isEmpty(userRequest.getPhone()) ||
                ObjectUtils.isEmpty(userRequest.getUsername()) || ObjectUtils.isEmpty(userRequest.getPassword()))
            throw new NoParamException();
        if (!isValidEmail(userRequest.getEmail()) || userRequest.getPhone().length() != 10)
            throw new InvalidValueException();
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
