drop database if exists healthcare_db;
-- 1. KHỞI TẠO DATABASE
CREATE DATABASE IF NOT EXISTS healthcare_db;
USE healthcare_db;

-- 2. DỌN DẸP DỮ LIỆU CŨ (Xóa theo thứ tự để tránh lỗi khóa ngoại)
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS medical_records;
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS user_profiles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS medicines;
DROP TABLE IF EXISTS specialties;
SET FOREIGN_KEY_CHECKS = 1;


-- =======================================================
-- 3. TẠO CẤU TRÚC CÁC BẢNG (TABLE SCHEMA)
-- =======================================================

-- Bảng Chuyên khoa (CÓ CỘT PRICE ĐỂ TÍNH DOANH THU)
CREATE TABLE specialties (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DOUBLE DEFAULT 0 
);

-- Bảng Danh mục thuốc (CORE-04)
CREATE TABLE medicines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    description TEXT
);

-- Bảng Tài khoản (Hỗ trợ khóa enabled và lọc chuyên khoa)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,            -- ADMIN, DOCTOR, PATIENT
    enabled BOOLEAN DEFAULT TRUE,         -- Trạng thái tài khoản (Day 3)
    specialty_id BIGINT,                  -- Chuyên khoa của Bác sĩ (Day 3)
    FOREIGN KEY (specialty_id) REFERENCES specialties(id) ON DELETE SET NULL
);

-- Bảng Hồ sơ chi tiết
CREATE TABLE user_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    gender VARCHAR(10),
    date_of_birth DATE,
    phone VARCHAR(20),
    email VARCHAR(255),
    address TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Bảng Lịch hẹn (CORE-09 Hủy lịch)
CREATE TABLE appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    specialty_id BIGINT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING', -- PENDING, COMPLETED, CANCELLED
    FOREIGN KEY (patient_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (specialty_id) REFERENCES specialties(id) ON DELETE CASCADE
);

-- Bảng Bệnh án & Cấp thuốc (CORE-08)
CREATE TABLE medical_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL UNIQUE,
    symptoms TEXT,
    diagnosis TEXT,
    prescription TEXT,
    dispense_status VARCHAR(50) DEFAULT 'PENDING', -- PENDING, DISPENSED
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE
);

-- =======================================================
-- 4. BƠM DỮ LIỆU MẪU (SEED DATA)
-- =======================================================

-- Chèn Chuyên khoa (ĐÃ CẬP NHẬT GIÁ TIỀN ĐỂ TÍNH DOANH THU)
INSERT INTO specialties (id, name, description, price) VALUES
(1, 'Nội tổng quát', 'Khám nội khoa chung', 200000),
(2, 'Nhi khoa', 'Khám nhi và tư vấn dinh dưỡng', 300000),
(3, 'Tai Mũi Họng', 'Điều trị các bệnh vùng họng, tai, mũi', 250000),
(4, 'Răng Hàm Mặt', 'Chăm sóc răng miệng chuyên sâu', 500000),
(5, 'Da liễu', 'Điều trị các bệnh về da', 350000);

-- Chèn Thuốc (10 loại)
INSERT INTO medicines (id, name, unit, description) VALUES
(1, 'Paracetamol 500mg', 'Viên', 'Giảm đau hạ sốt'),
(2, 'Amoxicillin 250mg', 'Viên', 'Kháng sinh'),
(3, 'Vitamin C 1000mg', 'Viên sủi', 'Tăng đề kháng'),
(4, 'Ibuprofen 400mg', 'Viên', 'Kháng viêm'),
(5, 'Omeprazole 20mg', 'Vỉ', 'Hỗ trợ dạ dày'),
(6, 'Oresol', 'Gói', 'Bù nước'),
(7, 'Panadol Extra', 'Vỉ', 'Giảm đau đầu'),
(8, 'Salbutamol', 'Chai', 'Xịt hen suyễn'),
(9, 'Metformin', 'Viên', 'Tiểu đường'),
(10, 'Losartan', 'Viên', 'Huyết áp');

