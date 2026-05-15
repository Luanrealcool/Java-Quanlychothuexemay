package util;

import constant.AppConstant;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatUtil {
    private static final NumberFormat TIEN_VN = NumberFormat.getInstance(new Locale("vi", "VN"));
    private static final DateTimeFormatter NGAY_HIEN_THI =
            DateTimeFormatter.ofPattern(AppConstant.FORMAT_NGAY_HIEN_THI);
    private static final DateTimeFormatter NGAY_SQL =
            DateTimeFormatter.ofPattern(AppConstant.FORMAT_NGAY);
    private static final DateTimeFormatter NGAY_GIO =
            DateTimeFormatter.ofPattern(AppConstant.FORMAT_NGAY_GIO);

    public static String tien(long soTien) {
        return TIEN_VN.format(soTien) + " đ";
    }

    public static String tienKhongDvi(long soTien) {
        return TIEN_VN.format(soTien);
    }

    public static String ngay(LocalDate d) {
        return d == null ? "" : d.format(NGAY_HIEN_THI);
    }

    public static String ngaySql(LocalDate d) {
        return d == null ? "" : d.format(NGAY_SQL);
    }

    public static String ngayGio(LocalDateTime dt) {
        return dt == null ? "" : dt.format(NGAY_GIO);
    }

    public static String sdt(String s) {
        if (s == null || s.length() < 9) return s == null ? "" : s;
        if (s.length() == 10) return s.substring(0, 4) + " " + s.substring(4, 7) + " " + s.substring(7);
        return s;
    }
}
