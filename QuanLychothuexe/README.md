# 🏍️ QUẢN LÝ CHO THUÊ XE MÁY DU LỊCH

> Bài tập nhóm môn **Lập trình Java**

## 📌 Thông tin dự án

| Mục | Nội dung |
|---|---|
| Tên project | QuanLyChoThueXeMay |
| Ngôn ngữ | Java SE (thuần) |
| Giao diện | Java Swing (built-in) |
| CSDL | MySQL |
| Kết nối | JDBC (mysql-connector-j) |

## 👥 Thành viên nhóm

| STT | Họ tên | MSSV | Đóng góp |
|---|---|---|---|
| 1 | _(điền tên)_ | _(MSSV)_ | _(VD: làm CSDL, FormXeMay)_ |
| 2 | _(điền tên)_ | _(MSSV)_ | _(VD: FormDangNhap, FormChinh, FormKhachHang)_ |
| 3 | _(điền tên)_ | _(MSSV)_ | _(VD: FormHopDong, FormThongKe, viết README)_ |

## 🎯 Chức năng

1. Đăng nhập nhân viên
2. Quản lý xe máy (Thêm / Sửa / Xoá / Tìm)
3. Quản lý khách hàng (Thêm / Sửa / Xoá / Tìm)
4. Cho thuê xe (lập hợp đồng, tự cập nhật trạng thái xe)
5. Trả xe (tính tiền, có phụ phí nếu trả trễ)
6. Thống kê doanh thu theo khoảng ngày

## 📂 Cấu trúc dự án

```
QuanLyChoThueXeMay/
├── lib/                              ← đặt mysql-connector-j-x.x.x.jar
├── src/
│   ├── Main.java                     chạy chương trình
│   │
│   ├── gui/                          📺 PACKAGE GIAO DIỆN
│   │   ├── FormDangNhap.java         đăng nhập
│   │   ├── FormChinh.java            cửa sổ chính (tab)
│   │   ├── FormXeMay.java            quản lý xe
│   │   ├── FormKhachHang.java        quản lý khách
│   │   ├── FormHopDong.java          cho thuê / trả xe
│   │   └── FormThongKe.java          thống kê doanh thu
│   │
│   └── database/                     🗄️ PACKAGE CƠ SỞ DỮ LIỆU
│       ├── quanlychothuexe.sql       script tạo CSDL
│       ├── KetNoiCSDL.java           kết nối MySQL
│       ├── NhanVien.java             class chứa dữ liệu
│       ├── KhachHang.java
│       ├── XeMay.java
│       └── HopDong.java
│
├── docs/                             📖 tài liệu chi tiết
└── README.md
```

**Quy ước package**:
- Mỗi file trong `src/gui/` → khai báo `package gui;` ở dòng đầu
- Mỗi file trong `src/database/` → khai báo `package database;` ở dòng đầu
- File `Main.java` ở `src/` → không có khai báo `package`
- Khi `gui` cần dùng class trong `database` → thêm `import database.XeMay;` (ví dụ)

## 📖 Đọc tài liệu theo thứ tự

1. [`docs/04-SETUP.md`](docs/04-SETUP.md) — cài MySQL, tải driver, mở project
2. [`docs/PROGRESS.md`](docs/PROGRESS.md) — checklist tiến độ, tick khi xong
3. [`docs/01-DATABASE.md`](docs/01-DATABASE.md) — thiết kế bảng + dữ liệu mẫu
4. [`docs/03-LOGIC.md`](docs/03-LOGIC.md) — class dữ liệu + cách viết SQL
5. [`docs/02-GUI.md`](docs/02-GUI.md) — bố cục các Form Swing

## 🚀 Thứ tự làm

**SQL → KetNoiCSDL → Class dữ liệu → Form** (đăng nhập → chính → các tab) → **Main**
