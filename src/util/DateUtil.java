package util;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class DateUtil {
    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter VN = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static LocalDate parse(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        s = s.trim();
        try {
            return LocalDate.parse(s, ISO);
        } catch (DateTimeParseException ignored) {}
        try {
            return LocalDate.parse(s, VN);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Ngày không hợp lệ (yyyy-MM-dd hoặc dd/MM/yyyy): " + s);
        }
    }

    public static long soNgay(LocalDate tu, LocalDate den) {
        if (tu == null || den == null) return 0;
        return ChronoUnit.DAYS.between(tu, den);
    }

    public static boolean hopLe(LocalDate ngay) {
        return ngay != null;
    }

    public static boolean truocHoacBang(LocalDate a, LocalDate b) {
        return a != null && b != null && !a.isAfter(b);
    }

    public static boolean sauHoacBang(LocalDate a, LocalDate b) {
        return a != null && b != null && !a.isBefore(b);
    }
}
