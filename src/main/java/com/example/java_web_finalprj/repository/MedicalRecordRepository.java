package com.example.java_web_finalprj.repository;

import com.example.java_web_finalprj.model.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    // Tìm danh sách đơn thuốc theo trạng thái (Dùng cho Admin quầy thuốc)
    List<MedicalRecord> findByDispenseStatus(String dispenseStatus);
}