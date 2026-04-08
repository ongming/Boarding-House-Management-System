-- Insert sample data for BoardingHouse_Pro
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

INSERT INTO TAI_KHOAN (id, ten_dang_nhap, mat_khau, ho_ten, loai_tai_khoan) VALUES
(1, 'admin', '12345', 'Chu tro Nam', 'ADMIN'),
(2, 'staff', '12345', 'Nhan vien 01', 'NHAN_VIEN'),
(3, 'staff02', '12345', 'Nhan vien 02', 'NHAN_VIEN'),
(4, 'staff03', '12345', 'Nhan vien 03', 'NHAN_VIEN'),
(5, 'staff04', '12345', 'Nhan vien 04', 'NHAN_VIEN'),
(6, 'staff05', '12345', 'Nhan vien 05', 'NHAN_VIEN'),
(7, 'staff06', '12345', 'Nhan vien 06', 'NHAN_VIEN'),
(8, 'staff07', '12345', 'Nhan vien 07', 'NHAN_VIEN'),
(9, 'staff08', '12345', 'Nhan vien 08', 'NHAN_VIEN'),
(10, 'staff09', '12345', 'Nhan vien 09', 'NHAN_VIEN'),
(11, 'staff10', '12345', 'Nhan vien 10', 'NHAN_VIEN'),
(12, 'staff11', '12345', 'Nhan vien 11', 'NHAN_VIEN'),
(13, 'staff12', '12345', 'Nhan vien 12', 'NHAN_VIEN'),
(14, 'staff13', '12345', 'Nhan vien 13', 'NHAN_VIEN'),
(15, 'staff14', '12345', 'Nhan vien 14', 'NHAN_VIEN'),
(16, 'staff15', '12345', 'Nhan vien 15', 'NHAN_VIEN'),
(17, 'staff16', '12345', 'Nhan vien 16', 'NHAN_VIEN'),
(18, 'staff17', '12345', 'Nhan vien 17', 'NHAN_VIEN'),
(19, 'staff18', '12345', 'Nhan vien 18', 'NHAN_VIEN'),
(20, 'staff19', '12345', 'Nhan vien 19', 'NHAN_VIEN'),
(21, 'staff20', '12345', 'Nhan vien 20', 'NHAN_VIEN');

INSERT INTO NHAN_VIEN (id, tai_khoan_id, ca_lam_viec) VALUES
(1, 2, 'Ca sang: Thu 2 - Thu 7 (07:00 - 15:00)'),
(2, 3, 'Ca chieu: Thu 2 - Thu 7 (14:00 - 22:00)'),
(3, 4, 'Ca sang: Thu 2 - Thu 7 (07:00 - 15:00)'),
(4, 5, 'Ca chieu: Thu 2 - Thu 7 (14:00 - 22:00)'),
(5, 6, 'Ca sang: Thu 2 - Thu 7 (07:00 - 15:00)'),
(6, 7, 'Ca chieu: Thu 2 - Thu 7 (14:00 - 22:00)'),
(7, 8, 'Ca sang: Thu 2 - Thu 7 (07:00 - 15:00)'),
(8, 9, 'Ca chieu: Thu 2 - Thu 7 (14:00 - 22:00)'),
(9, 10, 'Ca sang: Thu 2 - Thu 7 (07:00 - 15:00)'),
(10, 11, 'Ca chieu: Thu 2 - Thu 7 (14:00 - 22:00)'),
(11, 12, 'Ca sang: Thu 2 - Thu 7 (07:00 - 15:00)'),
(12, 13, 'Ca chieu: Thu 2 - Thu 7 (14:00 - 22:00)'),
(13, 14, 'Ca sang: Thu 2 - Thu 7 (07:00 - 15:00)'),
(14, 15, 'Ca chieu: Thu 2 - Thu 7 (14:00 - 22:00)'),
(15, 16, 'Ca sang: Thu 2 - Thu 7 (07:00 - 15:00)'),
(16, 17, 'Ca chieu: Thu 2 - Thu 7 (14:00 - 22:00)'),
(17, 18, 'Ca sang: Thu 2 - Thu 7 (07:00 - 15:00)'),
(18, 19, 'Ca chieu: Thu 2 - Thu 7 (14:00 - 22:00)'),
(19, 20, 'Ca sang: Thu 2 - Thu 7 (07:00 - 15:00)'),
(20, 21, 'Ca chieu: Thu 2 - Thu 7 (14:00 - 22:00)');