-- ⭐ CHÈN USERS VỚI PASSWORD SHA-256 ĐÚNG NGAY TỪ ĐẦU
-- SHA-256("123456") = KWRwqHaQ1X0q1L5YPWRVvhPIkxnrgwEOWfZ4XNpyf7E=
INSERT INTO users (id, username, password, role, enabled, specialty_id) VALUES
(1, 'admin', 'KWRwqHaQ1X0q1L5YPWRVvhPIkxnrgwEOWfZ4XNpyf7E=', 'ADMIN', 1, NULL),
(2, 'bs_luc', 'KWRwqHaQ1X0q1L5YPWRVvhPIkxnrgwEOWfZ4XNpyf7E=', 'DOCTOR', 1, 1),
(3, 'bs_mai', 'KWRwqHaQ1X0q1L5YPWRVvhPIkxnrgwEOWfZ4XNpyf7E=', 'DOCTOR', 1, 2),
(4, 'bs_thach', 'KWRwqHaQ1X0q1L5YPWRVvhPIkxnrgwEOWfZ4XNpyf7E=', 'DOCTOR', 1, 3),
(5, 'tienthanh', 'KWRwqHaQ1X0q1L5YPWRVvhPIkxnrgwEOWfZ4XNpyf7E=', 'PATIENT', 1, NULL),
(6, 'baokhanh', 'KWRwqHaQ1X0q1L5YPWRVvhPIkxnrgwEOWfZ4XNpyf7E=', 'PATIENT', 1, NULL),
(7, 'xuanhoang', 'KWRwqHaQ1X0q1L5YPWRVvhPIkxnrgwEOWfZ4XNpyf7E=', 'PATIENT', 1, NULL),
(8, 'trongtu', 'KWRwqHaQ1X0q1L5YPWRVvhPIkxnrgwEOWfZ4XNpyf7E=', 'PATIENT', 1, NULL),
(9, 'truongan', 'KWRwqHaQ1X0q1L5YPWRVvhPIkxnrgwEOWfZ4XNpyf7E=', 'PATIENT', 1, NULL),
(10, 'phuocanh', 'KWRwqHaQ1X0q1L5YPWRVvhPIkxnrgwEOWfZ4XNpyf7E=', 'PATIENT', 1, NULL);

-- Chèn Hồ sơ người dùng tương ứng
INSERT INTO user_profiles (user_id, full_name, gender, date_of_birth, phone, email, address) VALUES
(1, 'Quản Trị Viên', 'Nam', '1990-01-01', '0999999999', 'admin@healthcare.com', 'Hà Nội'),
(2, 'BS. Trần Văn Lực', 'Nam', '1985-05-12', '0988888881', 'luc.tran@healthcare.com', 'Nội khoa'),
(3, 'BS. Lê Thị Mai', 'Nữ', '1992-08-20', '0988888882', 'mai.le@healthcare.com', 'Nhi khoa'),
(4, 'BS. Phạm Ngọc Thạch', 'Nam', '1980-11-05', '0988888883', 'thach.pham@healthcare.com', 'Tai Mũi Họng'),
(5, 'Nguyễn Tiến Thành', 'Nam', '2004-02-15', '0911111111', 'thanh.nguyen@ptit.edu.vn', 'PTIT Hà Nội'),
(6, 'Nguyễn Trần Bảo Khánh', 'Nam', '2004-03-22', '0922222222', 'khanh.nguyen@ptit.edu.vn', 'Hà Nội'),
(7, 'Ngô Xuân Hoàng', 'Nam', '2004-06-10', '0933333333', 'hoang.ngo@ptit.edu.vn', 'Hà Nội'),
(8, 'Bàng Trọng Tú', 'Nam', '2004-09-05', '0944444444', 'tu.bang@ptit.edu.vn', 'Hà Nội'),
(9, 'Nguyễn Trường An', 'Nam', '2004-12-01', '0955555555', 'an.nguyen@ptit.edu.vn', 'Hà Nội'),
(10, 'Phan Phước Anh', 'Nam', '2004-07-18', '0966666666', 'anh.phan@ptit.edu.vn', 'Hà Nội');

-- =======================================================
-- 5. KIỂM CHỨNG (Verify)
-- =======================================================
SELECT 'Tất cả passwords đều là SHA-256("123456"):' as 'CHECK';
SELECT DISTINCT password, COUNT(*) as count FROM users GROUP BY password;
SELECT username, password FROM users LIMIT 5;