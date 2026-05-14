# 👤 PHẦN MỞ RỘNG — TÀI KHOẢN ĐĂNG NHẬP CHO KHÁCH HÀNG

> Tính năng tự chọn — chỉ làm nếu muốn nâng cấp. Không bắt buộc cho bài tập cơ bản.

## 1. Mục tiêu

Cho phép **khách hàng tự đăng nhập** và **tự đặt thuê xe** mà không cần nhân viên thao tác giúp.

## 2. Phân quyền sau khi nâng cấp

| Vai trò | Đăng nhập | Quyền hạn |
|---|---|---|
| **Nhân viên** | `tendangnhap` của bảng `nhanvien` | Quản lý xe, khách, hợp đồng, thống kê. Xử lý **trả xe**. |
| **Khách hàng** | `tendangnhap` của bảng `khachhang` (cột mới) | Xem xe sẵn sàng, **đặt thuê** xe, xem hợp đồng của mình. KHÔNG quản lý gì khác. KHÔNG tự trả xe (phải mang xe ra cửa hàng để nhân viên xử lý). |

## 3. Thay đổi CSDL

### Thêm 2 cột vào bảng `khachhang`:
```sql
ALTER TABLE khachhang
    ADD COLUMN tendangnhap VARCHAR(50) UNIQUE,
    ADD COLUMN matkhau VARCHAR(100);
```

### Sửa bảng `hopdong` cho phép `manv` NULL:
```sql
ALTER TABLE hopdong MODIFY COLUMN manv INT NULL;
```
*Lý do*: khi khách tự đặt thuê, không có nhân viên xử lý → `manv = NULL`.

### Insert tài khoản khách mẫu:
```sql
UPDATE khachhang SET tendangnhap='kh001', matkhau='123' WHERE cmnd='012345678';
UPDATE khachhang SET tendangnhap='kh002', matkhau='123' WHERE cmnd='098765432';
```

## 4. Luồng đăng nhập mới

```
   Người dùng nhập tendangnhap + matkhau
              │
              ▼
   1) Tìm trong bảng `nhanvien` trước
              │
        ┌─────┴─────┐
        ▼           ▼
      Có          Không
        │           │
        ▼           ▼
   FormChinh   2) Tìm tiếp trong bảng `khachhang`
   (nhân viên)       │
                ┌────┴────┐
                ▼         ▼
              Có        Không
                │         │
                ▼         ▼
           FormChinhKhach  Báo sai
           (khách hàng)
```

## 5. File cần tạo MỚI

| File | Mục đích |
|---|---|
| `src/gui/FormChinhKhach.java` | Cửa sổ chính của khách (2 tab) |
| `src/gui/FormThueXeKhach.java` | Tab "Đặt thuê xe": chọn xe sẵn sàng + ngày → tạo HĐ |
| `src/gui/FormHopDongCuaToi.java` | Tab "Hợp đồng của tôi": xem các HĐ của chính khách đó |

## 6. File cần SỬA

| File | Sửa gì |
|---|---|
| `KhachHang.java` (POJO) | Thêm 2 thuộc tính `tenDangNhap`, `matKhau` + getter/setter |
| `FormDangNhap.java` | Sau khi check `nhanvien` không thấy, check tiếp `khachhang`. Tuỳ kết quả mở `FormChinh` hoặc `FormChinhKhach`. |
| `FormKhachHang.java` (của nhân viên) | Thêm 2 ô nhập "Tên đăng nhập" + "Mật khẩu" vào form thêm khách hàng. Khi nhân viên thêm khách mới có thể tạo luôn tài khoản. |

## 7. Thiết kế `FormChinhKhach.java`

```
┌──────────────────────────────────────────────────┐
│  Xin chào: Nguyễn Văn A      [Đăng xuất]         │
├──────────────────────────────────────────────────┤
│ [Đặt thuê xe] [Hợp đồng của tôi]                 │
├──────────────────────────────────────────────────┤
│                                                  │
│            (tab đang chọn)                       │
│                                                  │
└──────────────────────────────────────────────────┘
```

## 8. Thiết kế `FormThueXeKhach.java`