INSERT INTO PHONG (id, so_phong, tang, kich_thuoc, gia_thue_co_ban, danh_sach_noi_that, trang_thai) VALUES
(1, '101', 1, 18.00, 2800000.00, 'Giuong,Tu,Ban,Quat', 'DA_THUE'),
(2, '102', 1, 20.00, 3200000.00, 'Giuong,Tu,Ban,May lanh', 'DA_THUE'),
(3, '103', 1, 17.50, 2700000.00, 'Giuong,Tu,Ban', 'TRONG'),
(4, '104', 1, 19.00, 3000000.00, 'Giuong,Tu,Ban', 'BAO_TRI'),
(5, '105', 1, 21.00, 3300000.00, 'Giuong,Tu,Ban,May lanh', 'DA_THUE'),
(6, '106', 1, 18.50, 2900000.00, 'Giuong,Tu,Ban,Quat', 'TRONG'),
(7, '107', 1, 16.50, 2500000.00, 'Giuong,Tu,Ban', 'DA_THUE'),
(8, '108', 1, 22.00, 3500000.00, 'Giuong,Tu,Ban,May lanh', 'DA_THUE'),
(9, '109', 1, 20.50, 3400000.00, 'Giuong,Tu,Ban,May lanh', 'TRONG'),
(10, '110', 1, 19.50, 3100000.00, 'Giuong,Tu,Ban,Quat', 'DA_THUE'),
(11, '201', 2, 22.50, 3600000.00, 'Giuong,Tu,Ban,May lanh,Nong lanh', 'TRONG'),
(12, '202', 2, 19.00, 3000000.00, 'Giuong,Tu,Ban', 'BAO_TRI'),
(13, '203', 2, 21.50, 3400000.00, 'Giuong,Tu,Ban,May lanh', 'DA_THUE'),
(14, '204', 2, 18.00, 2800000.00, 'Giuong,Tu,Ban,Quat', 'DA_THUE'),
(15, '205', 2, 23.00, 3700000.00, 'Giuong,Tu,Ban,May lanh', 'TRONG'),
(16, '206', 2, 20.00, 3200000.00, 'Giuong,Tu,Ban', 'DA_THUE'),
(17, '207', 2, 19.50, 3100000.00, 'Giuong,Tu,Ban,Quat', 'DA_THUE'),
(18, '208', 2, 22.00, 3600000.00, 'Giuong,Tu,Ban,May lanh', 'BAO_TRI'),
(19, '209', 2, 21.00, 3300000.00, 'Giuong,Tu,Ban', 'TRONG'),
(20, '210', 2, 18.50, 2900000.00, 'Giuong,Tu,Ban,Quat', 'DA_THUE');

INSERT INTO KHACH_THUE (id, ho_ten, so_cccd, so_dien_thoai) VALUES
(1, 'Nguyen Van A', '079123456789', '0901111222'),
(2, 'Tran Thi B', '079987654321', '0912233445'),
(3, 'Le Van C', '079112233445', '0933344556'),
(4, 'Pham Thi D', '079223344556', '0902222333'),
(5, 'Hoang Van E', '079334455667', '0903333444'),
(6, 'Do Thi F', '079445566778', '0904444555'),
(7, 'Bui Van G', '079556677889', '0905555666'),
(8, 'Vu Thi H', '079667788990', '0906666777'),
(9, 'Dang Van I', '079778899001', '0907777888'),
(10, 'Ngo Thi J', '079889900112', '0908888999'),
(11, 'Ly Van K', '079990011223', '0910000111'),
(12, 'Cao Thi L', '079101112334', '0911111222'),
(13, 'Dinh Van M', '079121314455', '0912222333'),
(14, 'Truong Thi N', '079141516566', '0913333444'),
(15, 'Mai Van O', '079161718677', '0914444555'),
(16, 'Le Thi P', '079181920788', '0915555666'),
(17, 'Phan Van Q', '079202122899', '0916666777'),
(18, 'Nguyen Thi R', '079222324900', '0917777888'),
(19, 'Tran Van S', '079242526011', '0918888999'),
(20, 'Ho Thi T', '079262728122', '0919999000');

