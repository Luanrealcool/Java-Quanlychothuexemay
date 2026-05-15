# 🏗️ KẾ HOẠCH NÂNG CẤP — KIẾN TRÚC CHUẨN MULTI-LAYER

> Tài liệu này định nghĩa kiến trúc, chuẩn code, và lộ trình refactor toàn bộ dự án từ "bài tập cơ bản" thành dự án **production-grade** mà vẫn dùng Java thuần + Swing + MySQL.

---

## 1. 🎯 Mục tiêu nâng cấp

| Khía cạnh | Hiện tại (basic) | **Mục tiêu (chuẩn chỉnh)** |
|---|---|---|
| Kiến trúc | 2-layer: GUI gọi thẳng SQL | **4-layer: GUI → BUS → DAO → DB** |
| Tách dữ liệu | POJO trộn lẫn cùng SQL | **DTO/Entity riêng** |
| Logic nghiệp vụ | Nằm trong Form | **Lớp BUS xử lý hết** |
| Validation | Inline `if (xxx.isEmpty())` | **Lớp Util validator chung** |
| Cấu hình DB | Hardcoded trong `KetNoiCSDL.java` | **File `config.properties`** |
| Lỗi | `JOptionPane` rải rác | **Custom Exception + handler** |
| Bảng nhiều dòng | Cuộn dọc duy nhất | **Phân trang + sort + filter** |
| Tiền/ngày | Format thủ công | **Util formatter chung** |
| Mật khẩu | Plain text | **Hash SHA-256** (vẫn không dùng lib ngoài) |
| Log | Không có | **Util Logger ghi file** |
| Audit | Không | **`created_at`, `updated_at`, `created_by`** |
| Test | Manual | **Smoke test script** (optional) |

---

## 2. 🏛️ Kiến trúc lớp (Layered Architecture)

```
┌────────────────────────────────────────────────────────────────┐
│                       PRESENTATION LAYER                       │
│   gui/  →  Form*.java, Dialog*.java                            │
│   Chỉ hiển thị và bắt sự kiện. KHÔNG chứa SQL, KHÔNG tính toán │
└─────────────────────────────┬──────────────────────────────────┘
                              │ gọi method của BUS
                              ▼
┌────────────────────────────────────────────────────────────────┐
│                      BUSINESS LOGIC LAYER                      │
│   bus/  →  XeMayBUS.java, KhachHangBUS.java, HopDongBUS.java   │
│   Validate input, tính toán (tiền, phụ phí), điều phối DAO,    │
│   quản lý transaction, áp dụng business rules                   │
└─────────────────────────────┬──────────────────────────────────┘
                              │ gọi method của DAO
                              ▼
┌────────────────────────────────────────────────────────────────┐
│                    DATA ACCESS LAYER                           │
│   dao/  →  XeMayDAO.java, KhachHangDAO.java, HopDongDAO.java   │
│   Chỉ SQL + PreparedStatement. Trả về DTO. KHÔNG có logic.     │
└─────────────────────────────┬──────────────────────────────────┘
                              │ JDBC
                              ▼
┌────────────────────────────────────────────────────────────────┐
│                          DATABASE                              │
│   MySQL — bảng + ràng buộc + index + trigger                   │
└────────────────────────────────────────────────────────────────┘

           ┌──────────────────────────────────────────┐
           │ Hỗ trợ ngang (dùng được ở mọi layer)     │
           ├──────────────────────────────────────────┤
           │ dto/      — POJO chỉ chứa dữ liệu        │
           │ util/     — Format, validate, hash, log  │
           │ config/   — Đọc file properties          │
           │ exception/— Custom exceptions            │
           │ constant/ — Hằng số (trạng thái, role)   │
           └──────────────────────────────────────────┘
```

### Quy tắc nghiêm ngặt
- ✅ **GUI** → chỉ gọi **BUS**, không được gọi DAO trực tiếp
- ✅ **BUS** → gọi **DAO**, gọi **Util**, throw exception
- ✅ **DAO** → chỉ làm SQL, trả về **DTO**
- ❌ Không bao giờ ngược: DAO không gọi BUS, BUS không gọi GUI

---

## 3. 📂 Cấu trúc thư mục mới

