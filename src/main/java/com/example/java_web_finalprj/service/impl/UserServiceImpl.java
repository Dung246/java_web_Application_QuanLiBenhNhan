package com.example.java_web_finalprj.service.impl;

import com.example.java_web_finalprj.model.dto.DoctorCreateDTO;
import com.example.java_web_finalprj.model.dto.UserProfileDTO;
import com.example.java_web_finalprj.model.dto.UserRegisterDTO;
import com.example.java_web_finalprj.model.entity.Role;
import com.example.java_web_finalprj.model.entity.User;
import com.example.java_web_finalprj.model.entity.UserProfile;
import com.example.java_web_finalprj.repository.SpecialtyRepository;
import com.example.java_web_finalprj.repository.UserRepository;
import com.example.java_web_finalprj.service.UserService;
import com.example.java_web_finalprj.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SpecialtyRepository specialtyRepository;

    @Override
    @Transactional
    public User register(UserRegisterDTO dto) throws Exception {
        String username = dto.getUsername().trim();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new Exception("Tên đăng nhập đã tồn tại!");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(HashUtil.hashPassword(dto.getPassword()));
        user.setRole(Role.PATIENT);
        user.setEnabled(true);

        UserProfile profile = new UserProfile();
        profile.setFullName(dto.getFullName());
        profile.setGender(dto.getGender());
        profile.setUser(user);
        user.setProfile(profile);

        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password) {
        if (username == null || password == null) {
            System.out.println("❌ Username hoặc password bị null");
            return null;
        }

        String trimmedUsername = username.trim();
        User user = userRepository.findByUsername(trimmedUsername).orElse(null);

        if (user == null) {
            System.out.println("❌ Không tìm thấy username: " + trimmedUsername);
            return null;
        }

        String hashedInput = HashUtil.hashPassword(password);
        String dbPassword = user.getPassword();

        System.out.println("=====================================");
        System.out.println("🔑 ĐANG ĐĂNG NHẬP: " + trimmedUsername);
        System.out.println("🔑 Hash nhập vào : " + hashedInput);
        System.out.println("🔑 Hash trong DB : " + dbPassword);
        System.out.println("🔑 Role: " + user.getRole());
        System.out.println("=====================================");

        if (hashedInput != null && hashedInput.equals(dbPassword)) {
            System.out.println("✅ ĐĂNG NHẬP THÀNH CÔNG: " + trimmedUsername + " (" + user.getRole() + ")");
            return user;
        } else {
            System.out.println("❌ SAI MẬT KHẨU!");
            return null;
        }
    }

    @Override
    @Transactional
    public void updateProfile(Long userId, UserProfileDTO dto) {
        User user = userRepository.findById(userId).orElseThrow();
        UserProfile profile = user.getProfile();

        if (profile != null) {
            profile.setFullName(dto.getFullName());
            profile.setPhone(dto.getPhone());
            profile.setAddress(dto.getAddress());
            profile.setDateOfBirth(dto.getDateOfBirth());
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User createDoctor(DoctorCreateDTO dto) throws Exception {
        String username = dto.getUsername().trim();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new Exception("Tên đăng nhập đã tồn tại!");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(HashUtil.hashPassword(dto.getPassword()));
        user.setRole(Role.DOCTOR);
        user.setEnabled(true);

        if (dto.getSpecialtyId() != null) {
            user.setSpecialty(specialtyRepository.findById(dto.getSpecialtyId()).orElse(null));
        }

        UserProfile profile = new UserProfile();
        profile.setFullName(dto.getFullName());
        profile.setGender(dto.getGender());
        profile.setEmail(dto.getEmail());
        profile.setPhone(dto.getPhone());
        profile.setUser(user);
        user.setProfile(profile);

        return userRepository.save(user);
    }
}