INSERT INTO HOP_DONG (id, khach_thue_id, phong_id, ngay_bat_dau, ngay_ket_thuc, tien_coc, anh_hop_dong_url, so_luong_nguoi_o, trang_thai) VALUES
(1, 1, 1, '2026-01-01', NULL, 3000000.00, 'https://example.com/contracts/hd001.jpg', 2, 'HIEU_LUC'),
(2, 2, 2, '2026-02-15', NULL, 3200000.00, 'https://example.com/contracts/hd002.jpg', 1, 'HIEU_LUC'),
(3, 3, 3, '2025-06-01', '2026-01-31', 2800000.00, 'https://example.com/contracts/hd003.jpg', 1, 'KET_THUC'),
(4, 4, 4, '2026-03-01', NULL, 3000000.00, 'https://example.com/contracts/hd004.jpg', 2, 'HIEU_LUC'),
(5, 5, 5, '2026-03-10', NULL, 3300000.00, 'https://example.com/contracts/hd005.jpg', 1, 'HIEU_LUC'),
(6, 6, 6, '2026-02-01', NULL, 2900000.00, 'https://example.com/contracts/hd006.jpg', 1, 'HIEU_LUC'),
(7, 7, 7, '2026-01-20', NULL, 2500000.00, 'https://example.com/contracts/hd007.jpg', 2, 'HIEU_LUC'),
(8, 8, 8, '2026-04-01', NULL, 3500000.00, 'https://example.com/contracts/hd008.jpg', 1, 'HIEU_LUC'),
(9, 9, 9, '2026-02-25', NULL, 3400000.00, 'https://example.com/contracts/hd009.jpg', 1, 'HIEU_LUC'),
(10, 10, 10, '2025-12-01', '2026-02-28', 3100000.00, 'https://example.com/contracts/hd010.jpg', 2, 'KET_THUC'),
(11, 11, 11, '2026-01-05', NULL, 3600000.00, 'https://example.com/contracts/hd011.jpg', 1, 'HIEU_LUC'),
(12, 12, 12, '2026-01-15', NULL, 3000000.00, 'https://example.com/contracts/hd012.jpg', 2, 'HIEU_LUC'),
(13, 13, 13, '2026-02-10', NULL, 3400000.00, 'https://example.com/contracts/hd013.jpg', 1, 'HIEU_LUC'),
(14, 14, 14, '2026-03-05', NULL, 2800000.00, 'https://example.com/contracts/hd014.jpg', 1, 'HIEU_LUC'),
(15, 15, 15, '2025-10-01', '2026-03-31', 3700000.00, 'https://example.com/contracts/hd015.jpg', 2, 'KET_THUC'),
(16, 16, 16, '2026-02-20', NULL, 3200000.00, 'https://example.com/contracts/hd016.jpg', 1, 'HIEU_LUC'),
(17, 17, 17, '2026-03-12', NULL, 3100000.00, 'https://example.com/contracts/hd017.jpg', 1, 'HIEU_LUC'),
(18, 18, 18, '2026-01-25', NULL, 3600000.00, 'https://example.com/contracts/hd018.jpg', 2, 'HIEU_LUC'),
(19, 19, 19, '2026-02-05', NULL, 3300000.00, 'https://example.com/contracts/hd019.jpg', 1, 'HIEU_LUC'),
(20, 20, 20, '2026-03-18', NULL, 2900000.00, 'https://example.com/contracts/hd020.jpg', 1, 'HIEU_LUC');

INSERT INTO XE (id, hop_dong_id, loai_xe, bien_so) VALUES
(1, 1, 'XE_MAY', '59A1-123.45'),
(2, 2, 'XE_MAY', '59B2-456.78'),
(3, 3, 'XE_MAY', '60C1-234.56'),
(4, 4, 'XE_MAY', '60D1-111.22'),
(5, 5, 'XE_MAY', '60E1-222.33'),
(6, 6, 'XE_MAY', '60F1-333.44'),
(7, 7, 'XE_MAY', '60G1-444.55'),
(8, 8, 'XE_MAY', '60H1-555.66'),
(9, 9, 'XE_MAY', '60K1-666.77'),
(10, 10, 'XE_MAY', '60L1-777.88'),
(11, 11, 'XE_MAY', '61A1-888.99'),
(12, 12, 'XE_MAY', '61B1-111.00'),
(13, 13, 'XE_MAY', '61C1-222.11'),
(14, 14, 'XE_MAY', '61D1-333.22'),
(15, 15, 'XE_MAY', '61E1-444.33'),
(16, 16, 'XE_MAY', '61F1-555.44'),
(17, 17, 'XE_MAY', '61G1-666.55'),
(18, 18, 'XE_MAY', '61H1-777.66'),
(19, 19, 'XE_MAY', '61K1-888.77'),
(20, 20, 'XE_MAY', '61L1-999.88');

