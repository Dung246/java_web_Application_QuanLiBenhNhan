package com.example.java_web_finalprj.model.dto;

import lombok.Data;

@Data
public class DoctorCreateDTO {
    private String fullName;
    private String gender;
    private String email;
    private String phone;
    private Long specialtyId; // Quan trọng để AdminController đọc được ID
    private String username;
    private String password;
}