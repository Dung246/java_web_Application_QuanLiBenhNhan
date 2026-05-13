package com.example.java_web_finalprj.controller.admin;

import com.example.java_web_finalprj.model.dto.MedicineDTO;
import com.example.java_web_finalprj.model.entity.Medicine;
import com.example.java_web_finalprj.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/medicines")
@RequiredArgsConstructor
public class AdminMedicineController {

    private final MedicineRepository medicineRepo;

    @GetMapping
    public String manageMedicines(Model model) {
        model.addAttribute("medicines", medicineRepo.findAll());
        model.addAttribute("dto", new MedicineDTO());
        return "admin/medicine-manage";
    }

    @PostMapping("/save")
    public String saveMedicine(@ModelAttribute("dto") MedicineDTO dto, Model model) {
        boolean hasError = false;

        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            model.addAttribute("errorName", "Vui lòng nhập tên thuốc!");
            hasError = true;
        }
        if (dto.getUnit() == null || dto.getUnit().trim().isEmpty()) {
            model.addAttribute("errorUnit", "Vui lòng nhập đơn vị!");
            hasError = true;
        }

        if (hasError) {
            model.addAttribute("medicines", medicineRepo.findAll());
            return "admin/medicine-manage";
        }

        Medicine medicine;
        if (dto.getId() != null) {
            medicine = medicineRepo.findById(dto.getId()).orElse(new Medicine());
        } else {
            medicine = new Medicine();
        }
        medicine.setName(dto.getName());
        medicine.setUnit(dto.getUnit());
        medicine.setDescription(dto.getDescription());

        medicineRepo.save(medicine);
        return "redirect:/admin/medicines?success";
    }

    @GetMapping("/delete/{id}")
    public String deleteMedicine(@PathVariable Long id) {
        medicineRepo.deleteById(id);
        return "redirect:/admin/medicines?deleted";
    }
}