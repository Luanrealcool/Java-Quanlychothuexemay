package bus;

import dao.KhachHangDAO;
import dto.KhachHangDTO;
import dto.PageResult;
import util.PasswordUtil;
import util.ValidatorUtil;

public class KhachHangBUS {
    private final KhachHangDAO dao = new KhachHangDAO();

    public PageResult<KhachHangDTO> phanTrang(int trang, int kichThuoc, String tuKhoa,
                                              String sortBy, String sortDir) {
        if (trang < 1) trang = 1;
        if (kichThuoc < 1) kichThuoc = 20;
        return dao.phanTrang(trang, kichThuoc, tuKhoa, sortBy, sortDir);
    }

    public java.util.List<KhachHangDTO> layTatCa() {
        return dao.layTatCa();
    }

    public KhachHangDTO timTheoId(int id) {
        KhachHangDTO kh = dao.timTheoId(id);
        if (kh == null) throw new RuntimeException("Không tìm thấy khách hàng có ID = " + id);
        return kh;
    }

    public int demTatCa() { return dao.demTatCa(); }
    public int demCoTaiKhoan() { return dao.demCoTaiKhoan(); }

    /**
     * Thêm khách hàng. matKhauThuong = chuỗi plain text, sẽ tự hash.
     */
    public int them(KhachHangDTO kh, String matKhauThuong) {
        kiemTra(kh);
        if (dao.tonTaiCmnd(kh.getCmnd(), null)) {
            throw new RuntimeException("CMND/CCCD '" + kh.getCmnd() + "' đã tồn tại");
        }
        if (matKhauThuong != null && !matKhauThuong.isEmpty()) {
            kh.setMatKhauHash(PasswordUtil.hash(matKhauThuong));
        }
        return dao.them(kh);
    }

    public void sua(KhachHangDTO kh, String matKhauMoi) {
        if (kh.getId() <= 0) throw new RuntimeException("Thiếu ID khách hàng");
        kiemTra(kh);
        if (dao.tonTaiCmnd(kh.getCmnd(), kh.getId())) {
            throw new RuntimeException("CMND/CCCD '" + kh.getCmnd() + "' đã dùng cho khách khác");
        }
        if (matKhauMoi != null && !matKhauMoi.isEmpty()) {
            kh.setMatKhauHash(PasswordUtil.hash(matKhauMoi));
        } else {
            kh.setMatKhauHash(null);
        }
        dao.sua(kh);
    }

    public void xoa(int id) {
        dao.xoa(id);
    }

    private void kiemTra(KhachHangDTO kh) {
        ValidatorUtil.kiemTraCmnd(kh.getCmnd());
        ValidatorUtil.requireNotEmpty(kh.getHoTen(), "Họ tên");
        ValidatorUtil.kiemTraSdt(kh.getSdt());
    }
}
