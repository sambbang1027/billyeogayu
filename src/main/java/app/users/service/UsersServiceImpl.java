package app.users.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import app.users.dao.UsersRepository;
import app.users.model.Users;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    UsersRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Users selectUserByLoginId(String loginId) {
        return userRepository.selectUserByLoginId(loginId);
    }

    @Override
    public Users selectUserById(Long userId) {
        return userRepository.selectUserById(userId);
    }

    @Override
    public Users selectUserByEmail(String email) {
        return userRepository.selectUserByEmail(email);
    }


    @Override
    public void registerUser(Users user) throws IllegalArgumentException {
        // 1. 로그인 ID 중복 체크
        if (isLoginIdExists(user.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 로그인 ID입니다.");
        }
        
        // 2. 이메일 중복 체크
        if (isEmailExists(user.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        
        // 3. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        // 4. 기본 역할 설정
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("COMMON");
        }
        
        // 5. 사용자 등록
        userRepository.insertUser(user);
    }

    @Override
    public boolean isLoginIdExists(String loginId) {
        return userRepository.selectUserByLoginId(loginId) != null;
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.selectUserByEmail(email) != null;
    }

    @Override
    public void updateUser(Users user) {
        userRepository.updateUser(user);
    }

    @Override
    public int softDeleteUser(Long userId) {
        return userRepository.softDeleteUser(userId);
    }

    @Override
    public int getUserCount() {
        return userRepository.getUserCount();
    }

    @Override
    public List<Users> selectUsersByRole(String role) {
        return userRepository.selectUsersByRole(role);
    }
}