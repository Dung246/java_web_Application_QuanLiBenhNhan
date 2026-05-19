package com.example.java_web_finalprj.controller;

import com.example.java_web_finalprj.model.dto.UserRegisterDTO;
import com.example.java_web_finalprj.model.entity.Role;
import com.example.java_web_finalprj.model.entity.User;
import com.example.java_web_finalprj.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // ====================== LOGIN ======================
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam(value = "username", defaultValue = "") String username,
                              @RequestParam(value = "password", defaultValue = "") String password,
                              Model model, HttpSession session) {

        // 1. Validate input
        boolean hasError = false;

        if (username.trim().isEmpty()) {
            model.addAttribute("usernameError", "Vui lòng nhập tên đăng nhập!");
            hasError = true;
        }
        if (password.trim().isEmpty()) {
            model.addAttribute("passwordError", "Vui lòng nhập mật khẩu!");
            hasError = true;
        }

        if (hasError) {
            model.addAttribute("oldUsername", username);
            return "login";
        }

        // 2. Xử lý đăng nhập
        User user = userService.login(username, password);

        if (user != null && user.getRole() != null) {
            // Lưu user vào session
            session.setAttribute("loggedInUser", user);

            System.out.println("✅ Đăng nhập thành công: " + user.getUsername() + " - Role: " + user.getRole());

            // 3. Redirect theo Role (An toàn - dùng String)
            switch (user.getRole()) {
                case ADMIN:
                    return "redirect:/admin/dashboard";
                case DOCTOR:
                    return "redirect:/doctor/pending-appointments";   // Trang mặc định của bác sĩ
                case PATIENT:
                    return "redirect:/patient/dashboard";
                default:
                    return "redirect:/login?error=unknown_role";
            }
        }

        // Đăng nhập thất bại
        model.addAttribute("loginError", "Tên đăng nhập hoặc mật khẩu không đúng!");
        model.addAttribute("oldUsername", username);
        return "login";
    }

    // ====================== REGISTER ======================
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("dto", new UserRegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("dto") UserRegisterDTO dto, Model model) {
        boolean hasError = false;

        // Validate Full Name
        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) {
            model.addAttribute("nameError", "Họ và tên không được để trống!");
            hasError = true;
        } else if (dto.getFullName().trim().length() < 6) {
            model.addAttribute("nameError", "Họ và tên phải có ít nhất 6 ký tự!");
            hasError = true;
        }

        // Validate Gender
        if (dto.getGender() == null || dto.getGender().isEmpty()) {
            model.addAttribute("genderError", "Vui lòng chọn giới tính!");
            hasError = true;
        }

        // Validate Username
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            model.addAttribute("usernameError", "Tên đăng nhập không được để trống!");
            hasError = true;
        } else if (dto.getUsername().trim().length() < 3) {
            model.addAttribute("usernameError", "Tên đăng nhập phải có ít nhất 3 ký tự!");
            hasError = true;
        }

        // Validate Password
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            model.addAttribute("passwordError", "Mật khẩu không được để trống!");
            hasError = true;
        } else if (dto.getPassword().trim().length() < 6) {
            model.addAttribute("passwordError", "Mật khẩu phải chứa ít nhất 6 ký tự!");
            hasError = true;
        }

        // Validate Confirm Password
        if (dto.getConfirmPassword() == null || dto.getConfirmPassword().trim().isEmpty()) {
            model.addAttribute("confirmError", "Vui lòng nhập lại mật khẩu!");
            hasError = true;
        } else if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("confirmError", "Mật khẩu xác nhận không khớp!");
            hasError = true;
        }

        if (hasError) {
            return "register";
        }

        try {
            userService.register(dto);
            return "redirect:/login?success=true";
        } catch (Exception e) {
            model.addAttribute("usernameError", e.getMessage());
            return "register";
        }
    }

    // ====================== LOGOUT ======================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}