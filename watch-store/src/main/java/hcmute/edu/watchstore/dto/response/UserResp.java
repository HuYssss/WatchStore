package hcmute.edu.watchstore.dto.response;

import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import hcmute.edu.watchstore.entity.Role;
import hcmute.edu.watchstore.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResp {
    private String id;

    private String email;

    private String phone;

    private String username;

    private String password;

    private String firstname;

    private String lastname;

    private String avatarImg;

    private String backgroundImg;
    
    private String address;

    private List<ObjectId> order;

    private Set<Role> role;

    private ObjectId cart;

    private String state;
    
    private String token;

    private boolean isAdmin;


    public UserResp(User user) {
        this.id = user.getId().toHexString();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.avatarImg = user.getAvatarImg();
        this.backgroundImg = user.getBackgroundImg();
        this.address = user.getAddress();
        this.order = user.getOrder();
        this.role = user.getRole();
        this.cart = user.getCart();
        this.state = user.getState();
    }

}
