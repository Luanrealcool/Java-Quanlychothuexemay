package dao;

import dto.HopDongDTO;
import dto.PageResult;
import util.KetNoiCSDL;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HopDongDAO {

    private static final String SELECT_JOIN =
            "SELECT h.*, k.hoten AS ten_kh, " +
            "CONCAT(x.bienso, ' - ', x.hangxe, ' ', x.model) AS thong_tin_xe, " +
            "nv.hoten AS ten_nv " +
            "FROM hopdong h " +
            "JOIN khachhang k ON h.makh = k.id " +
            "JOIN xemay x ON h.maxe = x.id " +
            "LEFT JOIN nhanvien nv ON h.manv = nv.id";

    public HopDongDTO timTheoId(int id) {
        String sql = SELECT_JOIN + " WHERE h.id = ?";
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

    public PageResult<HopDongDTO> phanTrang(int trang, int kichThuoc,
                                            String trangThai, Integer makh,
                                            LocalDate tuNgay, LocalDate denNgay,
                                            String sortBy, String sortDir) {
        StringBuilder where = new StringBuilder(" WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (trangThai != null && !trangThai.isEmpty()) {
            where.append(" AND h.trangthai = ?");
            params.add(trangThai);
        }
        if (makh != null) {
            where.append(" AND h.makh = ?");
            params.add(makh);
        }
        if (tuNgay != null) {
            where.append(" AND h.ngaythue >= ?");
            params.add(Date.valueOf(tuNgay));
        }
        if (denNgay != null) {
            where.append(" AND h.ngaythue <= ?");
            params.add(Date.valueOf(denNgay));
        }

        int total;
        String countSql = "SELECT COUNT(*) FROM hopdong h " +
                "JOIN khachhang k ON h.makh = k.id " +
                "JOIN xemay x ON h.maxe = x.id" + where;
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(countSql)) {
            int i = 1;
            for (Object p : params) ps.setObject(i++, p);
            try (ResultSet rs = ps.executeQuery()) {
                total = rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi count: " + e.getMessage(), e);
        }

        String orderBy = " ORDER BY h.id ASC";
        if (sortBy != null) {
            switch (sortBy) {
                case "masohopdong": orderBy = " ORDER BY h.masohopdong"; break;
                case "ngaythue": orderBy = " ORDER BY h.ngaythue"; break;
                case "tongtien": orderBy = " ORDER BY h.tongtien"; break;
                case "trangthai": orderBy = " ORDER BY h.trangthai"; break;
                default: orderBy = " ORDER BY h.id"; break;
            }
            orderBy += "ASC".equalsIgnoreCase(sortDir) ? " ASC" : " DESC";
        }

        String sql = SELECT_JOIN + where + orderBy + " LIMIT ? OFFSET ?";
        List<HopDongDTO> items = new ArrayList<>();
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

    public int them(Connection con, HopDongDTO hd) throws SQLException {
        String sql = "INSERT INTO hopdong(makh, maxe, manv, ngaythue, ngaytra_dukien, " +
                "giathue_taithoidiem, tiendaco, trangthai, ghichu) VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, hd.getMaKh());
            ps.setInt(2, hd.getMaXe());
            if (hd.getMaNv() != null) ps.setInt(3, hd.getMaNv()); else ps.setNull(3, Types.INTEGER);
            ps.setDate(4, Date.valueOf(hd.getNgayThue()));
            ps.setDate(5, Date.valueOf(hd.getNgayTraDuKien()));
            ps.setLong(6, hd.getGiaThueTaiThoiDiem());
            ps.setLong(7, hd.getTienDatCoc());
            ps.setString(8, hd.getTrangThai());
            ps.setString(9, hd.getGhiChu());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public void capNhatTraXe(Connection con, int hopDongId, LocalDate ngayTra,
                             long tongTien, long phuPhiTre) throws SQLException {
        String sql = "UPDATE hopdong SET ngaytra_thucte=?, tongtien=?, phuphi_tre=?, " +
                "trangthai='DA_TRA' WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(ngayTra));
            ps.setLong(2, tongTien);
            ps.setLong(3, phuPhiTre);
            ps.setInt(4, hopDongId);
            ps.executeUpdate();
        }
    }

    public List<HopDongDTO> thongKeDoanhThu(LocalDate tu, LocalDate den) {
        String sql = SELECT_JOIN +
                " WHERE h.trangthai = 'DA_TRA' AND h.ngaytra_thucte BETWEEN ? AND ? " +
                "ORDER BY h.ngaytra_thucte DESC";
        List<HopDongDTO> list = new ArrayList<>();
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(tu));
            ps.setDate(2, Date.valueOf(den));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi thống kê: " + e.getMessage(), e);
        }
        return list;
    }

    public int demTheoTrangThai(String trangThai) {
        String sql = "SELECT COUNT(*) FROM hopdong WHERE trangthai = ?";
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

    public long tongDoanhThu(String trangThai) {
        String sql = "SELECT IFNULL(SUM(tongtien),0) FROM hopdong WHERE trangthai = ?";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi: " + e.getMessage(), e);
        }
        return 0;
    }

    public int demTatCa() {
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM hopdong");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi: " + e.getMessage(), e);
        }
        return 0;
    }

    private HopDongDTO map(ResultSet rs) throws SQLException {
        HopDongDTO hd = new HopDongDTO();
        hd.setId(rs.getInt("id"));
        hd.setMaSoHopDong(rs.getString("masohopdong"));
        hd.setMaKh(rs.getInt("makh"));
        hd.setMaXe(rs.getInt("maxe"));
        int manv = rs.getInt("manv");
        if (!rs.wasNull()) hd.setMaNv(manv);
        hd.setNgayThue(rs.getDate("ngaythue").toLocalDate());
        hd.setNgayTraDuKien(rs.getDate("ngaytra_dukien").toLocalDate());
        Date traTt = rs.getDate("ngaytra_thucte");
        if (traTt != null) hd.setNgayTraThucTe(traTt.toLocalDate());
        hd.setGiaThueTaiThoiDiem(rs.getLong("giathue_taithoidiem"));
        hd.setTienDatCoc(rs.getLong("tiendaco"));
        hd.setTongTien(rs.getLong("tongtien"));
        hd.setPhuPhiTre(rs.getLong("phuphi_tre"));
        hd.setTrangThai(rs.getString("trangthai"));
        hd.setLyDoHuy(rs.getString("lydo_huy"));
        hd.setGhiChu(rs.getString("ghichu"));
        try { hd.setTenKhachHang(rs.getString("ten_kh")); } catch (SQLException ignored) {}
        try { hd.setThongTinXe(rs.getString("thong_tin_xe")); } catch (SQLException ignored) {}
        try { hd.setTenNhanVien(rs.getString("ten_nv")); } catch (SQLException ignored) {}
        return hd;
    }
}
