# 📊 BẢNG THEO DÕI TIẾN ĐỘ

> Tick `[x]` vào ô bên dưới khi xong mỗi phần.

---

## 🗄️ Phần 1 — DATABASE (xem [`01-DATABASE.md`](01-DATABASE.md))

- [x] `src/database/quanlychothuexe.sql` — viết script `CREATE DATABASE` + 4 bảng
- [x] Insert dữ liệu mẫu (1 admin + 5 xe + 3 khách)
- [x] Chạy script trong MySQL Workbench / phpMyAdmin → không lỗi
- [x] `SELECT * FROM xemay;` → thấy 5 xe

## ⚙️ Phần 2 — KẾT NỐI & CLASS DỮ LIỆU (xem [`03-LOGIC.md`](03-LOGIC.md))

Tất cả file dưới đây có `package database;` ở đầu.

- [x] `src/database/KetNoiCSDL.java` — viết hàm `getConnection()`
- [x] Test kết nối in `"Kết nối thành công"`
- [x] `src/database/NhanVien.java` — class chứa dữ liệu
- [x] `src/database/KhachHang.java`
- [x] `src/database/XeMay.java`
- [x] `src/database/HopDong.java`

## 🖥️ Phần 3 — GIAO DIỆN (xem [`02-GUI.md`](02-GUI.md))

Tất cả file dưới đây có `package gui;` ở đầu, dùng `import database.*;`.

- [x] `src/gui/FormDangNhap.java` — login bằng SQL bảng `nhanvien`
- [x] `src/gui/FormChinh.java` — cửa sổ chính có JTabbedPane
- [x] `src/gui/FormXeMay.java` — CRUD + search bằng SQL
- [x] `src/gui/FormKhachHang.java` — CRUD + search bằng SQL
- [x] `src/gui/FormHopDong.java` — Thuê xe + Trả xe (có transaction)
- [x] `src/gui/FormThongKe.java` — Doanh thu theo khoảng ngày bằng SQL
- [x] `src/Main.java` — chạy `new gui.FormDangNhap().setVisible(true)`

## 📦 Phần 4 — HOÀN THIỆN & NỘP

- [ ] Test tất cả chức năng (đăng nhập → thêm khách → thuê xe → trả xe → thống kê)
- [ ] Điền tên thành viên + đóng góp vào `README.md`
- [ ] Export file SQL cuối cùng
- [ ] Nén thư mục thành `.zip`
- [ ] Nộp lên Assignments "Bài Tập Nhóm Lập Trình Java"

---

## 🗓️ Lịch sử cập nhật

| Ngày | Người làm | Nội dung |
|---|---|---|
| _(điền)_ | _(tên)_ | _(VD: Xong file SQL)_ |
