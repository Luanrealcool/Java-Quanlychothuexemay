USE quanlychothuexe;

INSERT INTO loaixe(ten, mota) VALUES
('Tay ga', 'Xe tay ga tự động, dễ điều khiển'),
('Số sàn', 'Xe số sàn truyền thống, tiết kiệm xăng'),
('Côn tay', 'Xe côn tay, dành cho người chơi thể thao'),
('Điện', 'Xe máy điện thân thiện môi trường');

INSERT INTO nhanvien(tendangnhap, matkhau_hash, hoten, sdt, vaitro) VALUES
('admin', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',
    'Quản trị viên', '0900000000', 'QUAN_LY'),
('nv01', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',
    'Nguyễn Văn Hùng', '0901111111', 'NHAN_VIEN'),
('nv02', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',
    'Trần Thị Lan', '0902222222', 'NHAN_VIEN');

INSERT INTO khachhang(cmnd, hoten, sdt, ngaysinh, gioitinh, tendangnhap, matkhau_hash) VALUES
('001000000001', 'Nguyễn Văn An', '0911000001', '1990-01-15', 'NAM',
    'kh001', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3'),
('001000000002', 'Trần Thị Bình', '0911000002', '1992-02-20', 'NU',
    'kh002', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3'),
('001000000003', 'Lê Văn Cường', '0911000003', '1988-05-10', 'NAM', NULL, NULL),
('001000000004', 'Phạm Thị Dung', '0911000004', '1995-08-25', 'NU', NULL, NULL),
('001000000005', 'Hoàng Văn Em', '0911000005', '1991-11-30', 'NAM', NULL, NULL),
('001000000006', 'Vũ Thị Phương', '0911000006', '1993-03-12', 'NU', NULL, NULL),
('001000000007', 'Đặng Văn Giang', '0911000007', '1987-07-07', 'NAM', NULL, NULL),
('001000000008', 'Bùi Thị Hoa', '0911000008', '1996-09-18', 'NU', NULL, NULL),
('001000000009', 'Đỗ Văn Ích', '0911000009', '1989-12-22', 'NAM', NULL, NULL),
('001000000010', 'Ngô Thị Kim', '0911000010', '1994-04-04', 'NU', NULL, NULL),
('001000000011', 'Cao Văn Long', '0911000011', '1990-06-15', 'NAM', NULL, NULL),
('001000000012', 'Lý Thị Mai', '0911000012', '1992-10-25', 'NU', NULL, NULL),
('001000000013', 'Tô Văn Nam', '0911000013', '1988-08-08', 'NAM', NULL, NULL),
('001000000014', 'Phan Thị Oanh', '0911000014', '1995-02-14', 'NU', NULL, NULL),
('001000000015', 'Hồ Văn Phúc', '0911000015', '1991-05-19', 'NAM', NULL, NULL),
('001000000016', 'Đinh Thị Quỳnh', '0911000016', '1993-07-21', 'NU', NULL, NULL),
('001000000017', 'Trương Văn Rạng', '0911000017', '1987-11-11', 'NAM', NULL, NULL),
('001000000018', 'Hà Thị Sương', '0911000018', '1996-12-01', 'NU', NULL, NULL),
('001000000019', 'Mai Văn Tài', '0911000019', '1989-03-03', 'NAM', NULL, NULL),
('001000000020', 'Lương Thị Uyên', '0911000020', '1994-09-09', 'NU', NULL, NULL),
('001000000021', 'Đào Văn Việt', '0911000021', '1990-04-16', 'NAM', NULL, NULL),
('001000000022', 'Châu Thị Xuân', '0911000022', '1992-08-28', 'NU', NULL, NULL),
('001000000023', 'Tạ Văn Yên', '0911000023', '1988-01-01', 'NAM', NULL, NULL),
('001000000024', 'Khúc Thị Ánh', '0911000024', '1995-06-06', 'NU', NULL, NULL),
('001000000025', 'Vương Văn Bảo', '0911000025', '1991-10-13', 'NAM', NULL, NULL),
('001000000026', 'Lưu Thị Châu', '0911000026', '1993-02-23', 'NU', NULL, NULL),
('001000000027', 'Quách Văn Đức', '0911000027', '1987-05-15', 'NAM', NULL, NULL),
('001000000028', 'Thái Thị Hằng', '0911000028', '1996-08-17', 'NU', NULL, NULL),
('001000000029', 'Phùng Văn Linh', '0911000029', '1989-11-29', 'NAM', NULL, NULL),
('001000000030', 'Đoàn Thị Minh', '0911000030', '1994-12-12', 'NU', NULL, NULL),
('001000000031', 'Lâm Văn Nghĩa', '0911000031', '1990-07-07', 'NAM', NULL, NULL),
('001000000032', 'Lại Thị Oanh', '0911000032', '1992-03-15', 'NU', NULL, NULL),
('001000000033', 'Hứa Văn Phong', '0911000033', '1988-09-19', 'NAM', NULL, NULL),
('001000000034', 'Tô Thị Quế', '0911000034', '1995-11-11', 'NU', NULL, NULL),
('001000000035', 'Bạch Văn Sang', '0911000035', '1991-01-08', 'NAM', NULL, NULL),
('001000000036', 'Trịnh Thị Thảo', '0911000036', '1993-04-24', 'NU', NULL, NULL),
('001000000037', 'Văn Văn Tuấn', '0911000037', '1987-06-30', 'NAM', NULL, NULL),
('001000000038', 'Quan Thị Vy', '0911000038', '1996-10-20', 'NU', NULL, NULL),
('001000000039', 'Doãn Văn Xuân', '0911000039', '1989-02-02', 'NAM', NULL, NULL),
('001000000040', 'Tống Thị Yến', '0911000040', '1994-05-05', 'NU', NULL, NULL),
('001000000041', 'Nông Văn Bách', '0911000041', '1990-08-18', 'NAM', NULL, NULL),
('001000000042', 'Sầm Thị Cúc', '0911000042', '1992-11-11', 'NU', NULL, NULL),
('001000000043', 'Triệu Văn Đạt', '0911000043', '1988-12-22', 'NAM', NULL, NULL),
('001000000044', 'La Thị Hân', '0911000044', '1995-03-26', 'NU', NULL, NULL),
('001000000045', 'Khương Văn Khải', '0911000045', '1991-07-14', 'NAM', NULL, NULL),
('001000000046', 'Đới Thị Lan', '0911000046', '1993-09-09', 'NU', NULL, NULL),
('001000000047', 'Giáp Văn Mạnh', '0911000047', '1987-04-04', 'NAM', NULL, NULL),
('001000000048', 'Tôn Thị Ngọc', '0911000048', '1996-07-07', 'NU', NULL, NULL),
('001000000049', 'Bành Văn Phong', '0911000049', '1989-10-10', 'NAM', NULL, NULL),
('001000000050', 'Diệp Thị Quỳnh', '0911000050', '1994-12-25', 'NU', NULL, NULL);

DELIMITER //
CREATE PROCEDURE sinh_xe()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE bs VARCHAR(20);
    DECLARE hang VARCHAR(50);
    DECLARE md VARCHAR(50);
    DECLARE gia DECIMAL(12,0);
    DECLARE tt VARCHAR(20);
    DECLARE loai INT;
    DECLARE mau VARCHAR(30);
    DECLARE nam INT;

    WHILE i <= 100 DO
        SET bs = CONCAT(
            LPAD(FLOOR(11 + RAND()*80), 2, '0'),
            ELT(FLOOR(1 + RAND()*5), 'A', 'B', 'C', 'D', 'E'),
            FLOOR(1 + RAND()*9), '-',
            LPAD(FLOOR(10000 + RAND()*89999), 5, '0')
        );
        SET hang = ELT(FLOOR(1 + RAND()*5), 'Honda', 'Yamaha', 'Suzuki', 'SYM', 'Piaggio');
        SET md = ELT(FLOOR(1 + RAND()*10),
            'Wave Alpha', 'Vision', 'Air Blade', 'SH', 'Lead',
            'Exciter', 'Sirius', 'Jupiter', 'Satria', 'Vespa');
        SET gia = FLOOR(5 + RAND()*21) * 10000;
        SET tt = ELT(FLOOR(1 + RAND()*10),
            'SAN_SANG','SAN_SANG','SAN_SANG','SAN_SANG','SAN_SANG','SAN_SANG','SAN_SANG',
            'DANG_THUE','DANG_THUE','BAO_TRI');
        SET loai = FLOOR(1 + RAND()*4);
        SET mau = ELT(FLOOR(1 + RAND()*6), 'Đen', 'Trắng', 'Đỏ', 'Xanh', 'Vàng', 'Bạc');
        SET nam = 2018 + FLOOR(RAND()*7);

        INSERT INTO xemay(bienso, hangxe, model, maloaixe, namsx, mauxe, giathue, trangthai)
        VALUES (bs, hang, md, loai, nam, mau, gia, tt)
        ON DUPLICATE KEY UPDATE bienso = bienso;

        SET i = i + 1;
    END WHILE;
END//
DELIMITER ;

CALL sinh_xe();
DROP PROCEDURE sinh_xe;

INSERT IGNORE INTO xemay(bienso, hangxe, model, giathue, trangthai) VALUES
('29A1-99991', 'Honda', 'Wave Alpha', 100000, 'SAN_SANG'),
('29A1-99992', 'Honda', 'Vision', 120000, 'SAN_SANG'),
('29A1-99993', 'Yamaha', 'Exciter', 150000, 'SAN_SANG'),
('29A1-99994', 'Honda', 'SH', 250000, 'SAN_SANG'),
('29A1-99995', 'Yamaha', 'Sirius', 90000, 'BAO_TRI');
