package com.example.java_web_finalprj.service;

import com.example.java_web_finalprj.model.dto.DoctorCreateDTO;
import com.example.java_web_finalprj.model.dto.UserProfileDTO;
import com.example.java_web_finalprj.model.dto.UserRegisterDTO;
import com.example.java_web_finalprj.model.entity.User;

public interface UserService {
    User register(UserRegisterDTO dto) throws Exception;
    User login(String username, String password);
    void updateProfile(Long userId, UserProfileDTO dto);

    // Hàm mới: Admin cấp tài khoản Bác sĩ
    User createDoctor(DoctorCreateDTO dto) throws Exception;
}