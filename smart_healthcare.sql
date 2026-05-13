
DROP DATABASE IF EXISTS smart_healthcare;
CREATE DATABASE smart_healthcare CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE smart_healthcare;

-- =============================
-- 1. USERS TABLE
-- =============================
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    gender VARCHAR(10),
    role ENUM('ADMIN','DOCTOR','PATIENT') NOT NULL DEFAULT 'PATIENT',
    enabled BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- =============================
-- 2. SPECIALTIES TABLE (Chuyên khoa)
-- =============================
CREATE TABLE specialties (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO specialties(name) VALUES
('Nội khoa'),
('Ngoại khoa'),
('Tai Mũi Họng'),
('Da liễu'),
('Tim mạch');

-- =============================
-- 3. DOCTORS TABLE
-- =============================
CREATE TABLE doctors (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    specialty_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (specialty_id) REFERENCES specialties(id)
);

-- =============================
-- 4. MEDICINES TABLE (Thuốc)
-- =============================
CREATE TABLE medicines (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    stock INT DEFAULT 0,
    price DOUBLE,
    unit VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- =============================
-- 5. APPOINTMENTS TABLE (Lịch hẹn khám)
-- =============================
CREATE TABLE appointments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    appointment_time DATETIME NOT NULL,
    status ENUM('PENDING','CONFIRMED','COMPLETED','CANCELLED') DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_doctor_time (doctor_id, appointment_time),
    FOREIGN KEY (patient_id) REFERENCES users(id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

-- =============================
-- 6. MEDICAL RECORDS TABLE (Hồ sơ bệnh án)
-- =============================
CREATE TABLE medical_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    appointment_id BIGINT NOT NULL,
    symptoms TEXT,
    diagnosis TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id)
);

-- =============================
-- 7. PRESCRIPTIONS TABLE (Đơn thuốc)
-- =============================
CREATE TABLE prescriptions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    medical_record_id BIGINT NOT NULL,
    status ENUM('PENDING','DISPENSED') DEFAULT 'PENDING',
    FOREIGN KEY (medical_record_id) REFERENCES medical_records(id)
);

-- =============================
-- 8. PRESCRIPTION DETAILS TABLE
-- =============================
CREATE TABLE prescription_details (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    prescription_id BIGINT NOT NULL,
    medicine_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (prescription_id) REFERENCES prescriptions(id),
    FOREIGN KEY (medicine_id) REFERENCES medicines(id)
);

-- =============================
-- 9. SEED USERS (Password: 123456)
-- =============================
INSERT INTO users(username, email, phone, password, full_name, gender, role) VALUES
('admin', 'admin@gmail.com', '0900000000', '$2a$10$slYQmyNdGzin7olVyOh4V.WMeknLCrVM/p1N.MBQ97LcLSIXMvhGu', 'Admin System', 'Male', 'ADMIN'),
('doctor1', 'doctor1@gmail.com', '0901111111', '$2a$10$slYQmyNdGzin7olVyOh4V.WMeknLCrVM/p1N.MBQ97LcLSIXMvhGu', 'Dr. Nguyễn Văn A', 'Male', 'DOCTOR'),
('doctor2', 'doctor2@gmail.com', '0902222222', '$2a$10$slYQmyNdGzin7olVyOh4V.WMeknLCrVM/p1N.MBQ97LcLSIXMvhGu', 'Dr. Trần Thị B', 'Female', 'DOCTOR'),
('doctor3', 'doctor3@gmail.com', '0903333333', '$2a$10$slYQmyNdGzin7olVyOh4V.WMeknLCrVM/p1N.MBQ97LcLSIXMvhGu', 'Dr. Lê Văn C', 'Male', 'DOCTOR'),
('patient1', 'p1@gmail.com', '0910000001', '$2a$10$slYQmyNdGzin7olVyOh4V.WMeknLCrVM/p1N.MBQ97LcLSIXMvhGu', 'Phạm Văn D', 'Male', 'PATIENT'),
('patient2', 'p2@gmail.com', '0910000002', '$2a$10$slYQmyNdGzin7olVyOh4V.WMeknLCrVM/p1N.MBQ97LcLSIXMvhGu', 'Vũ Thị E', 'Female', 'PATIENT'),
('patient3', 'p3@gmail.com', '0910000003', '$2a$10$slYQmyNdGzin7olVyOh4V.WMeknLCrVM/p1N.MBQ97LcLSIXMvhGu', 'Đặng Văn F', 'Male', 'PATIENT'),
('patient4', 'p4@gmail.com', '0910000004', '$2a$10$slYQmyNdGzin7olVyOh4V.WMeknLCrVM/p1N.MBQ97LcLSIXMvhGu', 'Hoàng Thị G', 'Female', 'PATIENT'),
('patient5', 'p5@gmail.com', '0910000005', '$2a$10$slYQmyNdGzin7olVyOh4V.WMeknLCrVM/p1N.MBQ97LcLSIXMvhGu', 'Bùi Văn H', 'Male', 'PATIENT'),
('patient6', 'p6@gmail.com', '0910000006', '$2a$10$slYQmyNdGzin7olVyOh4V.WMeknLCrVM/p1N.MBQ97LcLSIXMvhGu', 'Tạ Thị I', 'Female', 'PATIENT'),
('patient7', 'p7@gmail.com', '0910000007', '$2a$10$slYQmyNdGzin7olVyOh4V.WMeknLCrVM/p1N.MBQ97LcLSIXMvhGu', 'Nông Văn J', 'Male', 'PATIENT'),
('patient8', 'p8@gmail.com', '0910000008', '$2a$10$slYQmyNdGzin7olVyOh4V.WMeknLCrVM/p1N.MBQ97LcLSIXMvhGu', 'Cát Thị K', 'Female', 'PATIENT'),
('patient9', 'p9@gmail.com', '0910000009', '$2a$10$slYQmyNdGzin7olVyOh4V.WMeknLCrVM/p1N.MBQ97LcLSIXMvhGu', 'Khương Văn L', 'Male', 'PATIENT'),
('patient10', 'p10@gmail.com', '0910000010', '$2a$10$slYQmyNdGzin7olVyOh4V.WMeknLCrVM/p1N.MBQ97LcLSIXMvhGu', 'Trịnh Thị M', 'Female', 'PATIENT');

-- =============================
-- 10. SEED DOCTORS
-- =============================
INSERT INTO doctors(user_id, specialty_id) VALUES
(2, 1),  -- doctor1 -> Nội khoa
(3, 2),  -- doctor2 -> Ngoại khoa
(4, 3);  -- doctor3 -> Tai Mũi Họng

-- =============================
-- 11. SEED MEDICINES
-- =============================
INSERT INTO medicines(name, description, stock, price, unit) VALUES
('Paracetamol', 'Hạ sốt, giảm đau', 100, 5000, 'viên'),
('Amoxicillin', 'Kháng sinh', 50, 10000, 'viên'),
('Vitamin C', 'Tăng cường miễn dịch', 200, 3000, 'viên'),
('Ibuprofen', 'Giảm đau, hạ sốt', 80, 7000, 'viên'),
('Aspirin', 'Giảm đau, hạ sốt', 60, 4000, 'viên'),
('Cephalexin', 'Kháng sinh', 40, 12000, 'viên');