package app.users.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.users.dao.UsersRepository;
import app.users.model.Users;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Users getUserByLoginId(String loginId) {
        log.debug("사용자 조회 시작 - loginId: {}", loginId);
        Users user = userRepository.selectUserByLoginId(loginId);
        log.debug("사용자 조회 결과 - loginId: {}, found: {}", loginId, user != null);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public Users getUserById(Long userId) {
        log.debug("사용자 조회 시작 - userId: {}", userId);
        return userRepository.selectUserById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Users getUserByEmail(String email) {
        log.debug("사용자 조회 시작 - email: {}", email);
        return userRepository.selectUserByEmail(email);
    }

    @Override
    public Users createUser(Users user) throws IllegalArgumentException {
        log.info("사용자 생성 시작 - loginId: {}", user.getLoginId());
        
        try {
            // 1. 입력 데이터 검증
            validateUserData(user);
            
            // 2. 중복 체크
            checkDuplicateUser(user);
            
            // 3. 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            
            // 4. 기본 역할 설정
            if (user.getRole() == null || user.getRole().trim().isEmpty()) {
                user.setRole("USER"); // Spring Security 표준: ROLE_USER
            }
            
            // 5. 사용자 등록
            int result = userRepository.insertUser(user);
            if (result <= 0) {
                throw new IllegalArgumentException("사용자 등록에 실패했습니다.");
            }
            
            log.info("사용자 생성 완료 - userId: {}, loginId: {}", user.getUserId(), user.getLoginId());
            
            // 비밀번호 제거 후 반환
            user.setPassword(null);
            return user;
            
        } catch (IllegalArgumentException e) {
            log.warn("사용자 생성 실패 - loginId: {}, reason: {}", user.getLoginId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("사용자 생성 중 오류 발생 - loginId: {}", user.getLoginId(), e);
            throw new IllegalArgumentException("사용자 생성 중 오류가 발생했습니다.");
        }
    }

    @Override
    public void registerUser(Users user) throws IllegalArgumentException {
        createUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLoginIdExists(String loginId) {
        return userRepository.selectUserByLoginId(loginId) != null;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailExists(String email) {
        return userRepository.selectUserByEmail(email) != null;
    }

    @Override
    public boolean authenticateUser(String loginId, String password) {
        try {
            Users user = getUserByLoginId(loginId);
            if (user == null) {
                log.debug("사용자 인증 실패 - 사용자 없음: {}", loginId);
                return false;
            }
            
            boolean matches = passwordEncoder.matches(password, user.getPassword());
            log.debug("사용자 인증 결과 - loginId: {}, success: {}", loginId, matches);
            return matches;
            
        } catch (Exception e) {
            log.error("사용자 인증 중 오류 발생 - loginId: {}", loginId, e);
            return false;
        }
    }

    @Override
    public void updateUser(Users user) {
        log.info("사용자 정보 수정 시작 - userId: {}", user.getUserId());
        
        try {
            // 비밀번호가 변경되는 경우 암호화
            if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
            }
            
            int result = userRepository.updateUser(user);
            if (result <= 0) {
                throw new IllegalArgumentException("사용자 정보 수정에 실패했습니다.");
            }
            
            log.info("사용자 정보 수정 완료 - userId: {}", user.getUserId());
            
        } catch (Exception e) {
            log.error("사용자 정보 수정 중 오류 발생 - userId: {}", user.getUserId(), e);
            throw new IllegalArgumentException("사용자 정보 수정 중 오류가 발생했습니다.");
        }
    }

    @Override
    public int softDeleteUser(Long userId) {
        log.info("사용자 삭제 시작 - userId: {}", userId);
        int result = userRepository.softDeleteUser(userId);
        log.info("사용자 삭제 완료 - userId: {}, result: {}", userId, result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public int getUserCount() {
        return userRepository.getUserCount();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Users> getUsersByRole(String role) {
        log.debug("역할별 사용자 조회 - role: {}", role);
        return userRepository.selectUsersByRole(role);
    }

    /**
     * 사용자 데이터 검증
     */
    private void validateUserData(Users user) {
        if (user.getLoginId() == null || user.getLoginId().trim().isEmpty()) {
            throw new IllegalArgumentException("로그인 ID는 필수입니다.");
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
        
        if (user.getPassword().length() < 6) {
            throw new IllegalArgumentException("비밀번호는 6자 이상이어야 합니다.");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
        
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }
        
        // 이메일 형식 검증
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!user.getEmail().matches(emailRegex)) {
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
        }
        
        // 로그인 ID 형식 검증 (영문, 숫자, 언더스코어만 허용)
        String loginIdRegex = "^[a-zA-Z0-9_]{4,20}$";
        if (!user.getLoginId().matches(loginIdRegex)) {
            throw new IllegalArgumentException("로그인 ID는 4-20자의 영문, 숫자, 언더스코어만 사용 가능합니다.");
        }
    }

    /**
     * 중복 사용자 확인
     */
    private void checkDuplicateUser(Users user) {
        // 로그인 ID 중복 체크
        if (isLoginIdExists(user.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 로그인 ID입니다.");
        }

        // 이메일 중복 체크
        if (isEmailExists(user.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
    }
}