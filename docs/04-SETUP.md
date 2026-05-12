# 🛠️ HƯỚNG DẪN CÀI ĐẶT & CHẠY DỰ ÁN

> Đọc file này **TRƯỚC TIÊN**. Làm theo đúng thứ tự 6 bước dưới đây.

---

## ✅ Yêu cầu môi trường

| Phần mềm | Phiên bản | Link |
|---|---|---|
| JDK | 11 trở lên (khuyến nghị 17 hoặc 21) | https://adoptium.net |
| MySQL Server | 8.x | https://dev.mysql.com/downloads/mysql/ |
| MySQL Workbench (GUI để chạy SQL) | 8.x | https://dev.mysql.com/downloads/workbench/ |
| IDE | IntelliJ IDEA Community (free) | https://www.jetbrains.com/idea/download |
| JDBC Driver | mysql-connector-j 8.3.0 | https://dev.mysql.com/downloads/connector/j/ |

> 💡 Nếu lười cài riêng → dùng **XAMPP** (có sẵn MySQL): https://www.apachefriends.org

---

## 🔧 BƯỚC 1 — Cài MySQL & tạo database

### Cách A: Dùng XAMPP (dễ nhất)
1. Cài XAMPP → mở XAMPP Control Panel → bấm **Start** ở dòng **MySQL**
2. Mở phpMyAdmin: http://localhost/phpmyadmin
3. Tab **SQL** → dán nội dung file `src/database/quanlychothuexe.sql` → bấm **Go**

### Cách B: MySQL Server + Workbench
1. Cài MySQL Community Server → đặt password cho user `root` (NHỚ password này!)
2. Cài MySQL Workbench → tạo connection tới `localhost:3306`
3. File → Open SQL Script → chọn `src/database/quanlychothuexe.sql`
4. Bấm ⚡ (Execute) để chạy

**Kiểm tra**: chạy `SELECT * FROM quanlychothuexe.xemay;` → thấy 5 xe = OK.

---

## 📦 BƯỚC 2 — Tải JDBC Driver

1. Vào https://dev.mysql.com/downloads/connector/j/
2. Chọn **Operating System: Platform Independent**
3. Tải file `.zip` (KHÔNG tải `.msi`)
4. Giải nén → lấy file `mysql-connector-j-8.x.x.jar`
5. Bỏ file `.jar` vào thư mục `lib/` của project

---

## 💻 BƯỚC 3 — Mở project trong IntelliJ IDEA

1. Mở IntelliJ → **Open** → chọn thư mục `D:\Java\QuanLychothuexe`
2. Đợi IntelliJ index xong

### Add JDBC driver vào project
1. `File` → `Project Structure` (Ctrl+Alt+Shift+S)
2. Chọn **Libraries** ở cột trái → bấm **+** → **Java**
3. Chọn file `lib/mysql-connector-j-8.x.x.jar` → OK
4. Apply → OK

### Đánh dấu thư mục `src` là Source Root
1. Chuột phải vào `src/` → **Mark Directory as** → **Sources Root**
   (Nếu IntelliJ đã tự nhận thì bỏ qua)

---

## 🔑 BƯỚC 4 — Cấu hình kết nối DB

Mở file `src/database/KetNoiCSDL.java`, sửa 3 dòng:

```java
private static final String URL  = "jdbc:mysql://localhost:3306/quanlychothuexe?useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
private static final String USER = "root";        // ← Tên user MySQL của bạn
private static final String PASS = "";            // ← Mật khẩu MySQL của bạn
```

| Trường hợp | USER | PASS |
|---|---|---|
| XAMPP mặc định | `root` | `` (để trống) |
| MySQL Server tự cài | `root` | (password bạn đã đặt lúc cài) |

---

## ▶️ BƯỚC 5 — Test kết nối (TRƯỚC khi code GUI)

Tạm thời thêm hàm `main` vào `src/database/KetNoiCSDL.java`:
```java
public static void main(String[] args) {
    try (var con = getConnection()) {
        System.out.println("✅ Kết nối MySQL thành công!");
    } catch (Exception e) {
        System.out.println("❌ Lỗi: " + e.getMessage());
    }
}
```
Bấm Run ▶️. Nếu in `✅` = OK, tiếp tục code. Nếu lỗi → xem mục Troubleshooting bên dưới.

---

## 🚀 BƯỚC 6 — Chạy ứng dụng

1. Run file `src/Main.java`
2. Cửa sổ **LoginFrame** hiện ra
3. Đăng nhập bằng tài khoản mẫu:
   - **Username**: `admin`
   - **Password**: `123456`
4. Vào MainFrame → thao tác các tab

---

## 🐛 Troubleshooting (lỗi thường gặp)

### Lỗi: `ClassNotFoundException: com.mysql.cj.jdbc.Driver`
→ Chưa add file `.jar` vào Libraries. Quay lại BƯỚC 3.

### Lỗi: `Communications link failure`
→ MySQL Server chưa chạy. Mở XAMPP → Start MySQL.

### Lỗi: `Access denied for user 'root'@'localhost'`
→ Sai mật khẩu. Sửa lại `PASS` trong `DBConnection.java`.

### Lỗi: `Unknown database 'quanlychothuexe'`
→ Chưa chạy script SQL. Quay lại BƯỚC 1.

### Lỗi tiếng Việt bị `???`
→ Thiếu `characterEncoding=utf8` trong URL. Kiểm tra `src/database/KetNoiCSDL.java`.

### Lỗi: `The server time zone value is unrecognized`
→ Thiếu `serverTimezone=UTC` trong URL.

---

## 📤 Đóng gói nộp bài

1. Đóng IntelliJ
2. Xoá thư mục `out/` hoặc `target/` (nếu có) — chỉ giữ source
3. Chọn các thư mục: `src/`, `lib/`, `database/`, `docs/`, `README.md`
4. Click chuột phải → **Send to** → **Compressed (zipped) folder**
5. Đặt tên: `NhomXX_QuanLyChoThueXeMay.zip`
6. Nộp lên Assignments **"Bài Tập Nhóm Lập Trình Java"**
