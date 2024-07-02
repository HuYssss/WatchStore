package hcmute.edu.watchstore.helper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import hcmute.edu.watchstore.helper.MailService;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender javaMailSender;

    public String sendMail(MultipartFile[] file, String to, String[] cc, String subject, String body) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setCc(cc);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body);

            if (file != null) {
                for (int i = 0; i < file.length; i++) {
                    mimeMessageHelper.addAttachment(
                            file[i].getOriginalFilename(),
                            new ByteArrayResource(file[i].getBytes()));
                }
            }

            javaMailSender.send(mimeMessage);

            return "mail send";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendResetToken(String email, String token, String username) {
        String subject = "Yêu cầu cấp lại mật khẩu";
        String body = "Xin chào "+ username + ",\r\n" + "\r\n" +
            "Chúng tôi nhận được yêu cầu xin cấp lại mật khẩu từ bạn\r\n" + "\r\n" +
            "Nếu yêu cầu này không phải của bạn xin vui lòng bỏ qua email này\r\n" + "\r\n" +
            "Vui lòng không cung cấp token sau cho bất cứ bên nào khác để bảo mật tài khoản của bạn: " + token;
        String[] cc = new String[1];
        cc[0] = email;

        try {
            return sendMail(null, email, cc, subject, body);
        } catch (Exception e) {
            return "Send mail failure !!!";
        }
    }

    @Override
    public String blockUser(String email, String username, String message) {
        String subject = "Thông báo khóa tài khoản";
        String body = "Xin chào "+ username + ",\r\n" + "\r\n" +
            "Chúng tôi viết email này để thông báo rằng tài khoản của bạn trên trang web Watches Store đã bị khóa tạm thời.\r\n" + "\r\n" +
            "Lý do khóa tài khoản: " +
            message + "\r\n\n" +
            "Chúng tôi hiểu rằng điều này có thể gây bất tiện cho bạn và chúng tôi xin lỗi vì sự bất tiện này. Để giải quyết vấn đề và khôi phục lại tài khoản của bạn, vui lòng liên hệ với chúng tôi qua địa chỉ email lehuyburn23@gmail.com hoặc số điện thoại 0765196829 và cung cấp thông tin chi tiết về vấn đề của bạn.\r\n\n" + 
            "Chúng tôi cam kết bảo vệ quyền lợi của khách hàng và đảm bảo rằng mọi vấn đề sẽ được giải quyết một cách nhanh chóng và công bằng.\r\n\n" + 
            "Cảm ơn bạn đã hợp tác và thông cảm.\r\n\n" + 
            "Trân trọng,\r\n\n" +
            "Huỳnh Lê Huy\r\n\n" +
            "Quản trị viên trang web\r\n\n" +
            "Watches Store Website\r\n\n" + 
            "Khoa Công Nghệ Thông Tin, Trường Đại Học Sư Phạm Kỹ Thuật Thành Phố Hồ Chí Minh";
        
        String[] cc = new String[1];
        cc[0] = email;

        try {
            return sendMail(null, email, cc, subject, body);
        } catch (Exception e) {
            return "Send mail failure !!!";
        }
    }

    @Override
    public String deleteUser(String email, String username) {
        String subject = "Thông báo xóa tài khoản";
        String body = "Xin chào "+ username + ",\r\n" + "\r\n" +
            "Chúng tôi nhận thấy có một số hành động bất thường ở tài khoản của bạn khi sử dụng trên hệ thống.\r\n" + 
            "Sau khoảng thời gian chờ đợi mà không có được những phản hồi từ bạn. Để bảo mật thông tin về tài khoản của bạn cũng như tài nguyên của hệ thống. Chúng tôi rất tiếc phải thông báo rằng tài khoản của bạn đã bị xóa.\r\n\n" + 
            "Cảm ơn bạn đã hợp tác và thông cảm.\r\n\n" + 
            "Trân trọng,\r\n\n" +
            "Huỳnh Lê Huy\r\n\n" +
            "Quản trị viên trang web\r\n\n" +
            "Watches Store Website\r\n\n" + 
            "Khoa Công Nghệ Thông Tin, Trường Đại Học Sư Phạm Kỹ Thuật Thành Phố Hồ Chí Minh";
        
        String[] cc = new String[1];
        cc[0] = email;

        try {
            return sendMail(null, email, cc, subject, body);
        } catch (Exception e) {
            return "Send mail failure !!!";
        }
    }

    @Override
    public String unBlockUser(String email, String username) {
        String subject = "Thông báo đã mở khóa tài khoản";
        String body = "Xin chào "+ username + ",\r\n" + "\r\n" +
            "Sau nhận thấy có sự nhầm lẫn trong việc khóa tài khoản của bạn. Chúng tôi xin thông báo tài khoảng của bạn đã được mở khóa. Chúc bạn có những trải nghiệm mua tuyệt vời trong tương lai. \r\n\n" + 
            "Cảm ơn bạn đã hợp tác và thông cảm.\r\n\n" + 
            "Trân trọng,\r\n\n" +
            "Huỳnh Lê Huy\r\n\n" +
            "Quản trị viên trang web\r\n\n" +
            "Watches Store Website\r\n\n" + 
            "Khoa Công Nghệ Thông Tin, Trường Đại Học Sư Phạm Kỹ Thuật Thành Phố Hồ Chí Minh";
        
        String[] cc = new String[1];
        cc[0] = email;

        try {
            return sendMail(null, email, cc, subject, body);
        } catch (Exception e) {
            return "Send mail failure !!!";
        }
    }

    @Override
    public String welcome(String email, String username) {
        String subject = "Thông báo đăng ký tài khoản thành công";
        String body = "Xin chào "+ username + ",\r\n" + "\r\n" +
            "Chúc mừng bạn đã đăng ký thành công tài khoản tại Watches Store!\r\n\n" + 
            "Cảm ơn bạn đã tin tưởng và lựa chọn Watches Store là nơi mua sắm. Chúc bạn có những trải nghiệm mua sắm tuyệt vời tại website của chúng tôi.\r\n\n" + 
            "Trân trọng,\r\n\n" +
            "Huỳnh Lê Huy\r\n\n" +
            "Quản trị viên trang web\r\n\n" +
            "Watches Store Website\r\n\n" + 
            "Khoa Công Nghệ Thông Tin, Trường Đại Học Sư Phạm Kỹ Thuật Thành Phố Hồ Chí Minh";
        
        String[] cc = new String[1];
        cc[0] = email;

        try {
            return sendMail(null, email, cc, subject, body);
        } catch (Exception e) {
            return "Send mail failure !!!";
        }
    }
    
}
