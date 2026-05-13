package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KetNoiCSDL {
    private static final String URL =
            "jdbc:mysql://localhost:3306/quanlychothuexe?useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
    private static final String USER = "root";
    private static final String PASS = "Anime@1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            System.out.println("Ket noi MySQL thanh cong!");
            System.out.println("Database: " + con.getCatalog());
        } catch (SQLException e) {
            System.out.println("Loi ket noi: " + e.getMessage());
        }
    }
}
