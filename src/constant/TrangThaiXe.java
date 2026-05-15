package constant;

public class TrangThaiXe {
    public static final String SAN_SANG = "SAN_SANG";
    public static final String DANG_THUE = "DANG_THUE";
    public static final String BAO_TRI = "BAO_TRI";

    public static String hienThi(String trangThai) {
        if (trangThai == null) return "";
        switch (trangThai) {
            case SAN_SANG: return "Sẵn sàng";
            case DANG_THUE: return "Đang thuê";
            case BAO_TRI: return "Bảo trì";
            default: return trangThai;
        }
    }

    public static String[] danhSach() {
        return new String[]{SAN_SANG, DANG_THUE, BAO_TRI};
    }
}
