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
}