```
QuanLyChoThueXeMay/
├── lib/
│   └── mysql-connector-j-8.4.0.jar
├── config/
│   └── config.properties           ← URL DB, user, password, page size
├── logs/                            ← File log do app tự ghi
│   └── app.log
├── src/
│   ├── Main.java                    ← Entry point
│   │
│   ├── constant/                    ← 🔧 HẰNG SỐ
│   │   ├── TrangThaiXe.java         (SAN_SANG, DANG_THUE, BAO_TRI)
│   │   ├── TrangThaiHopDong.java    (DANG_THUE, DA_TRA, HUY)
│   │   ├── LoaiNguoiDung.java       (NHAN_VIEN, KHACH_HANG)
│   │   └── AppConstant.java         (PAGE_SIZE_DEFAULT = 20, etc.)
│   │
│   ├── config/                      ← ⚙️ CẤU HÌNH
│   │   └── AppConfig.java           (đọc config.properties)
│   │
│   ├── exception/                   ← 🚨 EXCEPTION
│   │   ├── BusinessException.java   (vi phạm rule nghiệp vụ)
│   │   ├── ValidationException.java (input không hợp lệ)
│   │   └── DataAccessException.java (lỗi DB)
│   │
│   ├── dto/                         ← 📦 DTO (Data Transfer Object)
│   │   ├── NhanVienDTO.java
│   │   ├── KhachHangDTO.java
│   │   ├── XeMayDTO.java
│   │   ├── HopDongDTO.java          (có thêm tên KH, biển xe để hiển thị)
│   │   ├── ThongKeDTO.java          (chứa kết quả thống kê)
│   │   └── PageResult.java          (kết quả phân trang: items + totalCount)
│   │
│   ├── util/                        ← 🛠️ TIỆN ÍCH
│   │   ├── KetNoiCSDL.java          (connection pool đơn giản hoặc singleton)
│   │   ├── ValidatorUtil.java       (kiểm tra CMND, số đt, số tiền...)
│   │   ├── FormatUtil.java          (format tiền, ngày, hoten)
│   │   ├── DateUtil.java            (parse, so sánh ngày)
│   │   ├── PasswordUtil.java        (hash SHA-256)
│   │   ├── LoggerUtil.java          (ghi log ra file)
│   │   └── ExportUtil.java          (xuất CSV - optional)
│   │
│   ├── dao/                         ← 🗄️ DATA ACCESS
│   │   ├── NhanVienDAO.java
│   │   ├── KhachHangDAO.java
│   │   ├── XeMayDAO.java
│   │   └── HopDongDAO.java
│   │   (Mỗi DAO có: getById, getAll, getPage(page,size,filter,sort),
│   │                count, insert, update, delete)
│   │
│   ├── bus/                         ← 💼 BUSINESS LOGIC
│   │   ├── AuthBUS.java             (đăng nhập, phân quyền)
│   │   ├── NhanVienBUS.java
│   │   ├── KhachHangBUS.java
│   │   ├── XeMayBUS.java
│   │   ├── HopDongBUS.java          (tạo HĐ, tính tiền, trả xe)
│   │   └── ThongKeBUS.java
│   │
│   └── gui/                         ← 🖥️ GIAO DIỆN
│       ├── GiaoDien.java            (theme: màu, font, style helper)
│       ├── FormDangNhap.java
│       ├── FormChinh.java
│       ├── FormChinhKhach.java
│       │
│       ├── xemay/                   (gom theo chức năng cho dễ tìm)
│       │   ├── PanelXeMay.java      (panel chính có table + phân trang)
│       │   └── DialogXeMay.java     (thêm/sửa)
│       ├── khachhang/
│       │   ├── PanelKhachHang.java
│       │   └── DialogKhachHang.java
│       ├── hopdong/
│       │   ├── PanelHopDong.java
│       │   ├── DialogTaoHopDong.java
│       │   └── DialogTraXe.java
│       ├── thongke/
│       │   └── PanelThongKe.java
│       ├── khach/
│       │   ├── PanelThueXe.java
│       │   └── PanelHopDongCuaToi.java
│       └── common/                  (component tái sử dụng)
│           ├── PhanTrangPanel.java  (component phân trang Prev | 1 2 3 | Next)
│           ├── ThanhTimKiem.java
│           └── StatCard.java
│
├── database/
│   ├── schema.sql                   ← Tạo bảng
│   ├── seed.sql                     ← Dữ liệu mẫu
│   └── migrations/                  ← Các phiên bản sửa schema
│       ├── 001_initial.sql
│       └── 002_add_audit_fields.sql
│
├── docs/                            ← Tài liệu
└── README.md
```

---

