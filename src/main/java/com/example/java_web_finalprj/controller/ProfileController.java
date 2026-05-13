package com.example.java_web_finalprj.controller;

import com.example.java_web_finalprj.model.dto.UserProfileDTO;
import com.example.java_web_finalprj.model.entity.User;
import com.example.java_web_finalprj.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;

    @GetMapping
    public String viewProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");

        // Truyền dữ liệu sang giao diện HTML
        model.addAttribute("user", user);

        // ĐÂY LÀ DÒNG THIẾU LÚC NÃY ĐỂ TRỊ LỖI BÁO NULL:
        model.addAttribute("profile", user.getProfile());

        return "profile-view";
    }

    @PostMapping("/update")
    public String update(HttpSession session, UserProfileDTO dto) {
        User user = (User) session.getAttribute("loggedInUser");
        userService.updateProfile(user.getId(), dto);

        // Cập nhật xong thì lưu đè lại thông tin mới vào session để nó hiển thị luôn
        user.getProfile().setFullName(dto.getFullName());
        user.getProfile().setPhone(dto.getPhone());
        user.getProfile().setAddress(dto.getAddress());
        user.getProfile().setDateOfBirth(dto.getDateOfBirth());
        session.setAttribute("loggedInUser", user);

        return "redirect:/profile?updated";
    }
}