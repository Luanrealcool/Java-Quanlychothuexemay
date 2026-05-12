# 🗄️ PHẦN 1 — THIẾT KẾ CƠ SỞ DỮ LIỆU

> File này mô tả chi tiết cấu trúc CSDL. Khi làm xong, tick checklist trong [`PROGRESS.md`](PROGRESS.md).

## 1. Sơ đồ quan hệ (tổng quan)

```
   nhanvien                khachhang
      │                       │
      │ 1                     │ 1
      │                       │
      └────────► hopdong ◄────┘
                    │
                    │ N
                    ▼
                  xemay
```

- 1 **nhân viên** lập nhiều hợp đồng
- 1 **khách hàng** có nhiều hợp đồng
- 1 **xe máy** xuất hiện trong nhiều hợp đồng (theo thời gian)

## 2. Chi tiết các bảng

### 🧑‍💼 `nhanvien`

| Cột | Kiểu | Ràng buộc | Ghi chú |
|---|---|---|---|
| `id` | INT | PK, AUTO_INCREMENT | |
| `tendangnhap` | VARCHAR(50) | UNIQUE, NOT NULL | Dùng để đăng nhập |
| `matkhau` | VARCHAR(100) | NOT NULL | Lưu plain text cho đơn giản (bài tập) |
| `hoten` | NVARCHAR(100) | NOT NULL | |
| `sdt` | VARCHAR(15) | | |

### 👤 `khachhang`

| Cột | Kiểu | Ràng buộc | Ghi chú |
|---|---|---|---|
| `id` | INT | PK, AUTO_INCREMENT | |
| `cmnd` | VARCHAR(20) | UNIQUE, NOT NULL | CMND/CCCD |
| `hoten` | NVARCHAR(100) | NOT NULL | |
| `sdt` | VARCHAR(15) | | |
| `diachi` | NVARCHAR(200) | | |

### 🏍️ `xemay`

| Cột | Kiểu | Ràng buộc | Ghi chú |
|---|---|---|---|
| `id` | INT | PK, AUTO_INCREMENT | |
| `bienso` | VARCHAR(20) | UNIQUE, NOT NULL | VD: 29-A1 12345 |
| `hangxe` | NVARCHAR(50) | | Honda, Yamaha, ... |
| `model` | NVARCHAR(50) | | Wave, Vision, Exciter, ... |
| `giathue` | DECIMAL(12,0) | NOT NULL | Đơn vị VND/ngày |
| `trangthai` | VARCHAR(20) | DEFAULT 'SAN_SANG' | SAN_SANG / DANG_THUE / BAO_TRI |

### 📄 `hopdong`

| Cột | Kiểu | Ràng buộc | Ghi chú |
|---|---|---|---|
| `id` | INT | PK, AUTO_INCREMENT | |
| `makh` | INT | FK → khachhang.id | |
| `maxe` | INT | FK → xemay.id | |
| `manv` | INT | FK → nhanvien.id | |
| `ngaythue` | DATE | NOT NULL | |
| `ngaytra_dukien` | DATE | NOT NULL | |
| `ngaytra_thucte` | DATE | NULL | Null khi chưa trả |
| `tongtien` | DECIMAL(12,0) | DEFAULT 0 | Tự tính khi trả xe |
| `trangthai` | VARCHAR(20) | DEFAULT 'DANG_THUE' | DANG_THUE / DA_TRA |

## 3. Logic tính tiền (sẽ code trong DAO)

```
sốNgày = ngaytra_thucte - ngaythue
tongtien = sốNgày × giathue

Nếu ngaytra_thucte > ngaytra_dukien:
    tongtien += (số ngày trễ) × giathue × 1.5   ← phụ phí trả trễ 150%
```

## 4. Dữ liệu mẫu cần insert

- **1 nhân viên admin**: `admin` / `123456`
- **5 xe máy**: 2 Honda Wave, 1 Yamaha Exciter, 1 Honda Vision, 1 Honda SH
- **3 khách hàng** mẫu
- **0 hợp đồng** (để demo thao tác thêm mới)

## 5. Checklist

- [ ] Tạo file `src/database/quanlychothuexe.sql`
- [ ] Có lệnh `DROP DATABASE IF EXISTS` + `CREATE DATABASE` (chạy lại nhiều lần được)
- [ ] 4 bảng tạo đúng + khoá ngoại đầy đủ
- [ ] Insert dữ liệu mẫu
- [ ] Mở MySQL Workbench → Run script → không lỗi
- [ ] `SELECT * FROM xemay;` → thấy 5 xe

## 6. Lệnh chạy script

**Cách 1 — phpMyAdmin (XAMPP)**: mở http://localhost/phpmyadmin → tab SQL → dán nội dung → Go

**Cách 2 — MySQL Workbench**: mở file `.sql` → bấm ⚡ (Execute)

**Cách 3 — Command line**:
```bash
mysql -u root -p < src/database/quanlychothuexe.sql
```

> ⚠️ Tên DB trong file là `quanlychothuexe` (khớp với URL trong `KetNoiCSDL.java`).
