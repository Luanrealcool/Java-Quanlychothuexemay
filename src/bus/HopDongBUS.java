package bus;

import constant.AppConstant;
import constant.TrangThaiHopDong;
import constant.TrangThaiXe;
import dao.HopDongDAO;
import dao.XeMayDAO;
import dto.HopDongDTO;
import dto.PageResult;
import dto.XeMayDTO;
import util.KetNoiCSDL;
import util.ValidatorUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class HopDongBUS {
    private final HopDongDAO hopDongDAO = new HopDongDAO();
    private final XeMayDAO xeMayDAO = new XeMayDAO();

    public PageResult<HopDongDTO> phanTrang(int trang, int kichThuoc, String trangThai,
                                            Integer makh, LocalDate tu, LocalDate den,
                                            String sortBy, String sortDir) {
        if (trang < 1) trang = 1;
        if (kichThuoc < 1) kichThuoc = 20;
        return hopDongDAO.phanTrang(trang, kichThuoc, trangThai, makh, tu, den, sortBy, sortDir);
    }

    public int demTatCa() { return hopDongDAO.demTatCa(); }
    public int demTheoTrangThai(String tt) { return hopDongDAO.demTheoTrangThai(tt); }
    public long tongDoanhThu() { return hopDongDAO.tongDoanhThu(TrangThaiHopDong.DA_TRA); }

    public int taoHopDong(int maKh, int maXe, Integer maNv,
                          LocalDate ngayThue, LocalDate ngayTraDuKien, long tienDatCoc) {
        if (maKh <= 0) throw new RuntimeException("Chưa chọn khách hàng");
        if (maXe <= 0) throw new RuntimeException("Chưa chọn xe");
        if (ngayThue == null || ngayTraDuKien == null)
            throw new RuntimeException("Phải nhập ngày thuê và ngày trả dự kiến");
        if (!ngayTraDuKien.isAfter(ngayThue))
            throw new RuntimeException("Ngày trả phải sau ngày thuê");
        ValidatorUtil.requireNonNegative(tienDatCoc, "Tiền đặt cọc");

        XeMayDTO xe = xeMayDAO.timTheoId(maXe);
        if (xe == null) throw new RuntimeException("Xe không tồn tại");
        if (!TrangThaiXe.SAN_SANG.equals(xe.getTrangThai()))
            throw new RuntimeException("Xe đang " + TrangThaiXe.hienThi(xe.getTrangThai())
                    + ", không thể tạo hợp đồng");

        HopDongDTO hd = new HopDongDTO();
        hd.setMaKh(maKh);
        hd.setMaXe(maXe);
        hd.setMaNv(maNv);
        hd.setNgayThue(ngayThue);
        hd.setNgayTraDuKien(ngayTraDuKien);
        hd.setGiaThueTaiThoiDiem(xe.getGiaThue());
        hd.setTienDatCoc(tienDatCoc);
        hd.setTrangThai(TrangThaiHopDong.DANG_THUE);

        try (Connection con = KetNoiCSDL.getConnection()) {
            con.setAutoCommit(false);
            try {
                int id = hopDongDAO.them(con, hd);
                xeMayDAO.capNhatTrangThai(con, maXe, TrangThaiXe.DANG_THUE);
                con.commit();
                return id;
            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException("Lỗi tạo hợp đồng: " + e.getMessage(), e);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi DB: " + e.getMessage(), e);
        }
    }

    public long traXe(int hopDongId, LocalDate ngayTraThucTe) {
        if (ngayTraThucTe == null) throw new RuntimeException("Phải nhập ngày trả thực tế");

        HopDongDTO hd = hopDongDAO.timTheoId(hopDongId);
        if (hd == null) throw new RuntimeException("Không tìm thấy hợp đồng");
        if (!TrangThaiHopDong.DANG_THUE.equals(hd.getTrangThai()))
            throw new RuntimeException("Hợp đồng này không ở trạng thái đang thuê");
        if (ngayTraThucTe.isBefore(hd.getNgayThue()))
            throw new RuntimeException("Ngày trả không thể trước ngày thuê");

        long[] tien = tinhTien(hd.getNgayThue(), hd.getNgayTraDuKien(), ngayTraThucTe,
                hd.getGiaThueTaiThoiDiem());
        long tongTien = tien[0];
        long phuPhi = tien[1];

        try (Connection con = KetNoiCSDL.getConnection()) {
            con.setAutoCommit(false);
            try {
                hopDongDAO.capNhatTraXe(con, hopDongId, ngayTraThucTe, tongTien, phuPhi);
                xeMayDAO.capNhatTrangThai(con, hd.getMaXe(), TrangThaiXe.SAN_SANG);
                con.commit();
                return tongTien;
            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException("Lỗi trả xe: " + e.getMessage(), e);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi DB: " + e.getMessage(), e);
        }
    }

    /**
     * Trả về [tongTien, phuPhiTre]
     */
    public static long[] tinhTien(LocalDate ngayThue, LocalDate ngayTraDuKien,
                                  LocalDate ngayTraThucTe, long giaThue) {
        long soNgay = ChronoUnit.DAYS.between(ngayThue, ngayTraThucTe);
        if (soNgay <= 0) soNgay = 1;
        long phuPhi = 0;
        if (ngayTraThucTe.isAfter(ngayTraDuKien)) {
            long soNgayTre = ChronoUnit.DAYS.between(ngayTraDuKien, ngayTraThucTe);
            phuPhi = (long) (soNgayTre * giaThue * (AppConstant.PHU_PHI_TRE_PERCENT / 100.0));
        }
        long tong = soNgay * giaThue + phuPhi;
        return new long[]{tong, phuPhi};
    }

    public List<HopDongDTO> hopDongCuaKhach(int makh) {
        return hopDongDAO.phanTrang(1, 1000, null, makh, null, null, "id", "ASC").getItems();
    }
}
