package util;


import java.util.regex.Pattern;

public class ValidatorUtil {
    private static final Pattern CMND = Pattern.compile("^\\d{9}$|^\\d{12}$");
    private static final Pattern SDT = Pattern.compile("^(0|\\+84)[0-9]{9,10}$");
    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$");
    private static final Pattern BIEN_SO = Pattern.compile("^\\d{2}[A-Z][0-9A-Z]?-?\\d{3,5}$|^\\d{2}-[A-Z0-9]{1,3}\\s?\\d{3,5}$");

    public static void requireNotEmpty(String s, String field) {
        if (s == null || s.trim().isEmpty()) {
            throw new RuntimeException(field + " không được để trống");
        }
    }

    public static void requirePositive(long n, String field) {
        if (n <= 0) {
            throw new RuntimeException(field + " phải lớn hơn 0");
        }
    }

    public static void requireNonNegative(long n, String field) {
        if (n < 0) {
            throw new RuntimeException(field + " không được âm");
        }
    }

    public static long parseLong(String s, String field) {
        requireNotEmpty(s, field);
        try {
            return Long.parseLong(s.trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException(field + " phải là số");
        }
    }

    public static boolean laCmnd(String s) {
        return s != null && CMND.matcher(s.trim()).matches();
    }

    public static boolean laSdt(String s) {
        return s != null && SDT.matcher(s.trim()).matches();
    }

    public static boolean laEmail(String s) {
        return s != null && EMAIL.matcher(s.trim()).matches();
    }

    public static boolean laBienSo(String s) {
        return s != null && BIEN_SO.matcher(s.trim().toUpperCase()).matches();
    }

    public static void kiemTraCmnd(String s) {
        requireNotEmpty(s, "CMND/CCCD");
        if (!laCmnd(s)) throw new RuntimeException("CMND/CCCD phải có 9 hoặc 12 chữ số");
    }

    public static void kiemTraSdt(String s) {
        if (s == null || s.trim().isEmpty()) return;
        if (!laSdt(s)) throw new RuntimeException("Số điện thoại không hợp lệ");
    }

    public static void kiemTraEmail(String s) {
        if (s == null || s.trim().isEmpty()) return;
        if (!laEmail(s)) throw new RuntimeException("Email không hợp lệ");
    }
}
