package constant;

public class VaiTro {
    public static final String NHAN_VIEN = "NHAN_VIEN";
    public static final String QUAN_LY = "QUAN_LY";
    public static final String KHACH_HANG = "KHACH_HANG";

    public static String hienThi(String vt) {
        if (vt == null) return "";
        switch (vt) {
            case NHAN_VIEN: return "Nhân viên";
            case QUAN_LY: return "Quản lý";
            case KHACH_HANG: return "Khách hàng";
            default: return vt;
        }
    }
}
