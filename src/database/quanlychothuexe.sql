DROP DATABASE IF EXISTS quanlychothuexe;
CREATE DATABASE quanlychothuexe CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE quanlychothuexe;

CREATE TABLE nhanvien (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tendangnhap VARCHAR(50) NOT NULL UNIQUE,
    matkhau VARCHAR(100) NOT NULL,
    hoten VARCHAR(100) NOT NULL,
    sdt VARCHAR(15)
);

CREATE TABLE khachhang (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cmnd VARCHAR(20) NOT NULL UNIQUE,
    hoten VARCHAR(100) NOT NULL,
    sdt VARCHAR(15),
    diachi VARCHAR(200),
    tendangnhap VARCHAR(50) UNIQUE,
    matkhau VARCHAR(100)
);

CREATE TABLE xemay (
    id INT PRIMARY KEY AUTO_INCREMENT,
    bienso VARCHAR(20) NOT NULL UNIQUE,
    hangxe VARCHAR(50),
    model VARCHAR(50),
    giathue DECIMAL(12,0) NOT NULL,
    trangthai VARCHAR(20) NOT NULL DEFAULT 'SAN_SANG'
);

CREATE TABLE hopdong (
    id INT PRIMARY KEY AUTO_INCREMENT,
    makh INT NOT NULL,
    maxe INT NOT NULL,
    manv INT,
    ngaythue DATE NOT NULL,
    ngaytra_dukien DATE NOT NULL,
    ngaytra_thucte DATE,
    tongtien DECIMAL(12,0) DEFAULT 0,
    trangthai VARCHAR(20) NOT NULL DEFAULT 'DANG_THUE',
    FOREIGN KEY (makh) REFERENCES khachhang(id),
    FOREIGN KEY (maxe) REFERENCES xemay(id),
    FOREIGN KEY (manv) REFERENCES nhanvien(id)
);

INSERT INTO nhanvien(tendangnhap, matkhau, hoten, sdt) VALUES
('admin', '123456', 'Quản trị viên', '0900000000');

INSERT INTO khachhang(cmnd, hoten, sdt, diachi, tendangnhap, matkhau) VALUES
('012345678', 'Nguyễn Văn A', '0901234567', 'Hà Nội', 'kh001', '123'),
('098765432', 'Trần Thị B', '0912345678', 'Hải Phòng', 'kh002', '123'),
('111222333', 'Lê Văn C', '0987654321', 'Đà Nẵng', NULL, NULL);

INSERT INTO xemay(bienso, hangxe, model, giathue, trangthai) VALUES
('29A1-12345', 'Honda', 'Wave Alpha', 100000, 'SAN_SANG'),
('29A1-67890', 'Honda', 'Vision', 120000, 'SAN_SANG'),
('29B2-11111', 'Yamaha', 'Exciter', 150000, 'SAN_SANG'),
('29B2-22222', 'Honda', 'SH', 250000, 'SAN_SANG'),
('29C3-33333', 'Yamaha', 'Sirius', 90000, 'BAO_TRI');

SELECT 'Tao database thanh cong!' AS thongbao;
SELECT 'Tai khoan: admin/123456 (nhan vien), kh001/123 (khach), kh002/123 (khach)' AS taikhoan_mau;
