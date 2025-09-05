package app.users.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import app.users.dao.UsersRepository;
import app.users.model.Users;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        // 데이터베이스에서 사용자 정보 조회
        Users user = userRepository.selectUserByLoginId(loginId);
        
        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + loginId);
        }

        // 사용자 권한 설정
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        
        // ROLE_ 접두사가 없으면 추가 (Spring Security 규칙)
        String role = user.getRole();
        if (role != null && !role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }
        authorities.add(new SimpleGrantedAuthority(role));

        // CustomUserDetails 객체 반환
        return new app.users.auth.CustomUserDetails(
                user.getLoginId(),
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                !"Y".equals(user.getIsDeleted()), // 삭제된 사용자가 아니면 활성화
                authorities
        );
    }
}