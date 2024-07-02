package hcmute.edu.watchstore.helper;

public interface MailService {
    String sendResetToken(String email, String token, String username);
    String blockUser(String email, String username, String message);
    String deleteUser(String email, String username);
    String unBlockUser(String email, String username);
    String welcome(String email, String username);
}
