package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    public static String hash(String matKhau) {
        if (matKhau == null) matKhau = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(matKhau.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Không có SHA-256", e);
        }
    }

    public static boolean kiemTra(String matKhau, String hashLuu) {
        if (hashLuu == null) return false;
        return hashLuu.equalsIgnoreCase(hash(matKhau));
    }

    public static void main(String[] args) {
        System.out.println("admin/123456 → " + hash("123456"));
        System.out.println("kh/123       → " + hash("123"));
    }
}
