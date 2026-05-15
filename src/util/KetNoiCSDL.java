package util;

import config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KetNoiCSDL {
    private static final String URL;
    private static final String USER;
    private static final String PASS;

    static {
        URL = AppConfig.get("db.url",
                "jdbc:mysql://localhost:3306/quanlychothuexe?useSSL=false&serverTimezone=UTC&characterEncoding=utf8&allowPublicKeyRetrieval=true");
        USER = AppConfig.get("db.user", "root");
        PASS = AppConfig.get("db.password", "");
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Không kết nối được CSDL: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        try (Connection c = getConnection()) {
            System.out.println("✓ Kết nối thành công: " + c.getCatalog());
        } catch (Exception e) {
            System.out.println("✗ Lỗi: " + e.getMessage());
        }
    }
}