## 4. 🗄️ Database v2 — Thiết kế nâng cao

### 4.1. Bảng `nhanvien`
```sql
CREATE TABLE nhanvien (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tendangnhap VARCHAR(50) NOT NULL UNIQUE,
    matkhau_hash CHAR(64) NOT NULL,        -- SHA-256 hash
    hoten VARCHAR(100) NOT NULL,
    sdt VARCHAR(15),
    email VARCHAR(100),                     -- mới
    vaitro VARCHAR(20) NOT NULL DEFAULT 'NHAN_VIEN', -- NHAN_VIEN/QUAN_LY
    trangthai VARCHAR(20) NOT NULL DEFAULT 'HOAT_DONG', -- HOAT_DONG/KHOA
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tendangnhap (tendangnhap),
    INDEX idx_trangthai (trangthai)
);
```

### 4.2. Bảng `khachhang`
```sql
CREATE TABLE khachhang (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cmnd VARCHAR(20) NOT NULL UNIQUE,
    hoten VARCHAR(100) NOT NULL,
    sdt VARCHAR(15),
    email VARCHAR(100),
    diachi VARCHAR(200),
    tendangnhap VARCHAR(50) UNIQUE,
    matkhau_hash CHAR(64),
    ngaysinh DATE,
    gioitinh VARCHAR(10),                   -- NAM/NU/KHAC
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_cmnd (cmnd),
    INDEX idx_hoten (hoten),
    FULLTEXT INDEX ft_search (hoten, diachi)
);
```

### 4.3. Bảng `loaixe` (mới — chuẩn hóa)
```sql
CREATE TABLE loaixe (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ten VARCHAR(50) NOT NULL UNIQUE,        -- "Tay ga", "Số sàn", "Côn tay"
    mota VARCHAR(200)
);
```

### 4.4. Bảng `xemay`
```sql
CREATE TABLE xemay (
    id INT PRIMARY KEY AUTO_INCREMENT,
    bienso VARCHAR(20) NOT NULL UNIQUE,
    hangxe VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    maloaixe INT,                            -- FK loaixe
    namsx YEAR,
    mauxe VARCHAR(30),
    giathue DECIMAL(12,0) NOT NULL CHECK (giathue > 0),
    trangthai VARCHAR(20) NOT NULL DEFAULT 'SAN_SANG',
    ghichu VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (maloaixe) REFERENCES loaixe(id),
    INDEX idx_bienso (bienso),
    INDEX idx_trangthai (trangthai),
    INDEX idx_hangxe_model (hangxe, model)
);
```

### 4.5. Bảng `hopdong`
```sql
CREATE TABLE hopdong (
    id INT PRIMARY KEY AUTO_INCREMENT,
    masohopdong VARCHAR(20) NOT NULL UNIQUE, -- "HD-2026-0001"
    makh INT NOT NULL,
    maxe INT NOT NULL,
    manv INT,                                -- NULL nếu khách tự đặt
    ngaythue DATE NOT NULL,
    ngaytra_dukien DATE NOT NULL,
    ngaytra_thucte DATE,
    giathue_taithoidiem DECIMAL(12,0) NOT NULL, -- giá lock ở thời điểm thuê
    tiendaco DECIMAL(12,0) DEFAULT 0,        -- tiền đặt cọc
    tongtien DECIMAL(12,0) DEFAULT 0,
    phuphi_tre DECIMAL(12,0) DEFAULT 0,
    trangthai VARCHAR(20) NOT NULL DEFAULT 'DANG_THUE', -- DANG_THUE/DA_TRA/HUY
    lydo_huy VARCHAR(500),
    ghichu VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (makh) REFERENCES khachhang(id),
    FOREIGN KEY (maxe) REFERENCES xemay(id),
    FOREIGN KEY (manv) REFERENCES nhanvien(id),
    INDEX idx_masohd (masohopdong),
    INDEX idx_trangthai (trangthai),
    INDEX idx_ngaythue (ngaythue),
    INDEX idx_makh (makh),
    INDEX idx_maxe (maxe),
    CHECK (ngaytra_dukien > ngaythue),
    CHECK (ngaytra_thucte IS NULL OR ngaytra_thucte >= ngaythue)
);
```

### 4.6. View `v_doanhthu_theongay` (mới)
```sql
CREATE VIEW v_doanhthu_theongay AS
SELECT
    DATE(ngaytra_thucte) AS ngay,
    COUNT(*) AS so_hopdong,
    SUM(tongtien) AS doanh_thu
FROM hopdong
WHERE trangthai = 'DA_TRA'
GROUP BY DATE(ngaytra_thucte);
```

