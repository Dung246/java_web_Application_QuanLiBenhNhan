package com.example.java_web_finalprj.repository;

import com.example.java_web_finalprj.model.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface    MedicineRepository extends JpaRepository<Medicine, Long> {
}