INSERT INTO CHI_SO_DIEN_NUOC (id, phong_id, thang, nam, so_dien_cu, so_dien_moi, so_nuoc_cu, so_nuoc_moi, ngay_ghi) VALUES
(1, 1, 2, 2026, 1200, 1288, 350, 362, '2026-02-28 20:10:00'),
(2, 1, 3, 2026, 1288, 1365, 362, 374, '2026-03-31 20:15:00'),
(3, 2, 2, 2026, 800, 867, 210, 221, '2026-02-28 20:20:00'),
(4, 2, 3, 2026, 867, 933, 221, 230, '2026-03-31 20:25:00'),
(5, 3, 2, 2026, 650, 712, 180, 190, '2026-02-28 20:30:00'),
(6, 4, 2, 2026, 900, 945, 260, 268, '2026-02-28 20:35:00'),
(7, 5, 2, 2026, 780, 845, 220, 232, '2026-02-28 20:40:00'),
(8, 6, 2, 2026, 710, 774, 205, 214, '2026-02-28 20:45:00'),
(9, 7, 2, 2026, 640, 702, 175, 184, '2026-02-28 20:50:00'),
(10, 8, 2, 2026, 980, 1044, 300, 314, '2026-02-28 20:55:00'),
(11, 9, 2, 2026, 860, 922, 240, 252, '2026-02-28 21:00:00'),
(12, 10, 2, 2026, 820, 882, 230, 240, '2026-02-28 21:05:00'),
(13, 11, 2, 2026, 1000, 1075, 320, 332, '2026-02-28 21:10:00'),
(14, 12, 2, 2026, 740, 800, 210, 220, '2026-02-28 21:15:00'),
(15, 13, 2, 2026, 880, 950, 255, 268, '2026-02-28 21:20:00'),
(16, 14, 2, 2026, 760, 820, 215, 225, '2026-02-28 21:25:00'),
(17, 15, 2, 2026, 1020, 1098, 330, 345, '2026-02-28 21:30:00'),
(18, 16, 2, 2026, 810, 870, 225, 236, '2026-02-28 21:35:00'),
(19, 17, 2, 2026, 790, 852, 220, 230, '2026-02-28 21:40:00'),
(20, 18, 2, 2026, 970, 1036, 295, 308, '2026-02-28 21:45:00');