### 4.7. Trigger tự sinh mã HĐ (mới)
```sql
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
```

---

## 5. 📄 Phân trang — Giải pháp cho "quá nhiều dữ liệu"

### 5.1. Vấn đề
Nếu có 10,000 xe máy:
- Load hết về memory → app lag, treo
- JTable hiển thị 10k dòng → khó cuộn tìm
- Tốn băng thông DB

### 5.2. Giải pháp: phân trang DB-side với `LIMIT` + `OFFSET`

```sql
SELECT * FROM xemay
WHERE bienso LIKE ? OR hangxe LIKE ?    -- filter
ORDER BY id DESC                         -- sort
LIMIT 20 OFFSET 40                       -- page 3, size 20
```

### 5.3. Component `PhanTrangPanel`

```
┌────────────────────────────────────────────────────────────┐
│ Hiển thị 21-40 / 156    [⟪] [<]  1  2 [3] 4  5  [>] [⟫]   │
│                         Trang/dòng: [3] / 8    20/trang ▼  │
└────────────────────────────────────────────────────────────┘
```

**API**:
```java
public class PhanTrangPanel extends JPanel {
    void setTotalCount(int total);              // tổng số bản ghi
    void setPageSize(int size);                 // 10/20/50/100
    void setCurrentPage(int page);
    void addPageChangeListener(Consumer<Integer> cb);
}
```

### 5.4. DTO `PageResult`
```java
public class PageResult<T> {
    private List<T> items;
    private int totalCount;
    private int currentPage;
    private int pageSize;

    public int getTotalPages() {
        return (int) Math.ceil((double) totalCount / pageSize);
    }
}
```

### 5.5. DAO method
```java
public PageResult<XeMayDTO> getPage(int page, int size, String keyword, String sortBy) {
    // 1. Query COUNT(*) để tổng
    // 2. Query với LIMIT/OFFSET
    // 3. Trả PageResult
}
```

### 5.6. Các tính năng UX khác cho data nhiều
- ✅ **Sort theo cột**: click header → ORDER BY tương ứng
- ✅ **Filter nâng cao**: dropdown trạng thái, range giá, range ngày
- ✅ **Cuộn ngang khi nhiều cột**: `HORIZONTAL_SCROLLBAR_AS_NEEDED`
- ✅ **Resize cột**: cho user kéo thay đổi width
- ✅ **Đông cố cột đầu**: ID cố định khi cuộn ngang
- ✅ **Highlight dòng hover**: visual feedback
- ✅ **Loading indicator**: khi query lâu (>500ms hiện "Đang tải...")
- ✅ **Export CSV**: nếu cần in/báo cáo
- ✅ **Cache COUNT**: COUNT(*) rất tốn, cache 5s

---

## 6. ✅ Validation layer

### `ValidatorUtil.java`
```java
public class ValidatorUtil {
    public static void requireNotEmpty(String s, String fieldName) {
        if (s == null || s.trim().isEmpty())
            throw new ValidationException(fieldName + " không được trống");
    }

    public static void requirePositive(long n, String fieldName) {
        if (n <= 0)
            throw new ValidationException(fieldName + " phải > 0");
    }

    public static boolean isValidCmnd(String s) {
        return s != null && s.matches("^\\d{9}$|^\\d{12}$");
    }

    public static boolean isValidSdt(String s) {
        return s != null && s.matches("^(0|\\+84)[0-9]{9,10}$");
    }

    public static boolean isValidEmail(String s) {
        return s != null && s.matches("^[\\w.-]+@[\\w-]+\\.[\\w.-]+$");
    }

    public static boolean isValidBienSo(String s) {
        return s != null && s.matches("^\\d{2}[A-Z]\\d{1,2}-\\d{4,5}$");
    }
}
```

### Dùng ở BUS layer
```java
public void themXe(XeMayDTO xe) throws ValidationException {
    ValidatorUtil.requireNotEmpty(xe.getBienSo(), "Biển số");
    if (!ValidatorUtil.isValidBienSo(xe.getBienSo()))
        throw new ValidationException("Biển số không đúng định dạng");
    ValidatorUtil.requirePositive(xe.getGiaThue(), "Giá thuê");
    // ...
    xeMayDAO.insert(xe);
}
```

---

