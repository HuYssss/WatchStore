package hcmute.edu.watchstore.helper;

import org.springframework.web.multipart.MultipartFile;

public interface MailService {
    // hàm send email
    String sendMail(MultipartFile[] file, String to, String[] cc, String subject, String body);
}
