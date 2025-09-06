package app.users.service;

import java.util.List;

import app.users.model.Users;

public interface UsersService {

    /**
     * 로그인 ID로 사용자 조회 (세션 기반 로그인용)
     */
    Users getUserByLoginId(String loginId);

    /**
     * 사용자 ID로 조회
     */
    Users getUserById(Long userId);

    /**
     * 이메일로 사용자 조회
     */
    Users getUserByEmail(String email);

    /**
     * 회원가입 (사용자 생성)
     */
    Users createUser(Users user) throws IllegalArgumentException;

    /**
     * 회원가입 (기존 호환성)
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
    List<Users> getUsersByRole(String role);

    /**
     * 사용자 인증 확인 (세션 기반용)
     */
    boolean authenticateUser(String loginId, String password);
}