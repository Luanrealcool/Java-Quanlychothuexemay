DROP DATABASE IF EXISTS quanlychothuexe;
CREATE DATABASE quanlychothuexe CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE quanlychothuexe;

CREATE TABLE nhanvien (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tendangnhap VARCHAR(50) NOT NULL UNIQUE,
    matkhau_hash CHAR(64) NOT NULL,
    hoten VARCHAR(100) NOT NULL,
    sdt VARCHAR(15),
    vaitro VARCHAR(20) NOT NULL DEFAULT 'NHAN_VIEN',
    trangthai VARCHAR(20) NOT NULL DEFAULT 'HOAT_DONG',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_nv_tdn (tendangnhap),
    INDEX idx_nv_trangthai (trangthai)
);

CREATE TABLE khachhang (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cmnd VARCHAR(20) NOT NULL UNIQUE,
    hoten VARCHAR(100) NOT NULL,
    sdt VARCHAR(15),
    ngaysinh DATE,
    gioitinh VARCHAR(10),
    tendangnhap VARCHAR(50) UNIQUE,
    matkhau_hash CHAR(64),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_kh_cmnd (cmnd),
    INDEX idx_kh_hoten (hoten),
    INDEX idx_kh_sdt (sdt)
);

CREATE TABLE loaixe (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ten VARCHAR(50) NOT NULL UNIQUE,
    mota VARCHAR(200)
);

CREATE TABLE xemay (
    id INT PRIMARY KEY AUTO_INCREMENT,
    bienso VARCHAR(20) NOT NULL UNIQUE,
    hangxe VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    maloaixe INT,
    namsx YEAR,
    mauxe VARCHAR(30),
    giathue DECIMAL(12,0) NOT NULL,
    trangthai VARCHAR(20) NOT NULL DEFAULT 'SAN_SANG',
    ghichu VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_xe_gia CHECK (giathue > 0),
    FOREIGN KEY (maloaixe) REFERENCES loaixe(id),
    INDEX idx_xe_bienso (bienso),
    INDEX idx_xe_trangthai (trangthai),
    INDEX idx_xe_hang_model (hangxe, model)
);

CREATE TABLE hopdong (
    id INT PRIMARY KEY AUTO_INCREMENT,
    masohopdong VARCHAR(20) NOT NULL UNIQUE,
    makh INT NOT NULL,
    maxe INT NOT NULL,
    manv INT,
    ngaythue DATE NOT NULL,
    ngaytra_dukien DATE NOT NULL,
    ngaytra_thucte DATE,
    giathue_taithoidiem DECIMAL(12,0) NOT NULL,
    tiendaco DECIMAL(12,0) DEFAULT 0,
    tongtien DECIMAL(12,0) DEFAULT 0,
    phuphi_tre DECIMAL(12,0) DEFAULT 0,
    trangthai VARCHAR(20) NOT NULL DEFAULT 'DANG_THUE',
    lydo_huy VARCHAR(500),
    ghichu VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (makh) REFERENCES khachhang(id),
    FOREIGN KEY (maxe) REFERENCES xemay(id),
    FOREIGN KEY (manv) REFERENCES nhanvien(id),
    CONSTRAINT chk_hd_ngay CHECK (ngaytra_dukien > ngaythue),
    INDEX idx_hd_masohd (masohopdong),
    INDEX idx_hd_trangthai (trangthai),
    INDEX idx_hd_ngaythue (ngaythue),
    INDEX idx_hd_ngaytratt (ngaytra_thucte),
    INDEX idx_hd_makh (makh),
    INDEX idx_hd_maxe (maxe)
);

DELIMITER //
CREATE TRIGGER trg_hopdong_genmaso BEFORE INSERT ON hopdong
FOR EACH ROW
BEGIN
    IF NEW.masohopdong IS NULL OR NEW.masohopdong = '' THEN
        SET NEW.masohopdong = CONCAT('HD-', YEAR(NOW()), '-',
            LPAD((SELECT IFNULL(MAX(id),0)+1 FROM hopdong), 4, '0'));
    END IF;
END//
DELIMITER ;

CREATE OR REPLACE VIEW v_doanhthu_theongay AS
SELECT
    DATE(ngaytra_thucte) AS ngay,
    COUNT(*) AS so_hopdong,
    SUM(tongtien) AS doanh_thu
FROM hopdong
WHERE trangthai = 'DA_TRA' AND ngaytra_thucte IS NOT NULL
GROUP BY DATE(ngaytra_thucte);