INSERT INTO HOA_DON (id, hop_dong_id, nhan_vien_id, thang, nam, tien_phong, tien_dien, tien_nuoc, tien_xe, tien_rac, tong_tien, da_thanh_toan, hinh_thuc_thanh_toan, ngay_thanh_toan, ghi_chu, ngay_lap) VALUES
(1, 1, 1, 2, 2026, 2800000.00, 308000.00, 180000.00, 200000.00, 30000.00, 3518000.00, TRUE, 'CHUYEN_KHOAN', '2026-03-05 10:30:00', 'Da thu du', '2026-03-01 09:00:00'),
(2, 1, 2, 3, 2026, 2800000.00, 269500.00, 180000.00, 200000.00, 30000.00, 3479500.00, FALSE, 'CHUA_THU', NULL, 'Cho thanh toan', '2026-04-01 09:00:00'),
(3, 2, 3, 3, 2026, 3200000.00, 231000.00, 135000.00, 100000.00, 30000.00, 3696000.00, TRUE, 'TIEN_MAT', '2026-04-03 16:10:00', 'Thu tai van phong', '2026-04-01 09:10:00'),
(4, 3, 4, 2, 2026, 2700000.00, 180000.00, 120000.00, 100000.00, 30000.00, 3130000.00, TRUE, 'TIEN_MAT', '2026-03-04 09:15:00', 'Da thu', '2026-03-01 09:20:00'),
(5, 4, 5, 3, 2026, 3000000.00, 210000.00, 150000.00, 100000.00, 30000.00, 3490000.00, TRUE, 'CHUYEN_KHOAN', '2026-04-02 08:40:00', 'Da thu', '2026-04-01 09:30:00'),
(6, 5, 6, 3, 2026, 3300000.00, 240000.00, 165000.00, 100000.00, 30000.00, 3835000.00, FALSE, 'CHUA_THU', NULL, 'Cho thanh toan', '2026-04-01 09:40:00'),
(7, 6, 7, 2, 2026, 2900000.00, 200000.00, 140000.00, 100000.00, 30000.00, 3370000.00, TRUE, 'TIEN_MAT', '2026-03-03 14:10:00', 'Da thu', '2026-03-01 09:50:00'),
(8, 7, 8, 2, 2026, 2500000.00, 190000.00, 130000.00, 100000.00, 30000.00, 2950000.00, TRUE, 'TIEN_MAT', '2026-03-03 15:05:00', 'Da thu', '2026-03-01 10:00:00'),
(9, 8, 9, 2, 2026, 3500000.00, 260000.00, 170000.00, 100000.00, 30000.00, 4060000.00, FALSE, 'CHUA_THU', NULL, 'Cho thanh toan', '2026-03-01 10:10:00'),
(10, 9, 10, 2, 2026, 3400000.00, 250000.00, 160000.00, 100000.00, 30000.00, 3940000.00, TRUE, 'CHUYEN_KHOAN', '2026-03-05 11:00:00', 'Da thu', '2026-03-01 10:20:00'),
(11, 10, 11, 1, 2026, 3100000.00, 210000.00, 150000.00, 100000.00, 30000.00, 3590000.00, TRUE, 'CHUYEN_KHOAN', '2026-02-05 10:30:00', 'Da thu', '2026-02-01 09:00:00'),
(12, 11, 12, 2, 2026, 3600000.00, 270000.00, 180000.00, 100000.00, 30000.00, 4180000.00, FALSE, 'CHUA_THU', NULL, 'Cho thanh toan', '2026-03-01 10:30:00'),
(13, 12, 13, 2, 2026, 3000000.00, 220000.00, 150000.00, 100000.00, 30000.00, 3500000.00, TRUE, 'TIEN_MAT', '2026-03-04 13:30:00', 'Da thu', '2026-03-01 10:40:00'),
(14, 13, 14, 2, 2026, 3400000.00, 240000.00, 160000.00, 100000.00, 30000.00, 3930000.00, TRUE, 'CHUYEN_KHOAN', '2026-03-05 09:40:00', 'Da thu', '2026-03-01 10:50:00'),
(15, 14, 15, 2, 2026, 2800000.00, 200000.00, 140000.00, 100000.00, 30000.00, 3270000.00, TRUE, 'TIEN_MAT', '2026-03-03 16:20:00', 'Da thu', '2026-03-01 11:00:00'),
(16, 15, 16, 2, 2026, 3700000.00, 280000.00, 190000.00, 100000.00, 30000.00, 4300000.00, FALSE, 'CHUA_THU', NULL, 'Cho thanh toan', '2026-03-01 11:10:00'),
(17, 16, 17, 2, 2026, 3200000.00, 230000.00, 150000.00, 100000.00, 30000.00, 3710000.00, TRUE, 'CHUYEN_KHOAN', '2026-03-05 12:10:00', 'Da thu', '2026-03-01 11:20:00'),
(18, 17, 18, 2, 2026, 3100000.00, 220000.00, 145000.00, 100000.00, 30000.00, 3595000.00, TRUE, 'TIEN_MAT', '2026-03-03 17:30:00', 'Da thu', '2026-03-01 11:30:00'),
(19, 18, 19, 2, 2026, 3600000.00, 270000.00, 180000.00, 100000.00, 30000.00, 4180000.00, FALSE, 'CHUA_THU', NULL, 'Cho thanh toan', '2026-03-01 11:40:00'),
(20, 19, 20, 2, 2026, 3300000.00, 240000.00, 160000.00, 100000.00, 30000.00, 3830000.00, TRUE, 'CHUYEN_KHOAN', '2026-03-05 13:00:00', 'Da thu', '2026-03-01 11:50:00');

