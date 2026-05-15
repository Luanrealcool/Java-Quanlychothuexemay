package bus;

import constant.TrangThaiHopDong;
import dao.HopDongDAO;
import dto.HopDongDTO;

import java.time.LocalDate;
import java.util.List;

public class ThongKeBUS {
    private final HopDongDAO dao = new HopDongDAO();

    public static class KetQuaThongKe {
        public final List<HopDongDTO> danhSach;
        public final long tongDoanhThu;
        public final int soHopDong;
        public final long trungBinhMoiHd;
        public final int soHopDongTre;

        public KetQuaThongKe(List<HopDongDTO> ds, long tong, int soHd, long tb, int soTre) {
            this.danhSach = ds;
            this.tongDoanhThu = tong;
            this.soHopDong = soHd;
            this.trungBinhMoiHd = tb;
            this.soHopDongTre = soTre;
        }
    }

    public KetQuaThongKe thongKeTheoNgay(LocalDate tu, LocalDate den) {
        if (tu == null || den == null)
            throw new RuntimeException("Phải nhập đủ ngày bắt đầu và kết thúc");
        if (den.isBefore(tu))
            throw new RuntimeException("Ngày kết thúc phải sau ngày bắt đầu");

        List<HopDongDTO> list = dao.thongKeDoanhThu(tu, den);
        long tong = 0;
        int soTre = 0;
        for (HopDongDTO h : list) {
            tong += h.getTongTien();
            if (h.getNgayTraThucTe() != null && h.getNgayTraDuKien() != null
                    && h.getNgayTraThucTe().isAfter(h.getNgayTraDuKien())) {
                soTre++;
            }
        }
        long tb = list.isEmpty() ? 0 : tong / list.size();
        return new KetQuaThongKe(list, tong, list.size(), tb, soTre);
    }

    public long doanhThuTatCa() {
        return dao.tongDoanhThu(TrangThaiHopDong.DA_TRA);
    }
}