## 7. 🛠️ Util classes

| Class | Method chính | Mục đích |
|---|---|---|
| `FormatUtil` | `formatTien(long)`, `formatNgay(LocalDate)`, `formatSdt(String)` | Hiển thị đẹp |
| `DateUtil` | `parseNgay(String)`, `khoangNgay(d1,d2)`, `laNgayHopLe(d)` | Xử lý ngày |
| `PasswordUtil` | `hash(String pass)`, `verify(pass, hash)` | SHA-256 không cần lib |
| `LoggerUtil` | `info()`, `error(Throwable)`, `warn()` | Ghi `logs/app.log` |
| `ExportUtil` | `toCSV(table, file)`, `toExcel(...)` | Xuất báo cáo |
| `KetNoiCSDL` | `getConnection()` từ pool đơn giản (5 conn) | Tái sử dụng kết nối |

---

## 8. 🚨 Exception hierarchy

```
RuntimeException
    └── AppException                  (base)
        ├── ValidationException       (input sai)
        ├── BusinessException         (vi phạm rule, vd xoá xe đang thuê)
        └── DataAccessException       (lỗi DB)
```

### Handler chung trong GUI
```java
private void handleException(Exception ex) {
    if (ex instanceof ValidationException || ex instanceof BusinessException) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Cảnh báo",
            JOptionPane.WARNING_MESSAGE);
    } else {
        LoggerUtil.error("Lỗi không mong đợi", ex);
        JOptionPane.showMessageDialog(this,
            "Đã xảy ra lỗi. Vui lòng liên hệ quản trị viên.\nChi tiết: " + ex.getMessage(),
            "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
    }
}
```

---

## 9. ⚙️ Config file

### `config/config.properties`
```properties
# Database
db.url=jdbc:mysql://localhost:3306/quanlychothuexe?useSSL=false&serverTimezone=UTC&characterEncoding=utf8
db.user=root
db.password=Anime@1234
db.pool.size=5

# UI
ui.page.size.default=20
ui.page.size.options=10,20,50,100

# Business
business.phuphi.tre.percent=150
business.hopdong.prefix=HD

# Log
log.file=logs/app.log
log.level=INFO
```

### `AppConfig.java`
```java
public class AppConfig {
    private static Properties props;

    static {
        try (InputStream in = new FileInputStream("config/config.properties")) {
            props = new Properties();
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Không đọc được config", e);
        }
    }

    public static String get(String key) { return props.getProperty(key); }
    public static int getInt(String key) { return Integer.parseInt(props.getProperty(key)); }
}
```

---

## 10. 🔐 Password hashing

### `PasswordUtil.java` (không cần thư viện ngoài)
```java
public class PasswordUtil {
    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
```

DB lưu `matkhau_hash CHAR(64)`. Migration cần script update password cũ.

---

## 11. 📝 Audit fields & soft delete

Mỗi bảng có:
- `created_at DATETIME` (tự gán)
- `updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP` (tự cập nhật)
- (Optional) `deleted_at DATETIME` để soft-delete thay vì DELETE thật

→ Lợi: truy vết, audit log, không mất data.

---

## 12. 📋 Lộ trình refactor — 6 giai đoạn

### Giai đoạn 1 — Hạ tầng (1-2 tiếng)
- [ ] Tạo cấu trúc thư mục mới (constant/, config/, dto/, util/, dao/, bus/, exception/)
- [ ] Viết `AppConfig`, `KetNoiCSDL` (singleton), `LoggerUtil`
- [ ] Viết `AppException`, `ValidationException`, `BusinessException`
- [ ] Viết `FormatUtil`, `DateUtil`, `ValidatorUtil`, `PasswordUtil`
- [ ] Viết các class `constant/` (enum hằng số)
- [ ] Tạo `config.properties` + hướng dẫn

### Giai đoạn 2 — Database v2 (1 tiếng)
- [ ] Viết `database/schema.sql` mới (thêm audit fields, indexes, constraints)
- [ ] Viết `database/seed.sql` (dữ liệu mẫu nhiều hơn — 100 xe, 50 khách để test phân trang)
- [ ] Viết migration script chuyển từ v1 → v2
- [ ] Tạo trigger, view, stored procedure nếu cần
- [ ] Test schema chạy được, FK constraint hoạt động