INSERT INTO BOI_THUONG (id, hop_dong_id, so_tien, ly_do, da_thu, hinh_thuc_thanh_toan, ngay_tao) VALUES
(1, 3, 500000.00, 'Hong quat treo tuong', TRUE, 'TRU_VAO_COC', '2026-01-31'),
(2, 3, 200000.00, 'Mat chia khoa phong', FALSE, 'TIEN_MAT', '2026-01-31'),
(3, 5, 150000.00, 'Vo bong den', TRUE, 'TIEN_MAT', '2026-02-10'),
(4, 6, 300000.00, 'Hu laptop bang tho', FALSE, 'CHUYEN_KHOAN', '2026-02-12'),
(5, 7, 100000.00, 'Mat remote may lanh', TRUE, 'TIEN_MAT', '2026-02-15'),
(6, 8, 250000.00, 'Hu sen tam', TRUE, 'CHUYEN_KHOAN', '2026-02-18'),
(7, 9, 120000.00, 'Hu o dien', FALSE, 'TIEN_MAT', '2026-02-20'),
(8, 10, 180000.00, 'Vo kinh', TRUE, 'TIEN_MAT', '2026-02-22'),
(9, 11, 200000.00, 'Mat the phong', FALSE, 'TIEN_MAT', '2026-02-25'),
(10, 12, 130000.00, 'Hu quat', TRUE, 'CHUYEN_KHOAN', '2026-02-27'),
(11, 13, 160000.00, 'Hu khoa cua', TRUE, 'TIEN_MAT', '2026-03-01'),
(12, 14, 140000.00, 'Vo guong', FALSE, 'TIEN_MAT', '2026-03-02'),
(13, 15, 220000.00, 'Hu dieu hoa', TRUE, 'TRU_VAO_COC', '2026-03-03'),
(14, 16, 190000.00, 'Hu den hanh lang', FALSE, 'CHUYEN_KHOAN', '2026-03-04'),
(15, 17, 170000.00, 'Vo tay cam cua', TRUE, 'TIEN_MAT', '2026-03-05'),
(16, 18, 210000.00, 'Hu mang wifi', FALSE, 'CHUYEN_KHOAN', '2026-03-06'),
(17, 19, 115000.00, 'Mat the tu', TRUE, 'TIEN_MAT', '2026-03-07'),
(18, 20, 135000.00, 'Vo chen tam', TRUE, 'TIEN_MAT', '2026-03-08'),
(19, 4, 155000.00, 'Hu bep dien', FALSE, 'CHUYEN_KHOAN', '2026-03-09'),
(20, 2, 175000.00, 'Hu may loc nuoc', TRUE, 'TIEN_MAT', '2026-03-10');

INSERT INTO PHAN_HOI (id, phong_id, nhan_vien_id, noi_dung, trang_thai, ngay_gui) VALUES
(1, 1, 1, 'Dieu hoa phong 101 khong lanh', 'DANG_SUA', '2026-03-10 11:20:00'),
(2, 2, 2, 'Bon rua bi ro nuoc nhe', 'CHO_XU_LY', '2026-03-18 08:45:00'),
(3, 1, 1, 'Da sua xong aptomat', 'HOAN_THANH', '2026-03-22 14:05:00'),
(4, 3, 3, 'Den hanh lang khong sang', 'DANG_SUA', '2026-03-12 09:15:00'),
(5, 4, 4, 'Nuoc may yeu', 'CHO_XU_LY', '2026-03-13 10:30:00'),
(6, 5, 5, 'Cua phong bi ket', 'DANG_SUA', '2026-03-14 15:10:00'),
(7, 6, 6, 'Quat phong 106 keu lon', 'CHO_XU_LY', '2026-03-15 08:05:00'),
(8, 7, 7, 'May lanh ro nuoc', 'DANG_SUA', '2026-03-16 13:20:00'),
(9, 8, 8, 'Bong den phong 108 hong', 'HOAN_THANH', '2026-03-17 19:45:00'),
(10, 9, 9, 'Hu o cam dien', 'CHO_XU_LY', '2026-03-18 07:55:00'),
(11, 10, 10, 'Cua so khong dong duoc', 'DANG_SUA', '2026-03-19 11:05:00'),
(12, 11, 11, 'Nuoc nong khong chay', 'CHO_XU_LY', '2026-03-20 12:40:00'),
(13, 12, 12, 'Roi nuoc nha tam', 'DANG_SUA', '2026-03-21 18:10:00'),
(14, 13, 13, 'Mat wifi tang 2', 'CHO_XU_LY', '2026-03-22 09:25:00'),
(15, 14, 14, 'May lanh khong mat', 'DANG_SUA', '2026-03-23 10:50:00'),
(16, 15, 15, 'Bao tri cua ra vao', 'HOAN_THANH', '2026-03-24 14:30:00'),
(17, 16, 16, 'Den phong 206 chap chon', 'CHO_XU_LY', '2026-03-25 16:05:00'),
(18, 17, 17, 'May bom nuoc yeu', 'DANG_SUA', '2026-03-26 08:20:00'),
(19, 18, 18, 'Roi nuoc tai bon rua', 'CHO_XU_LY', '2026-03-27 09:40:00'),
(20, 19, 19, 'O cam dien bi chay', 'DANG_SUA', '2026-03-28 11:55:00');

INSERT INTO CAU_HINH_GIA (id, ten_loai, don_gia) VALUES
(1, 'DIEN', 3500.00),
(2, 'NUOC', 15000.00),
(3, 'RAC', 30000.00),
(4, 'XE_MAY', 100000.00);
