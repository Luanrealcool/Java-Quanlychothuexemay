# 🖥️ PHẦN 3 — GIAO DIỆN (Java Swing)

> Tất cả file ở phần này nằm trong `src/gui/` → **bắt buộc** khai báo `package gui;` ở dòng đầu tiên, kèm `import database.*;` khi cần dùng `KetNoiCSDL`, `XeMay`, `KhachHang`, ...
>
> Chỉ dùng component có sẵn trong JDK: `JFrame`, `JPanel`, `JButton`, `JLabel`, `JTextField`, `JPasswordField`, `JTable`, `JTabbedPane`, `JComboBox`, `JOptionPane`. **Không** dùng thư viện ngoài.

## 1. Sơ đồ điều hướng

```
   FormDangNhap  ──(login OK)──►  FormChinh (JTabbedPane)
                                       ├── Tab "Xe máy"     → FormXeMay
                                       ├── Tab "Khách hàng" → FormKhachHang
                                       ├── Tab "Hợp đồng"   → FormHopDong
                                       └── Tab "Thống kê"   → FormThongKe
```

> 💡 `FormChinh` extends `JFrame`. Các Form còn lại có thể extends `JPanel` để nhét vào tab, hoặc cũng làm `JFrame` riêng — tuỳ nhóm chọn. **Khuyên dùng `JPanel`** để đơn giản.

## 2. `FormDangNhap.java`

Kích thước nhỏ ~ 350×200.

```
┌─────────────────────────────┐
│  ĐĂNG NHẬP HỆ THỐNG         │
├─────────────────────────────┤
│  Tên đăng nhập: [_________] │
│  Mật khẩu:      [_________] │
│      [ Đăng nhập ] [ Thoát ]│
└─────────────────────────────┘
```

**Sự kiện**:
- Bấm "Đăng nhập" → truy vấn bảng `nhanvien` → đúng thì `new FormChinh().setVisible(true)` + `this.dispose()`.

## 3. `FormChinh.java`

Kích thước 900×600, chứa `JTabbedPane`.

```
┌────────────────────────────────────────────────────┐
│  Quản lý cho thuê xe máy   |   Xin chào: admin     │
├────────────────────────────────────────────────────┤
│ [Xe máy] [Khách hàng] [Hợp đồng] [Thống kê]        │
├────────────────────────────────────────────────────┤
│                                                    │
│           (panel của tab đang chọn)                │
│                                                    │
└────────────────────────────────────────────────────┘
```

## 4. `FormXeMay.java` (mẫu chung cho các form CRUD)

`BorderLayout`: NORTH = form nhập, CENTER = JTable, SOUTH = nút.

```
┌────────────────────────────────────────────┐
│ Biển số: [____]    Hãng: [____]            │
│ Model:   [____]    Giá:  [____]            │
│ Trạng thái: [SAN_SANG ▼]                   │
├────────────────────────────────────────────┤
│ ID │ Biển số │ Hãng │ Model │ Giá │ TT     │
│  1 │ 29A1... │Honda │ Wave  │100k │Sẵn sàng│  ← JTable
│  2 │ ...                                   │
├────────────────────────────────────────────┤
│ [Thêm] [Sửa] [Xoá] [Tải lại]               │
│ Tìm: [______] [Tìm]                        │
└────────────────────────────────────────────┘
```

**Sự kiện**:
- Click 1 dòng JTable → đổ dữ liệu lên form
- Thêm / Sửa / Xoá → gọi SQL → reload JTable
- Tìm → `SELECT ... WHERE bienso LIKE ?`

## 5. `FormKhachHang.java`

Giống `FormXeMay`. Cột: ID / CMND / Họ tên / SĐT / Địa chỉ.

## 6. `FormHopDong.java`

Chia 2 phần (dùng 2 `JPanel`):

**A. Tạo hợp đồng (thuê xe)**:
```
Khách hàng: [JComboBox - load từ bảng khachhang] ▼
Xe máy:     [JComboBox - chỉ load xe trangthai=SAN_SANG] ▼
Ngày thuê:        [yyyy-MM-dd]
Ngày trả dự kiến: [yyyy-MM-dd]
[ Tạo hợp đồng ]
```
→ Sau khi tạo: `UPDATE xemay SET trangthai='DANG_THUE' WHERE id=?`.

**B. Danh sách hợp đồng + Trả xe**:
```
JTable: ID | Khách | Xe | Ngày thuê | Trả DK | Trả TT | Tổng tiền | TT
[ Trả xe ]
```
→ Chọn 1 hợp đồng `DANG_THUE` → bấm "Trả xe" → popup nhập ngày trả thực tế → tính `tongtien` → `UPDATE` xe về `SAN_SANG`.

## 7. `FormThongKe.java`

```
Từ ngày: [yyyy-MM-dd]   Đến ngày: [yyyy-MM-dd]   [Thống kê]

Tổng doanh thu: ______________ VNĐ

JTable: ID | Khách hàng | Xe | Ngày trả | Tổng tiền
```

SQL: `SELECT SUM(tongtien) FROM hopdong WHERE ngaytra_thucte BETWEEN ? AND ? AND trangthai='DA_TRA'`

## 8. Mẹo JTable

```java
String[] cols = {"ID","Biển số","Hãng","Model","Giá","Trạng thái"};
DefaultTableModel model = new DefaultTableModel(cols, 0);
JTable table = new JTable(model);

// Reload data
model.setRowCount(0);
// ... duyệt ResultSet → model.addRow(new Object[]{...});

// Bắt sự kiện click dòng
table.getSelectionModel().addListSelectionListener(e -> {
    int row = table.getSelectedRow();
    if (row >= 0) {
        txtBienSo.setText(model.getValueAt(row, 1).toString());
        // ...
    }
});
```

## 9. Format

- Tiền: `String.format("%,d VNĐ", soTien)` → `100,000 VNĐ`
- Ngày người dùng nhập: `LocalDate.parse(txtNgay.getText())` (format `yyyy-MM-dd`)

## 10. Checklist

- [ ] `FormDangNhap` — login đúng/sai đều xử lý
- [ ] `FormChinh` — có 4 tab
- [ ] `FormXeMay` — CRUD + tìm
- [ ] `FormKhachHang` — CRUD + tìm
- [ ] `FormHopDong` — thuê xe + trả xe (xe đổi trạng thái đúng)
- [ ] `FormThongKe` — doanh thu theo khoảng ngày
- [ ] `Main.java` (ở `src/`, **không có package**) — `new gui.FormDangNhap().setVisible(true);`
