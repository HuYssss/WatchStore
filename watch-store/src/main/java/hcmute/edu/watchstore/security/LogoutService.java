package hcmute.edu.watchstore.security;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import hcmute.edu.watchstore.entity.AuthToken;
import hcmute.edu.watchstore.entity.User;
import hcmute.edu.watchstore.repository.AuthTokenRepository;
import hcmute.edu.watchstore.repository.UserRepository;
import hcmute.edu.watchstore.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = JwtUtils.extractUsername(token);
        }

        Optional<AuthToken> presentToken = this.authTokenRepository.findByToken(token);
        Optional<User> user = this.userRepository.findByUsername(username);

        if (presentToken.isPresent() && user.isPresent()) {
            presentToken.get().setExpiration(new Date());
            this.authTokenRepository.save(presentToken.get());

            user.get().setState("offline");
            this.userRepository.save(user.get());
        }

    }
    
}
