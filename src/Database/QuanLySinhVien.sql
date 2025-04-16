-- Xóa và tạo lại CSDL
DROP DATABASE IF EXISTS QuanLySinhVien;
CREATE DATABASE QuanLySinhVien;
USE QuanLySinhVien;

-- Tạo bảng
CREATE TABLE tblChucVu (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tenCV VARCHAR(55)
);

CREATE TABLE tblKhoa (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tenKhoa VARCHAR(255)
);

CREATE TABLE tblThanhVien (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tblChucVuid INT,
    tblKhoaid INT,
    maSV VARCHAR(255) UNIQUE,
    lop VARCHAR(55),
    hoTen VARCHAR(255),
    diaChi VARCHAR(255),
    ngaySinh DATE,
    FOREIGN KEY (tblChucVuid) REFERENCES tblChucVu(id),
    FOREIGN KEY (tblKhoaid) REFERENCES tblKhoa(id)
);

CREATE TABLE tblKiHoc (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tenKiHoc VARCHAR(255)
);

CREATE TABLE tblMonHoc (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tenMH VARCHAR(255),
    soTc INT
);

CREATE TABLE tblMonHocDauDiem (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tenDauDiem VARCHAR(255),
    heSo INT,
    tblMonHocid INT,
    FOREIGN KEY (tblMonHocid) REFERENCES tblMonHoc(id)
);

CREATE TABLE tblLopHocPhan (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tblKiHocid INT,
    tblMonHocid INT,
    nhomMonHoc VARCHAR(255),
    siSoToiDa VARCHAR(255),
    namHoc INT,
    FOREIGN KEY (tblKiHocid) REFERENCES tblKiHoc(id),
    FOREIGN KEY (tblMonHocid) REFERENCES tblMonHoc(id)
);

CREATE TABLE tblThamGia (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tblThanhVienid INT,
    tblLopHocPhanid INT,
    FOREIGN KEY (tblThanhVienid) REFERENCES tblThanhVien(id),
    FOREIGN KEY (tblLopHocPhanid) REFERENCES tblLopHocPhan(id)
);

CREATE TABLE tblKetQua (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tblMonHocDauDiemid INT,
    tblThamGiaid INT,
    diem FLOAT,
    FOREIGN KEY (tblMonHocDauDiemid) REFERENCES tblMonHocDauDiem(id),
    FOREIGN KEY (tblThamGiaid) REFERENCES tblThamGia(id)
);

CREATE TABLE tblDiemChu (
    id INT PRIMARY KEY AUTO_INCREMENT,
    diemChu VARCHAR(255),
    diemHe10ToiThieu FLOAT,
    diemHe10ToiDa FLOAT
);

CREATE TABLE tblPhucKhao (
    id INT PRIMARY KEY AUTO_INCREMENT,
    maSV VARCHAR(255),
    tblKetQuaId INT,
    noiDungPK VARCHAR(500),
    noiDungXL VARCHAR(500) DEFAULT 'Chưa xử lý',
    trangThaiXL VARCHAR(50) DEFAULT 'Chưa xử lý',
    FOREIGN KEY (tblKetQuaId) REFERENCES tblKetQua(id),
    FOREIGN KEY (maSV) REFERENCES tblThanhVien(maSV)
);
-- Dữ liệu mẫu

-- tblChucVu
INSERT INTO tblChucVu (tenCV) VALUES
('Sinh viên'), ('Giảng viên'), ('Quản trị hệ thống');

-- tblKhoa
INSERT INTO tblKhoa (tenKhoa) VALUES
('Công nghệ thông tin'), ('Quản trị kinh doanh'), ('Tài chính – Ngân hàng'), ('Kế toán'),
('Ngoại ngữ'), ('Luật'), ('Y dược'), ('Kiến trúc'), ('Kỹ thuật điện – điện tử'),
('Môi trường'), ('Du lịch – Nhà hàng – Khách sạn'), ('Công nghệ thực phẩm'), ('Giáo dục'),
('Thương mại điện tử'), ('Tâm lý học');

-- tblThanhVien
INSERT INTO tblThanhVien (tblChucVuid, tblKhoaid, maSV, lop, hoTen, diaChi, ngaySinh) VALUES
(1, 1, 'SV001', 'CNTT1', 'Nguyễn Văn A', 'Hà Nội', '2001-01-01');

-- tblKiHoc
INSERT INTO tblKiHoc (tenKiHoc) VALUES
('Học kỳ 1'), ('Học kỳ 2'), ('Học kỳ hè');

