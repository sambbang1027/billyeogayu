package app.users.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import app.users.model.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {

    /**
     * JWT 서명에 사용할 비밀키
     */
    private static final String SECRET_KEY = "mySecretKeyForJWT1234567890123456789012345678901234567890";
    
    /**
     * 토큰 유효기간: 30분 (밀리초)
     */
    private long tokenValidTime = 30 * 60 * 1000L;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    /**
     * JWT 서명을 위한 SecretKey 생성
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * JWT 토큰 생성
     * @param user 사용자 정보
     * @return 생성된 JWT 토큰
     */
    public String generateToken(Users user) {
        long now = System.currentTimeMillis();
        Claims claims = Jwts.claims()
                .setSubject(user.getLoginId())     // sub: 사용자 ID
                .setIssuer(user.getName())         // iss: 발행자 (사용자 이름)
                .setIssuedAt(new Date(now))        // iat: 발행 시간
                .setExpiration(new Date(now + tokenValidTime)); // exp: 만료 시간
        
        claims.put("roles", user.getRole());       // 사용자 역할 추가
        
        return Jwts.builder()
                .setClaims(claims)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Request의 Header에서 토큰 값 가져오기
     * @param request 요청 객체
     * @return 토큰 값
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }
    
    /**
     * 토큰에서 사용자 ID 추출
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    /**
     * JWT 토큰에서 인증 정보 조회
     * @param token JWT 토큰
     * @return Authentication 객체
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    
    /**
     * 토큰 유효성 및 만료일자 확인
     * @param token JWT 토큰
     * @return 유효하면 true
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}