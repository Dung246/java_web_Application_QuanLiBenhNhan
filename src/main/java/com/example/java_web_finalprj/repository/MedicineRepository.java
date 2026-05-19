package com.example.java_web_finalprj.repository;

import com.example.java_web_finalprj.model.entity.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    // THÊM DÒNG NÀY ĐỂ TÌM THUỐC BẰNG TÊN
    Optional<Medicine> findByName(String name);

}