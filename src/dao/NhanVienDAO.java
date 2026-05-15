package dao;

import dto.NhanVienDTO;
import util.KetNoiCSDL;

import java.sql.*;
import java.time.LocalDateTime;

public class NhanVienDAO {

    public NhanVienDTO timTheoTenDangNhap(String tenDangNhap) {
        String sql = "SELECT * FROM nhanvien WHERE tendangnhap = ? AND trangthai = 'HOAT_DONG'";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tenDangNhap);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi tìm nhân viên: " + e.getMessage(), e);
        }
        return null;
    }

    public NhanVienDTO timTheoId(int id) {
        String sql = "SELECT * FROM nhanvien WHERE id = ?";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi: " + e.getMessage(), e);
        }
        return null;
    }

    private NhanVienDTO map(ResultSet rs) throws SQLException {
        NhanVienDTO nv = new NhanVienDTO();
        nv.setId(rs.getInt("id"));
        nv.setTenDangNhap(rs.getString("tendangnhap"));
        nv.setMatKhauHash(rs.getString("matkhau_hash"));
        nv.setHoTen(rs.getString("hoten"));
        nv.setSdt(rs.getString("sdt"));
        nv.setVaiTro(rs.getString("vaitro"));
        nv.setTrangThai(rs.getString("trangthai"));
        Timestamp ca = rs.getTimestamp("created_at");
        Timestamp ua = rs.getTimestamp("updated_at");
        if (ca != null) nv.setCreatedAt(ca.toLocalDateTime());
        if (ua != null) nv.setUpdatedAt(ua.toLocalDateTime());
        return nv;
    }
}
