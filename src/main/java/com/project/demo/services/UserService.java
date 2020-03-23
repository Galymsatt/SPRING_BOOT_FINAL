package com.project.demo.services;

import com.project.demo.entities.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    public Users registerUser(Users user);
    public Users getAuthonticatedUser();
    public Users getUserById(Long id);
    public String refreshData(Long id, String act, String email, String password, String newPassword, String confirmNewPassword, String name, String surName);
    public String refreshName(Long id, String name);
    public String refreshSurName(Long id, String surName);
    public String refreshPassword(Long id, String oldPass, String newPass, String confirmNewPass);
    public String refreshPassword(Long id, String newPass);
    public String register(String email, String password, String re_password, String name, String surName);
    public String blockUser(Long id);
    public List<Users> getAllUsers();

    }
