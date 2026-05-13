package com.example.java_web_finalprj.controller;

import com.example.java_web_finalprj.model.dto.DoctorCreateDTO;
import com.example.java_web_finalprj.model.entity.Role;
import com.example.java_web_finalprj.model.entity.User;
import com.example.java_web_finalprj.repository.AppointmentRepository;
import com.example.java_web_finalprj.repository.SpecialtyRepository;
import com.example.java_web_finalprj.repository.UserRepository;
import com.example.java_web_finalprj.service.ExaminationService;
import com.example.java_web_finalprj.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final UserRepository userRepo;
    private final SpecialtyRepository specialtyRepo;
    private final AppointmentRepository appointmentRepo;
    private final ExaminationService examService;

    @GetMapping("/dashboard")
    public String dashboard() { return "admin-dashboard"; }

    // --- QUẢN LÝ BÁC SĨ ---
    @GetMapping("/doctors")
    public String listDoctors(Model model) {
        model.addAttribute("doctors", userRepo.findByRole(Role.DOCTOR));
        return "admin/doctor-manage";
    }

    @GetMapping("/doctor/create")
    public String showCreateDoctorForm(Model model) {
        model.addAttribute("dto", new DoctorCreateDTO());
        model.addAttribute("specialties", specialtyRepo.findAll());
        return "admin/create-doctor";
    }

    @PostMapping("/doctor/create")
    public String createDoctor(@ModelAttribute("dto") DoctorCreateDTO dto, Model model) {
        boolean hasError = false;

        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) {
            model.addAttribute("nameError", "Họ và tên không được để trống!"); hasError = true;
        }
        if (dto.getGender() == null || dto.getGender().isEmpty()) {
            model.addAttribute("genderError", "Vui lòng chọn giới tính!"); hasError = true;
        }
        if (dto.getSpecialtyId() == null) {
            model.addAttribute("specialtyError", "Vui lòng chọn chuyên khoa!"); hasError = true;
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty() || !dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            model.addAttribute("emailError", "Email không đúng định dạng!"); hasError = true;
        }
        if (dto.getPhone() == null || !dto.getPhone().matches("\\d{10,11}")) {
            model.addAttribute("phoneError", "Số điện thoại phải từ 10-11 chữ số!"); hasError = true;
        }
        if (dto.getUsername() == null || dto.getUsername().trim().length() < 3) {
            model.addAttribute("usernameError", "Tên đăng nhập phải có ít nhất 3 ký tự!"); hasError = true;
        }
        if (dto.getPassword() == null || dto.getPassword().trim().length() < 6) {
            model.addAttribute("passwordError", "Mật khẩu phải từ 6 ký tự trở lên!"); hasError = true;
        }

        if (hasError) {
            model.addAttribute("specialties", specialtyRepo.findAll());
            return "admin/create-doctor";
        }

        try {
            userService.createDoctor(dto);
            return "redirect:/admin/doctors?success=created";
        } catch (Exception e) {
            model.addAttribute("usernameError", e.getMessage());
            model.addAttribute("specialties", specialtyRepo.findAll());
            return "admin/create-doctor";
        }
    }

    @GetMapping("/doctor/toggle-status/{id}")
    public String toggleDoctorStatus(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow();
        user.setEnabled(!user.isEnabled());
        userRepo.save(user);
        return "redirect:/admin/doctors";
    }

    // --- QUẢN LÝ BỆNH NHÂN ---
    @GetMapping("/patients")
    public String listPatients(Model model) {
        model.addAttribute("patients", userRepo.findByRole(Role.PATIENT));
        return "admin/patient-manage";
    }

    @GetMapping("/patients/{id}")
    public String viewPatientDetails(@PathVariable Long id, Model model) {
        User patient = userRepo.findById(id).orElseThrow();
        model.addAttribute("patient", patient);
        model.addAttribute("appointments", appointmentRepo.findByPatientIdOrderByAppointmentDateDesc(id));
        return "admin/patient-details";
    }

    // --- QUẢN LÝ CẤP PHÁT THUỐC ---
    @GetMapping("/prescriptions")
    public String listPrescriptions(Model model) {
        model.addAttribute("pendingRecords", examService.getPendingPrescriptions());
        model.addAttribute("historyRecords", examService.getDispensedPrescriptions());
        return "admin/dispense-manage";
    }

    @GetMapping("/prescriptions/dispense/{id}")
    public String dispenseMedicine(@PathVariable Long id) {
        examService.dispenseMedicine(id);
        return "redirect:/admin/prescriptions?success=dispensed";
    }
}