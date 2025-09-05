package app.users.service;

import java.util.List;

import app.users.model.Users;

public interface UsersService {
    
    /**
     * 로그인 ID로 사용자 조회
     */
    Users selectUserByLoginId(String loginId);
    
    /**
     * 사용자 ID로 조회  
     */
    Users selectUserById(Long userId);
    
    /**
     * 이메일로 사용자 조회
     */
    Users selectUserByEmail(String email);
    
    /**
     * 회원가입
     */
    void registerUser(Users user) throws IllegalArgumentException;
    
    /**
     * 로그인 ID 중복 확인
     */
    boolean isLoginIdExists(String loginId);
    
    /**
     * 이메일 중복 확인
     */
    boolean isEmailExists(String email);
    
    /**
     * 사용자 정보 수정
     */
    void updateUser(Users user);
    
    /**
     * 사용자 논리 삭제
     */
    int softDeleteUser(Long userId);
    
    /**
     * 전체 사용자 수 조회
     */
    int getUserCount();
    
    /**
     * 역할별 사용자 조회
     */
    List<Users> selectUsersByRole(String role);
}