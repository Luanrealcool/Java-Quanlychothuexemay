package bus;

import constant.TrangThaiXe;
import dao.XeMayDAO;
import dto.PageResult;
import dto.XeMayDTO;
import util.ValidatorUtil;

public class XeMayBUS {
    private final XeMayDAO dao = new XeMayDAO();

    public PageResult<XeMayDTO> phanTrang(int trang, int kichThuoc, String tuKhoa,
                                          String trangThai, String sortBy, String sortDir) {
        if (trang < 1) trang = 1;
        if (kichThuoc < 1) kichThuoc = 20;
        return dao.phanTrang(trang, kichThuoc, tuKhoa, trangThai, sortBy, sortDir);
    }

    public java.util.List<XeMayDTO> layXeSanSang() {
        return dao.layTheoTrangThai(TrangThaiXe.SAN_SANG);
    }

    public int demTatCa() { return dao.demTatCa(); }
    public int demTheoTrangThai(String tt) { return dao.demTheoTrangThai(tt); }

    public XeMayDTO timTheoId(int id) {
        XeMayDTO x = dao.timTheoId(id);
        if (x == null) throw new RuntimeException("Không tìm thấy xe có ID = " + id);
        return x;
    }

    public int them(XeMayDTO x) {
        kiemTra(x, null);
        if (dao.tonTaiBienSo(x.getBienSo(), null)) {
            throw new RuntimeException("Biển số '" + x.getBienSo() + "' đã tồn tại");
        }
        return dao.them(x);
    }

    public void sua(XeMayDTO x) {
        if (x.getId() <= 0) throw new RuntimeException("Thiếu ID xe");
        kiemTra(x, x.getId());
        if (dao.tonTaiBienSo(x.getBienSo(), x.getId())) {
            throw new RuntimeException("Biển số '" + x.getBienSo() + "' đã được dùng cho xe khác");
        }
        dao.sua(x);
    }

    public void xoa(int id) {
        XeMayDTO x = dao.timTheoId(id);
        if (x == null) throw new RuntimeException("Xe không tồn tại");
        if (TrangThaiXe.DANG_THUE.equals(x.getTrangThai())) {
            throw new RuntimeException("Không thể xoá xe đang được thuê. Hãy đợi khách trả xe.");
        }
        dao.xoa(id);
    }

    private void kiemTra(XeMayDTO x, Integer idHienTai) {
        ValidatorUtil.requireNotEmpty(x.getBienSo(), "Biển số");
        ValidatorUtil.requireNotEmpty(x.getHangXe(), "Hãng xe");
        ValidatorUtil.requireNotEmpty(x.getModel(), "Model");
        ValidatorUtil.requirePositive(x.getGiaThue(), "Giá thuê");
        if (x.getTrangThai() == null) x.setTrangThai(TrangThaiXe.SAN_SANG);
    }
}
