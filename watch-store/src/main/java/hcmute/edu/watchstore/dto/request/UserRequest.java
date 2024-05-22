package hcmute.edu.watchstore.dto.request;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String email;
	private String phone;
	private String username;
	private String password;
	private String firstname;
    private String lastname;
    private String avatarImg;
    private String backgroundImg;
    private String address;
}
