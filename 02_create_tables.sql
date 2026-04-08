-- Create tables for BoardingHouse_Pro
USE BoardingHouse_Pro;

CREATE TABLE TAI_KHOAN (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ten_dang_nhap VARCHAR(50) NOT NULL UNIQUE,
    mat_khau VARCHAR(255) NOT NULL,
    ho_ten VARCHAR(100),
    loai_tai_khoan ENUM('ADMIN', 'NHAN_VIEN') NOT NULL
);

CREATE TABLE NHAN_VIEN (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tai_khoan_id INT UNIQUE,
    ca_lam_viec TEXT,
    FOREIGN KEY (tai_khoan_id) REFERENCES TAI_KHOAN(id) ON DELETE CASCADE
);

CREATE TABLE PHONG (
    id INT AUTO_INCREMENT PRIMARY KEY,
    so_phong VARCHAR(10) NOT NULL UNIQUE,
    tang INT CHECK (tang IN (1, 2)),
    kich_thuoc DECIMAL(10, 2),
    gia_thue_co_ban DECIMAL(15, 2) NOT NULL,
    danh_sach_noi_that TEXT,
    trang_thai ENUM('TRONG', 'DA_THUE', 'BAO_TRI') DEFAULT 'TRONG'
);

CREATE TABLE KHACH_THUE (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ho_ten VARCHAR(100) NOT NULL,
    so_cccd VARCHAR(20) UNIQUE NOT NULL,
    so_dien_thoai VARCHAR(15)
);

CREATE TABLE HOP_DONG (
    id INT AUTO_INCREMENT PRIMARY KEY,
    khach_thue_id INT,
    phong_id INT,
    ngay_bat_dau DATE NOT NULL,
    ngay_don_vao DATE,
    ngay_ket_thuc DATE,
    tien_coc DECIMAL(15, 2),
    anh_hop_dong_url VARCHAR(255),
    so_luong_nguoi_o INT DEFAULT 1,
    trang_thai ENUM('HIEU_LUC', 'KET_THUC') DEFAULT 'HIEU_LUC',
    FOREIGN KEY (khach_thue_id) REFERENCES KHACH_THUE(id),
    FOREIGN KEY (phong_id) REFERENCES PHONG(id)
);

CREATE TABLE XE (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hop_dong_id INT,
    loai_xe VARCHAR(50),
    bien_so VARCHAR(20),
    FOREIGN KEY (hop_dong_id) REFERENCES HOP_DONG(id) ON DELETE CASCADE
);

CREATE TABLE CHI_SO_DIEN_NUOC (
    id INT AUTO_INCREMENT PRIMARY KEY,
    phong_id INT,
    thang INT NOT NULL,
    nam INT NOT NULL,
    so_dien_cu INT NOT NULL,
    so_dien_moi INT NOT NULL,
    so_nuoc_cu INT NOT NULL,
    so_nuoc_moi INT NOT NULL,
    ngay_ghi DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (phong_id) REFERENCES PHONG(id)
);

CREATE TABLE HOA_DON (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hop_dong_id INT,
    nhan_vien_id INT,
    thang INT NOT NULL,
    nam INT NOT NULL,
    tien_phong DECIMAL(15, 2) DEFAULT 0,
    tien_dien DECIMAL(15, 2) DEFAULT 0,
    tien_nuoc DECIMAL(15, 2) DEFAULT 0,
    tien_xe DECIMAL(15, 2) DEFAULT 0,
    tien_rac DECIMAL(15, 2) DEFAULT 0,
    tong_tien DECIMAL(15, 2) NOT NULL,
    da_thanh_toan BOOLEAN DEFAULT FALSE,
    hinh_thuc_thanh_toan ENUM('TIEN_MAT', 'CHUYEN_KHOAN', 'CHUA_THU') DEFAULT 'CHUA_THU',
    ngay_thanh_toan DATETIME,
    ghi_chu TEXT,
    ngay_lap DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hop_dong_id) REFERENCES HOP_DONG(id),
    FOREIGN KEY (nhan_vien_id) REFERENCES NHAN_VIEN(id)
);

CREATE TABLE BOI_THUONG (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hop_dong_id INT,
    so_tien DECIMAL(15, 2) NOT NULL,
    ly_do VARCHAR(255),
    da_thu BOOLEAN DEFAULT FALSE,
    hinh_thuc_thanh_toan ENUM('TIEN_MAT', 'CHUYEN_KHOAN', 'TRU_VAO_COC') DEFAULT 'TIEN_MAT',
    ngay_tao DATE NOT NULL,
    FOREIGN KEY (hop_dong_id) REFERENCES HOP_DONG(id) ON DELETE CASCADE
);

CREATE TABLE PHAN_HOI (
    id INT AUTO_INCREMENT PRIMARY KEY,
    phong_id INT,
    nhan_vien_id INT,
    noi_dung TEXT,
    trang_thai ENUM('CHO_XU_LY', 'DANG_SUA', 'HOAN_THANH') DEFAULT 'CHO_XU_LY',
    ngay_gui DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (phong_id) REFERENCES PHONG(id),
    FOREIGN KEY (nhan_vien_id) REFERENCES NHAN_VIEN(id)
);

CREATE TABLE CAU_HINH_GIA (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ten_loai VARCHAR(50) UNIQUE,
    don_gia DECIMAL(15, 2) NOT NULL
);
