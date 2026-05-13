package com.example.java_web_finalprj.service;

import com.example.java_web_finalprj.model.entity.Medicine;
import java.util.List;

public interface MedicineService {
    List<Medicine> getAll();
    void save(Medicine medicine);
    void delete(Long id);
}