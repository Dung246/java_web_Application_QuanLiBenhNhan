package com.example.java_web_finalprj.config;

import com.example.java_web_finalprj.model.entity.Role;
import com.example.java_web_finalprj.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        System.out.println("\n🔐 AuthInterceptor - URI: " + uri);

        HttpSession session = request.getSession(false); // false để không tạo session mới
        User user = (User) (session != null ? session.getAttribute("loggedInUser") : null);

        System.out.println("   User: " + (user != null ? user.getUsername() + " (" + user.getRole() + ")" : "NULL"));

        // 1. Cho phép truy cập các trang công khai
        if (uri.equals("/login") || uri.equals("/register") ||
                uri.startsWith("/css") || uri.startsWith("/js") ||
                uri.startsWith("/images") || uri.contains(".")) {

            System.out.println("   ✅ Public resource - ALLOW");
            return true;
        }

        // 2. Chưa đăng nhập → bắt login
        if (user == null) {
            System.out.println("   ❌ Not logged in - REDIRECT to /login");
            response.sendRedirect("/login");
            return false;
        }

        // Lấy role dạng String để an toàn hơn
        String role = user.getRole() != null ? user.getRole().name() : "";

        // 3. Kiểm tra quyền truy cập theo Role
        if (uri.startsWith("/admin") && !Role.ADMIN.name().equals(role)) {
            System.out.println("   ❌ Not ADMIN - DENY (403)");
            response.sendError(403, "Access Denied: Only Admin allowed");
            return false;
        }

        if (uri.startsWith("/doctor") && !Role.DOCTOR.name().equals(role)) {
            System.out.println("   ❌ Not DOCTOR - DENY (403)");
            response.sendError(403, "Access Denied: Only Doctor allowed");
            return false;
        }

        if (uri.startsWith("/patient") && !Role.PATIENT.name().equals(role)) {
            System.out.println("   ❌ Not PATIENT - DENY (403)");
            response.sendError(403, "Access Denied: Only Patient allowed");
            return false;
        }

        // 4. Đã xác thực và có quyền
        System.out.println("   ✅ Authorized - ALLOW");
        return true;
    }
}