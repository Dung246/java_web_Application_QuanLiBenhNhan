package com.example.java_web_finalprj.service.impl;

import com.example.java_web_finalprj.model.entity.Medicine;
import com.example.java_web_finalprj.repository.MedicineRepository;
import com.example.java_web_finalprj.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepo;

    @Override
    public List<Medicine> getAll() {
        return medicineRepo.findAll();
    }

    @Override
    public void save(Medicine medicine) {
        medicineRepo.save(medicine);
    }

    @Override
    public void delete(Long id) {
        medicineRepo.deleteById(id);
    }
}