-- tblMonHoc
INSERT INTO tblMonHoc (tenMH, soTc) VALUES
('Toán cao cấp', 3), ('Lập trình Java', 4), ('Cấu trúc dữ liệu', 3), ('Cơ sở dữ liệu', 3),
('Hệ điều hành', 3), ('Mạng máy tính', 3), ('Lập trình Python', 4), ('Phân tích thiết kế HTTT', 3),
('Kỹ năng mềm', 2), ('Triết học Mác-Lênin', 2), ('Kinh tế học đại cương', 2), ('Tiếng Anh chuyên ngành', 3),
('Marketing căn bản', 3), ('Kế toán tài chính', 3), ('Pháp luật đại cương', 2);

-- tblMonHocDauDiem: chỉ có 2 đầu điểm mỗi môn
INSERT INTO tblMonHocDauDiem (tenDauDiem, heSo, tblMonHocid)
SELECT 'Giữa kỳ', 40, id FROM tblMonHoc
UNION ALL
SELECT 'Cuối kỳ', 60, id FROM tblMonHoc;

-- tblLopHocPhan
INSERT INTO tblLopHocPhan (tblKiHocid, tblMonHocid, nhomMonHoc, siSoToiDa, namHoc) VALUES
(1, 1, 'Nhóm 1', '60', 2024), (2, 2, 'Nhóm 2', '60', 2024), (3, 3, 'Nhóm 3', '60', 2024),
(1, 4, 'Nhóm 4', '60', 2024), (2, 5, 'Nhóm 5', '60', 2024), (3, 6, 'Nhóm 6', '60', 2024),
(1, 7, 'Nhóm 7', '60', 2024), (2, 8, 'Nhóm 8', '60', 2024), (3, 9, 'Nhóm 9', '60', 2024),
(1, 10, 'Nhóm 10', '60', 2024), (2, 11, 'Nhóm 11', '60', 2024), (3, 12, 'Nhóm 12', '60', 2024),
(1, 13, 'Nhóm 13', '60', 2024), (2, 14, 'Nhóm 14', '60', 2024), (3, 15, 'Nhóm 15', '60', 2024);

-- tblThamGia cho SV001 tham gia tất cả môn học
INSERT INTO tblThamGia (tblThanhVienid, tblLopHocPhanid) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
(1, 6), (1, 7), (1, 8), (1, 9), (1, 10),
(1, 11), (1, 12), (1, 13), (1, 14), (1, 15);

-- tblKetQua: Điểm đầy đủ cho SV001 ở tất cả các đầu điểm (giữa kỳ + cuối kỳ)
INSERT INTO tblKetQua (tblMonHocDauDiemid, tblThamGiaid, diem)
SELECT m.id, t.id, ROUND(5 + (RAND() * 5), 1)
FROM tblMonHocDauDiem m
JOIN tblThamGia t ON t.tblThanhVienid = 1
JOIN tblLopHocPhan lhp ON lhp.id = t.tblLopHocPhanid
WHERE m.tblMonHocid = lhp.tblMonHocid;

-- tblDiemChu
INSERT INTO tblDiemChu (diemChu, diemHe10ToiThieu, diemHe10ToiDa) VALUES
('A+', 9.5, 10.1), ('A', 8.5, 9.5), ('B+', 8.0, 8.5), ('B', 7.0, 8.0),
('C+', 6.5, 7.0), ('C', 5.5, 6.5), ('D+', 5.0, 5.5), ('D', 4.0, 5.0),
('F', 0.0, 4.0);

-- Thêm phúc khảo chưa xử lý
INSERT INTO tblPhucKhao (maSV, tblKetQuaId, noiDungPK)
VALUES
('SV001', 1, 'Em thấy điểm giữa kỳ môn Toán cao cấp chưa chính xác, xin được xem lại.'),
('SV001', 3, 'Điểm cuối kỳ môn Cấu trúc dữ liệu có thể bị nhầm, em muốn phúc khảo.'),
('SV001', 10, 'Em nghĩ bài thi cuối kỳ môn Hệ điều hành chấm thiếu, xin kiểm tra lại.');

-- Thêm phúc khảo đã xử lý
INSERT INTO tblPhucKhao (maSV, tblKetQuaId, noiDungPK, noiDungXL, trangThaiXL)
VALUES
('SV001', 2, 'Xin phúc khảo bài giữa kỳ môn Lập trình Java.', 'Đã kiểm tra, điểm đúng, không thay đổi.', 'Đã xử lý'),
('SV001', 5, 'Điểm cuối kỳ môn Cơ sở dữ liệu không đúng với bài làm.', 'Chấm lại, điểm điều chỉnh từ 6.0 lên 7.0.', 'Đã xử lý'),
('SV001', 8, 'Em thấy điểm giữa kỳ môn Mạng máy tính thấp bất thường.', 'Kiểm tra lại, điểm đúng, không thay đổi.', 'Đã xử lý');

