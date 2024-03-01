package hcmute.edu.watchstore.base;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hcmute.edu.watchstore.entity.User;
import hcmute.edu.watchstore.service.UserService;

@Component
public class ControllerBase {
    
    @Autowired
    private UserService userService;

    public ObjectId findIdByUsername(String username) {
        User currentUser = this.userService.findUserByUsername(username);

        if (currentUser != null) 
            return currentUser.getId();
        else
            return null;
    }
}
