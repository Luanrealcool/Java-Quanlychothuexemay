package dao;

import dto.PageResult;
import dto.XeMayDTO;
import util.KetNoiCSDL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class
XeMayDAO {

    private static final String SELECT_BASE =
            "SELECT x.*, l.ten AS ten_loaixe FROM xemay x " +
            "LEFT JOIN loaixe l ON x.maloaixe = l.id";

    public XeMayDTO timTheoId(int id) {
        String sql = SELECT_BASE + " WHERE x.id = ?";
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

    public PageResult<XeMayDTO> phanTrang(int trang, int kichThuoc,
                                          String tuKhoa, String trangThai,
                                          String sortBy, String sortDir) {
        StringBuilder where = new StringBuilder(" WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (tuKhoa != null && !tuKhoa.trim().isEmpty()) {
            where.append(" AND (x.bienso LIKE ? OR x.hangxe LIKE ? OR x.model LIKE ?)");
            String like = "%" + tuKhoa.trim() + "%";
            params.add(like); params.add(like); params.add(like);
        }
        if (trangThai != null && !trangThai.isEmpty()) {
            where.append(" AND x.trangthai = ?");
            params.add(trangThai);
        }

        // Count
        int total = countWhere(where.toString(), params);

        // Sort whitelist
        String orderBy = " ORDER BY x.id ASC";
        if (sortBy != null) {
            switch (sortBy) {
                case "bienso": orderBy = " ORDER BY x.bienso"; break;
                case "hangxe": orderBy = " ORDER BY x.hangxe"; break;
                case "giathue": orderBy = " ORDER BY x.giathue"; break;
                case "trangthai": orderBy = " ORDER BY x.trangthai"; break;
                default: orderBy = " ORDER BY x.id"; break;
            }
            orderBy += "ASC".equalsIgnoreCase(sortDir) ? " ASC" : " DESC";
        }

        String sql = SELECT_BASE + where + orderBy + " LIMIT ? OFFSET ?";
        List<XeMayDTO> items = new ArrayList<>();
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
            throw new RuntimeException("Lỗi phân trang xe: " + e.getMessage(), e);
        }
        return new PageResult<>(items, total, trang, kichThuoc);
    }

    private int countWhere(String where, List<Object> params) {
        String sql = "SELECT COUNT(*) FROM xemay x" + where;
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            int i = 1;
            for (Object p : params) ps.setObject(i++, p);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi count: " + e.getMessage(), e);
        }
        return 0;
    }

    public List<XeMayDTO> layTheoTrangThai(String trangThai) {
        String sql = SELECT_BASE + " WHERE x.trangthai = ? ORDER BY x.bienso";
        List<XeMayDTO> list = new ArrayList<>();
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi: " + e.getMessage(), e);
        }
        return list;
    }

    public int demTheoTrangThai(String trangThai) {
        String sql = "SELECT COUNT(*) FROM xemay WHERE trangthai = ?";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi: " + e.getMessage(), e);
        }
        return 0;
    }

    public int demTatCa() {
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM xemay");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi: " + e.getMessage(), e);
        }
        return 0;
    }

    public boolean tonTaiBienSo(String bienSo, Integer trungIdId) {
        String sql = "SELECT COUNT(*) FROM xemay WHERE bienso = ?" +
                (trungIdId != null ? " AND id <> ?" : "");
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, bienSo);
            if (trungIdId != null) ps.setInt(2, trungIdId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi: " + e.getMessage(), e);
        }
        return false;
    }

    public int them(XeMayDTO x) {
        String sql = "INSERT INTO xemay(bienso, hangxe, model, maloaixe, namsx, mauxe, giathue, trangthai, ghichu) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, x.getBienSo());
            ps.setString(2, x.getHangXe());
            ps.setString(3, x.getModel());
            if (x.getMaLoaiXe() != null) ps.setInt(4, x.getMaLoaiXe()); else ps.setNull(4, Types.INTEGER);
            if (x.getNamSx() != null) ps.setInt(5, x.getNamSx()); else ps.setNull(5, Types.INTEGER);
            ps.setString(6, x.getMauXe());
            ps.setLong(7, x.getGiaThue());
            ps.setString(8, x.getTrangThai());
            ps.setString(9, x.getGhiChu());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi thêm xe: " + e.getMessage(), e);
        }
        return 0;
    }

    public void sua(XeMayDTO x) {
        String sql = "UPDATE xemay SET bienso=?, hangxe=?, model=?, maloaixe=?, namsx=?, mauxe=?, " +
                "giathue=?, trangthai=?, ghichu=? WHERE id=?";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, x.getBienSo());
            ps.setString(2, x.getHangXe());
            ps.setString(3, x.getModel());
            if (x.getMaLoaiXe() != null) ps.setInt(4, x.getMaLoaiXe()); else ps.setNull(4, Types.INTEGER);
            if (x.getNamSx() != null) ps.setInt(5, x.getNamSx()); else ps.setNull(5, Types.INTEGER);
            ps.setString(6, x.getMauXe());
            ps.setLong(7, x.getGiaThue());
            ps.setString(8, x.getTrangThai());
            ps.setString(9, x.getGhiChu());
            ps.setInt(10, x.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi sửa xe: " + e.getMessage(), e);
        }
    }

    public void xoa(int id) {
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM xemay WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi xoá xe: " + e.getMessage(), e);
        }
    }

    public void capNhatTrangThai(Connection con, int id, String trangThai) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("UPDATE xemay SET trangthai = ? WHERE id = ?")) {
            ps.setString(1, trangThai);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    private XeMayDTO map(ResultSet rs) throws SQLException {
        XeMayDTO x = new XeMayDTO();
        x.setId(rs.getInt("id"));
        x.setBienSo(rs.getString("bienso"));
        x.setHangXe(rs.getString("hangxe"));
        x.setModel(rs.getString("model"));
        int loai = rs.getInt("maloaixe");
        if (!rs.wasNull()) x.setMaLoaiXe(loai);
        x.setTenLoaiXe(rs.getString("ten_loaixe"));
        int nam = rs.getInt("namsx");
        if (!rs.wasNull()) x.setNamSx(nam);
        x.setMauXe(rs.getString("mauxe"));
        x.setGiaThue(rs.getLong("giathue"));
        x.setTrangThai(rs.getString("trangthai"));
        x.setGhiChu(rs.getString("ghichu"));
        return x;
    }
}
