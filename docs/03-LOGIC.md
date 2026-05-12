# ⚙️ PHẦN 2 — KẾT NỐI DB & CLASS DỮ LIỆU

> Tất cả file ở phần này nằm trong `src/database/` → **bắt buộc** khai báo `package database;` ở dòng đầu tiên.

## 1. Sơ đồ luồng

```
   gui.Form*.java  ──gọi──►  database.KetNoiCSDL.getConnection()  ──JDBC──►  MySQL
        │
        ▼
   database.XeMay / KhachHang / HopDong / NhanVien   (class dữ liệu)
```

Không tách lớp DAO riêng — viết SQL trực tiếp ngay trong các Form. Đơn giản, dễ hiểu cho bài tập.

## 2. `src/database/KetNoiCSDL.java`

```java
package database;

import java.sql.*;

public class KetNoiCSDL {
    private static final String URL  = "jdbc:mysql://localhost:3306/quanlychothuexe?useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
    private static final String USER = "root";
    private static final String PASS = "";   // ← Sửa mật khẩu MySQL của bạn

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
```

**Test**: tạm thời thêm `main()` để in `"Kết nối thành công"`.

## 3. Class dữ liệu (POJO)

Mỗi class tương ứng 1 bảng — chỉ có thuộc tính + constructor + getter/setter.

**Ví dụ `src/database/XeMay.java`**:
```java
package database;

public class XeMay {
    private int id;
    private String bienso;
    private String hangxe;
    private String model;
    private long giathue;
    private String trangthai;

    public XeMay() { }
    public XeMay(int id, String bienso, String hangxe, String model, long giathue, String trangthai) {
        // gán thuộc tính
    }

    // getter / setter cho từng thuộc tính
}
```

Tương tự cho `NhanVien`, `KhachHang`, `HopDong` (đều có `package database;` ở đầu).

## 4. Cách viết SQL trong Form (đặt trong `gui/Form*.java`)

Trong file Form nhớ `import database.KetNoiCSDL;` và `import database.XeMay;` (etc).

Dùng `PreparedStatement` (an toàn, tránh SQL Injection) + `try-with-resources`:

```java
// Lấy danh sách xe
String sql = "SELECT * FROM xemay";
try (Connection con = KetNoiCSDL.getConnection();
     PreparedStatement ps = con.prepareStatement(sql);
     ResultSet rs = ps.executeQuery()) {
    while (rs.next()) {
        int id = rs.getInt("id");
        String bienso = rs.getString("bienso");
        // ... đổ vào JTable
    }
} catch (SQLException e) {
    JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
}
```

```java
// Thêm xe
String sql = "INSERT INTO xemay(bienso, hangxe, model, giathue, trangthai) VALUES (?,?,?,?,?)";
try (Connection con = KetNoiCSDL.getConnection();
     PreparedStatement ps = con.prepareStatement(sql)) {
    ps.setString(1, txtBienSo.getText());
    ps.setString(2, txtHangXe.getText());
    ps.setString(3, txtModel.getText());
    ps.setLong(4, Long.parseLong(txtGia.getText()));
    ps.setString(5, "SAN_SANG");
    ps.executeUpdate();
}
```

## 5. Đăng nhập (đặt trong `gui/FormDangNhap.java`)

```java
String sql = "SELECT * FROM nhanvien WHERE tendangnhap=? AND matkhau=?";
// ... nếu rs.next() == true thì đăng nhập thành công
```

## 6. Công thức tính tiền (đặt trong `gui/FormHopDong.java` — nút "Trả xe")

```java
long soNgay = ChronoUnit.DAYS.between(ngayThue, ngayTraThucTe);
if (soNgay <= 0) soNgay = 1;

long tongTien = soNgay * giaThue;

if (ngayTraThucTe.isAfter(ngayTraDuKien)) {
    long soNgayTre = ChronoUnit.DAYS.between(ngayTraDuKien, ngayTraThucTe);
    tongTien += (long)(soNgayTre * giaThue * 1.5);   // phụ phí 150%
}
```

## 7. Checklist

- [ ] `database/KetNoiCSDL.java` — test in `"Kết nối thành công"`
- [ ] `database/NhanVien.java`
- [ ] `database/KhachHang.java`
- [ ] `database/XeMay.java`
- [ ] `database/HopDong.java`
