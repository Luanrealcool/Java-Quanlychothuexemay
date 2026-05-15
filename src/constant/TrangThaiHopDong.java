package constant;

public class TrangThaiHopDong {
    public static final String DANG_THUE = "DANG_THUE";
    public static final String DA_TRA = "DA_TRA";
    public static final String HUY = "HUY";

    public static String hienThi(String trangThai) {
        if (trangThai == null) return "";
        switch (trangThai) {
            case DANG_THUE: return "Đang thuê";
            case DA_TRA: return "Đã trả";
            case HUY: return "Đã huỷ";
            default: return trangThai;
        }
    }

    public static String[] danhSach() {
        return new String[]{DANG_THUE, DA_TRA, HUY};
    }
}
