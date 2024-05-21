package hcmute.edu.watchstore.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPassword {
    private String token;
    private String password;
}
