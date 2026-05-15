package dao;

import dto.KhachHangDTO;
import dto.PageResult;
import util.KetNoiCSDL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    public KhachHangDTO timTheoId(int id) {
        String sql = "SELECT * FROM khachhang WHERE id = ?";
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

    public KhachHangDTO timTheoTenDangNhap(String tenDangNhap) {
        String sql = "SELECT * FROM khachhang WHERE tendangnhap = ?";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tenDangNhap);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi: " + e.getMessage(), e);
        }
        return null;
    }

    public List<KhachHangDTO> layTatCa() {
        String sql = "SELECT * FROM khachhang ORDER BY hoten";
        List<KhachHangDTO> list = new ArrayList<>();
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi: " + e.getMessage(), e);
        }
        return list;
    }

    public PageResult<KhachHangDTO> phanTrang(int trang, int kichThuoc,
                                              String tuKhoa, String sortBy, String sortDir) {
        StringBuilder where = new StringBuilder(" WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (tuKhoa != null && !tuKhoa.trim().isEmpty()) {
            where.append(" AND (cmnd LIKE ? OR hoten LIKE ? OR sdt LIKE ?)");
            String like = "%" + tuKhoa.trim() + "%";
            params.add(like); params.add(like); params.add(like);
        }

        int total;
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM khachhang" + where)) {
            int i = 1;
            for (Object p : params) ps.setObject(i++, p);
            try (ResultSet rs = ps.executeQuery()) {
                total = rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi count: " + e.getMessage(), e);
        }

        String orderBy = " ORDER BY id ASC";
        if (sortBy != null) {
            switch (sortBy) {
                case "cmnd": orderBy = " ORDER BY cmnd"; break;
                case "hoten": orderBy = " ORDER BY hoten"; break;
                case "sdt": orderBy = " ORDER BY sdt"; break;
                default: orderBy = " ORDER BY id"; break;
            }
            orderBy += "ASC".equalsIgnoreCase(sortDir) ? " ASC" : " DESC";
        }

        String sql = "SELECT * FROM khachhang" + where + orderBy + " LIMIT ? OFFSET ?";
        List<KhachHangDTO> items = new ArrayList<>();
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            int i = 1;
            for (Object p : params) ps.setObject(i++, p);
            ps.setInt(i++, kichThuoc);
            ps.setInt(i, (trang - 1) * kichThuoc);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) items.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi: " + e.getMessage(), e);
        }
        return new PageResult<>(items, total, trang, kichThuoc);
    }

    public boolean tonTaiCmnd(String cmnd, Integer trungId) {
        String sql = "SELECT COUNT(*) FROM khachhang WHERE cmnd = ?" +
                (trungId != null ? " AND id <> ?" : "");
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cmnd);
            if (trungId != null) ps.setInt(2, trungId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi: " + e.getMessage(), e);
        }
    }

    public int them(KhachHangDTO kh) {
        String sql = "INSERT INTO khachhang(cmnd, hoten, sdt, ngaysinh, gioitinh, tendangnhap, matkhau_hash) " +
                "VALUES (?,?,?,?,?,?,?)";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, kh.getCmnd());
            ps.setString(2, kh.getHoTen());
            ps.setString(3, kh.getSdt());
            if (kh.getNgaySinh() != null) ps.setDate(4, Date.valueOf(kh.getNgaySinh())); else ps.setNull(4, Types.DATE);
            ps.setString(5, kh.getGioiTinh());
            setNullable(ps, 6, kh.getTenDangNhap());
            setNullable(ps, 7, kh.getMatKhauHash());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi thêm khách: " + e.getMessage(), e);
        }
        return 0;
    }

    public void sua(KhachHangDTO kh) {
        String sql;
        boolean coMatKhau = kh.getMatKhauHash() != null && !kh.getMatKhauHash().isEmpty();
        if (coMatKhau) {
            sql = "UPDATE khachhang SET cmnd=?, hoten=?, sdt=?, ngaysinh=?, gioitinh=?, tendangnhap=?, matkhau_hash=? WHERE id=?";
        } else {
            sql = "UPDATE khachhang SET cmnd=?, hoten=?, sdt=?, ngaysinh=?, gioitinh=?, tendangnhap=? WHERE id=?";
        }
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getCmnd());
            ps.setString(2, kh.getHoTen());
            ps.setString(3, kh.getSdt());
            if (kh.getNgaySinh() != null) ps.setDate(4, Date.valueOf(kh.getNgaySinh())); else ps.setNull(4, Types.DATE);
            ps.setString(5, kh.getGioiTinh());
            setNullable(ps, 6, kh.getTenDangNhap());
            if (coMatKhau) {
                ps.setString(7, kh.getMatKhauHash());
                ps.setInt(8, kh.getId());
            } else {
                ps.setInt(7, kh.getId());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi sửa khách: " + e.getMessage(), e);
        }
    }

    public void xoa(int id) {
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM khachhang WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Không xoá được khách (có thể có hợp đồng): " + e.getMessage(), e);
        }
    }

    public int demTatCa() {
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM khachhang");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi: " + e.getMessage(), e);
        }
        return 0;
    }

    public int demCoTaiKhoan() {
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT COUNT(*) FROM khachhang WHERE tendangnhap IS NOT NULL AND tendangnhap <> ''");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi: " + e.getMessage(), e);
        }
        return 0;
    }

    private void setNullable(PreparedStatement ps, int idx, String v) throws SQLException {
        if (v == null || v.isEmpty()) ps.setNull(idx, Types.VARCHAR);
        else ps.setString(idx, v);
    }

    private KhachHangDTO map(ResultSet rs) throws SQLException {
        KhachHangDTO kh = new KhachHangDTO();
        kh.setId(rs.getInt("id"));
        kh.setCmnd(rs.getString("cmnd"));
        kh.setHoTen(rs.getString("hoten"));
        kh.setSdt(rs.getString("sdt"));
        Date ns = rs.getDate("ngaysinh");
        if (ns != null) kh.setNgaySinh(ns.toLocalDate());
        kh.setGioiTinh(rs.getString("gioitinh"));
        kh.setTenDangNhap(rs.getString("tendangnhap"));
        kh.setMatKhauHash(rs.getString("matkhau_hash"));
        return kh;
    }
}
