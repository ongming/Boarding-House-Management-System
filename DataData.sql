-- Seed data for BoardingHouse_Pro
-- Run this after creating schema from Database.txt

USE BoardingHouse_Pro;

SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM PHAN_HOI;
DELETE FROM BOI_THUONG;
DELETE FROM HOA_DON;
DELETE FROM CHI_SO_DIEN_NUOC;
DELETE FROM XE;
DELETE FROM HOP_DONG;
DELETE FROM KHACH_THUE;
DELETE FROM PHONG;
DELETE FROM NHAN_VIEN;
DELETE FROM TAI_KHOAN;
DELETE FROM CAU_HINH_GIA;

SET FOREIGN_KEY_CHECKS = 1;

-- 1) TAI_KHOAN
INSERT INTO TAI_KHOAN (id, ten_dang_nhap, mat_khau, ho_ten, loai_tai_khoan) VALUES
(1, 'admin', 'password123', 'Chu tro Nam', 'ADMIN'),
(2, 'staff01', 'password123', 'Nhan vien An', 'NHAN_VIEN'),
(3, 'staff02', 'password123', 'Nhan vien Binh', 'NHAN_VIEN');

-- 2) NHAN_VIEN
INSERT INTO NHAN_VIEN (id, tai_khoan_id, ca_lam_viec) VALUES
(1, 2, 'Ca sang: Thu 2 - Thu 7 (07:00 - 15:00)'),
(2, 3, 'Ca chieu: Thu 2 - Thu 7 (14:00 - 22:00)');

-- 3) PHONG
INSERT INTO PHONG (id, so_phong, tang, kich_thuoc, gia_thue_co_ban, danh_sach_noi_that, trang_thai) VALUES
(1, '101', 1, 18.00, 2800000.00, 'Giuong,Tủ,Ban,Quat', 'DA_THUE'),
(2, '102', 1, 20.00, 3200000.00, 'Giuong,Tủ,Ban,May lanh', 'DA_THUE'),
(3, '201', 2, 22.50, 3600000.00, 'Giuong,Tủ,Ban,May lanh,Nong lanh', 'TRONG'),
(4, '202', 2, 19.00, 3000000.00, 'Giuong,Tủ,Ban', 'BAO_TRI');

-- 4) KHACH_THUE
INSERT INTO KHACH_THUE (id, ho_ten, so_cccd, so_dien_thoai) VALUES
(1, 'Nguyen Van A', '079123456789', '0901111222'),
(2, 'Tran Thi B', '079987654321', '0912233445'),
(3, 'Le Van C', '079112233445', '0933344556');

-- 5) HOP_DONG
INSERT INTO HOP_DONG (id, khach_thue_id, phong_id, ngay_bat_dau, ngay_ket_thuc, tien_coc, anh_hop_dong_url, so_luong_nguoi_o, trang_thai) VALUES
(1, 1, 1, '2026-01-01', NULL, 3000000.00, 'https://example.com/contracts/hd001.jpg', 2, 'HIEU_LUC'),
(2, 2, 2, '2026-02-15', NULL, 3200000.00, 'https://example.com/contracts/hd002.jpg', 1, 'HIEU_LUC'),
(3, 3, 3, '2025-06-01', '2026-01-31', 2800000.00, 'https://example.com/contracts/hd003.jpg', 1, 'KET_THUC');

-- 6) XE
INSERT INTO XE (id, hop_dong_id, loai_xe, bien_so) VALUES
(1, 1, 'XE_MAY', '59A1-123.45'),
(2, 1, 'XE_MAY', '59B2-456.78'),
(3, 2, 'XE_MAY', '60C1-234.56');

-- 7) CHI_SO_DIEN_NUOC
INSERT INTO CHI_SO_DIEN_NUOC (id, phong_id, thang, nam, so_dien_cu, so_dien_moi, so_nuoc_cu, so_nuoc_moi, ngay_ghi) VALUES
(1, 1, 2, 2026, 1200, 1288, 350, 362, '2026-02-28 20:10:00'),
(2, 1, 3, 2026, 1288, 1365, 362, 374, '2026-03-31 20:15:00'),
(3, 2, 2, 2026, 800, 867, 210, 221, '2026-02-28 20:20:00'),
(4, 2, 3, 2026, 867, 933, 221, 230, '2026-03-31 20:25:00');

-- 8) HOA_DON
INSERT INTO HOA_DON (id, hop_dong_id, nhan_vien_id, thang, nam, tien_phong, tien_dien, tien_nuoc, tien_xe, tien_rac, tong_tien, da_thanh_toan, hinh_thuc_thanh_toan, ngay_thanh_toan, ghi_chu, ngay_lap) VALUES
(1, 1, 1, 2, 2026, 2800000.00, 308000.00, 180000.00, 200000.00, 30000.00, 3518000.00, TRUE, 'CHUYEN_KHOAN', '2026-03-05 10:30:00', 'Da thu du', '2026-03-01 09:00:00'),
(2, 1, 1, 3, 2026, 2800000.00, 269500.00, 180000.00, 200000.00, 30000.00, 3479500.00, FALSE, 'CHUA_THU', NULL, 'Cho thanh toan', '2026-04-01 09:00:00'),
(3, 2, 2, 3, 2026, 3200000.00, 231000.00, 135000.00, 100000.00, 30000.00, 3696000.00, TRUE, 'TIEN_MAT', '2026-04-03 16:10:00', 'Thu tai van phong', '2026-04-01 09:10:00');

-- 9) BOI_THUONG
INSERT INTO BOI_THUONG (id, hop_dong_id, so_tien, ly_do, da_thu, hinh_thuc_thanh_toan, ngay_tao) VALUES
(1, 3, 500000.00, 'Hong quat treo tuong', TRUE, 'TRU_VAO_COC', '2026-01-31'),
(2, 3, 200000.00, 'Mat chia khoa phong', FALSE, 'TIEN_MAT', '2026-01-31');

-- 10) PHAN_HOI
INSERT INTO PHAN_HOI (id, phong_id, nhan_vien_id, noi_dung, trang_thai, ngay_gui) VALUES
(1, 1, 1, 'Dieu hoa phong 101 khong lanh', 'DANG_SUA', '2026-03-10 11:20:00'),
(2, 2, 2, 'Bon rua bi ro nuoc nhe', 'CHO_XU_LY', '2026-03-18 08:45:00'),
(3, 1, 1, 'Da sua xong aptomat', 'HOAN_THANH', '2026-03-22 14:05:00');

-- 11) CAU_HINH_GIA
INSERT INTO CAU_HINH_GIA (id, ten_loai, don_gia) VALUES
(1, 'DIEN', 3500.00),
(2, 'NUOC', 15000.00),
(3, 'RAC', 30000.00),
(4, 'XE_MAY', 100000.00);