### Giai đoạn 3 — DTO + DAO (2-3 tiếng)
- [ ] Viết các DTO mới với đủ field + builder pattern (optional)
- [ ] Viết DAO với:
  - `getById`, `getAll` (limit), `count`
  - `getPage(page, size, filter, sort)` → `PageResult<DTO>`
  - `insert`, `update`, `delete`
  - `existsByXxx` cho unique check
- [ ] Test DAO bằng main() đơn giản

### Giai đoạn 4 — BUS layer (2-3 tiếng)
- [ ] Viết BUS với business rules:
  - `XeMayBUS`: thêm/sửa/xoá xe (check biển số trùng, không xoá xe đang thuê)
  - `KhachHangBUS`: validate CMND duy nhất, format SĐT
  - `HopDongBUS`: `taoHopDong()` (validate xe sẵn sàng, transaction), `traXe()` (tính tiền)
  - `AuthBUS`: `dangNhap()` (hash + so sánh)
  - `ThongKeBUS`: các query thống kê
- [ ] Mỗi method throw exception đúng loại

### Giai đoạn 5 — Refactor GUI (3-4 tiếng)
- [ ] Tạo component dùng chung trong `gui/common/`:
  - `PhanTrangPanel` — phân trang
  - `StatCard` — card thống kê
  - `ThanhTimKiem` — search bar
- [ ] Refactor các Form/Panel:
  - Xoá hết SQL → chỉ gọi BUS
  - Thêm phân trang vào panel có table
  - Thêm sort theo cột (click header)
  - Thêm filter nâng cao (combobox trạng thái...)
- [ ] Centralize exception handler

### Giai đoạn 6 — Polish & Test (2 tiếng)
- [ ] Test scenario:
  - Login sai 3 lần → khoá tài khoản?
  - Tạo HĐ khi xe vừa được người khác thuê (race condition)
  - 1000 xe → phân trang mượt
  - Xoá khách hàng có HĐ → báo lỗi business
  - Trả xe sớm hơn ngày thuê → báo lỗi
- [ ] Cập nhật README + docs
- [ ] Export CSV thống kê
- [ ] Backup script SQL final

---

## 13. 📊 Tổng số file mới + sửa

| Loại | Số lượng |
|---|---|
| File mới (constant + config + exception + dto + util + dao + bus + common) | ~35 file |
| File refactor (form/panel hiện có) | ~10 file |
| Schema/migration SQL | 3-4 file |
| Tổng | **~50 file** |

**Ước tính thời gian**: 12-15 giờ làm tập trung.

---

## 14. ✨ Best practices áp dụng

1. **Single Responsibility** — mỗi class 1 nhiệm vụ
2. **Separation of Concerns** — GUI/BUS/DAO tách biệt
3. **DRY** — code dùng chung gom vào Util
4. **Fail Fast** — validate ngay đầu vào ở BUS
5. **Defensive Programming** — luôn try-with-resources, luôn check null
6. **Consistent Naming** — tiếng Việt thuần (themXe, suaKhachHang) hoặc tiếng Anh (addBike, updateCustomer) — chọn 1, giữ nguyên
7. **Logging** — log mọi exception, mọi thao tác critical
8. **Configuration External** — không hardcode credentials

---

## 15. ❌ Những thứ KHÔNG làm (để không "over-engineer")

- ❌ **Spring/Hibernate** — yêu cầu Java thuần
- ❌ **JPA/ORM** — quá phức tạp, dùng JDBC trực tiếp
- ❌ **Maven/Gradle** — IntelliJ tự lo classpath
- ❌ **JUnit framework** — viết main() test thủ công
- ❌ **Multi-thread phức tạp** — chỉ dùng SwingWorker cho query lâu
- ❌ **WebSocket / Realtime** — không cần
- ❌ **REST API** — không cần
- ❌ **Docker** — không cần

---

## 16. 🚀 Sau khi xong

App sẽ có:
- 🏗️ Kiến trúc 4 lớp rõ ràng (GUI/BUS/DAO/DB)
- 📄 Phân trang mượt với hàng nghìn bản ghi
- 🔍 Sort + filter + search nâng cao
- 🔐 Password hash an toàn
- 🚨 Exception handling chuyên nghiệp
- 📝 Audit fields + logging
- ⚙️ Config file ngoài (đổi DB không cần build lại)
- 📊 Stat cards + dashboard analytics
- 🎨 UI cream/beige hiện đại
- 👥 Phân quyền nhân viên / khách hàng / quản lý

→ Sẵn sàng demo, dễ bảo trì, đủ chuẩn để đi xin việc.