```
┌──────────────────────────────────────────────┐
│ Danh sách xe đang sẵn sàng:                  │
│ ┌──────────────────────────────────────────┐ │
│ │ ID │ Biển số │ Hãng │ Model │ Giá │      │ │
│ │  1 │ 29A1... │Honda │ Wave  │100k│      │ │  ← JTable
│ │  2 │ ...                                 │ │
│ └──────────────────────────────────────────┘ │
├──────────────────────────────────────────────┤
│ Xe đã chọn: 29A1-12345 Honda Wave            │
│ Ngày thuê:        [yyyy-MM-dd]               │
│ Ngày trả dự kiến: [yyyy-MM-dd]               │
│ Số ngày: 3   Tạm tính: 300,000 VNĐ           │
├──────────────────────────────────────────────┤
│         [ Đặt thuê ] [ Làm mới ]             │
└──────────────────────────────────────────────┘
```

**SQL khi đặt thuê** (giống FormHopDong nhưng `manv = NULL`):
```sql
INSERT INTO hopdong(makh, maxe, manv, ngaythue, ngaytra_dukien, trangthai)
VALUES (?, ?, NULL, ?, ?, 'DANG_THUE');
UPDATE xemay SET trangthai='DANG_THUE' WHERE id=?;
```

## 9. Thiết kế `FormHopDongCuaToi.java`

```
┌─────────────────────────────────────────────────────┐
│ Hợp đồng của bạn:                                   │
│ ┌─────────────────────────────────────────────────┐ │
│ │ID│Xe        │Thuê     │Trả DK   │Trả TT │Tiền│TT│ │
│ │ 1│Honda Wave│2026-05-01│2026-05-04│      │   │ĐT│ │
│ │ 2│Yamaha Ex.│2026-04-10│2026-04-13│04-13 │450k│ĐT│ │
│ └─────────────────────────────────────────────────┘ │
├─────────────────────────────────────────────────────┤
│                       [ Làm mới ]                   │
└─────────────────────────────────────────────────────┘
```

**SQL** (chỉ load HĐ của chính khách này):
```sql
SELECT h.id, CONCAT(x.hangxe,' ',x.model) AS xe, h.ngaythue,
       h.ngaytra_dukien, h.ngaytra_thucte, h.tongtien, h.trangthai
FROM hopdong h JOIN xemay x ON h.maxe = x.id
WHERE h.makh = ?   -- ← id của khách đang đăng nhập
ORDER BY h.id DESC;
```

## 10. Lưu ý quan trọng

- **Khách không tự trả xe** — chỉ có nhân viên mới có nút "Trả xe". Khách chỉ XEM được trạng thái.
- **Khách không sửa thông tin xe / khách khác** — không có tab quản lý.
- **Tài khoản mặc định** trong dữ liệu mẫu (sau khi ALTER TABLE):
  - Nhân viên: `admin` / `123456`
  - Khách: `kh001` / `123` (Nguyễn Văn A)
  - Khách: `kh002` / `123` (Trần Thị B)

## 11. Checklist khi triển khai

- [ ] Chạy script ALTER TABLE để thêm cột vào `khachhang` và `hopdong`
- [ ] Update sample data (gán username/password cho 1-2 khách)
- [ ] Sửa `KhachHang.java` thêm 2 thuộc tính
- [ ] Sửa `FormDangNhap.java` — check 2 bảng
- [ ] Sửa `FormKhachHang.java` — thêm 2 ô nhập username/password
- [ ] Tạo `FormChinhKhach.java`
- [ ] Tạo `FormThueXeKhach.java`
- [ ] Tạo `FormHopDongCuaToi.java`
- [ ] Test: đăng nhập `admin/123456` → vào FormChinh (4 tab)
- [ ] Test: đăng nhập `kh001/123` → vào FormChinhKhach (2 tab)
- [ ] Test: khách `kh001` chỉ thấy HĐ của mình, không thấy của `kh002`

## 12. Đánh giá độ phức tạp

| Tiêu chí | Mức |
|---|---|
| Số file mới | 3 |
| Số file sửa | 3 |
| Thay đổi DB | 2 lệnh ALTER + UPDATE |
| Thời gian ước tính | ~1-2 giờ làm |
| Có ảnh hưởng tính năng cũ? | **Không** — chỉ thêm vào, không phá vỡ luồng nhân viên |
| Đáng làm cho bài tập? | Nếu muốn điểm cao hoặc thầy hỏi sâu thì có. Bài cơ bản thì **không cần**. |

---

> 💡 **Khi nào quyết định làm**: báo "OK làm phần khách hàng" → tôi sẽ tick checklist Phần 11 từng bước.
