package hcmute.edu.watchstore.util;

import java.util.HashMap;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public class JwtUtils {
    private static final String SECRET = "hcmutewatchstorejwtsecrethuynhlehuynguyenduchuyspringsecurity";


    // Trích xuất tên người dùng từ JWT.
    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Trích xuất ngày hết hạn của JWT.
    public static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Xác thực JWT.
    public static Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Tạo JWT mới.
    public static String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    // Tạo JWT với claims và tên người dùng.
    private static String createToken(Map<String, Object> claims, String userName) {
        long currentTime = System.currentTimeMillis();
        long expiredTimeInMillis = 3600 * 1_000L; // 1 giờ
        return Jwts.builder().setClaims(claims).setSubject(userName).setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + expiredTimeInMillis))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    // Lấy khóa ký từ chuỗi bí mật.
    private static Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Trích xuất một claim cụ thể từ JWT.
    private static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Trích xuất tất cả các claims từ JWT.
    private static Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    // Kiểm tra token đã hết hạn hay chưa.
    private static Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
}
