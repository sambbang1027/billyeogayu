package app.users.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.users.dao.UsersRepository;
import app.users.model.Users;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    UsersRepository userRepository;

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
    public void insertUser(Users user) {
        userRepository.insertUser(user);
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