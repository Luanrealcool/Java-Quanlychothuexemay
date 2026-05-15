package bus;

import dao.KhachHangDAO;
import dao.NhanVienDAO;
import dto.KhachHangDTO;
import dto.NhanVienDTO;
import util.PasswordUtil;
import util.ValidatorUtil;

public class AuthBUS {
    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    public static class KetQuaDangNhap {
        public final NhanVienDTO nhanVien;
        public final KhachHangDTO khachHang;

        private KetQuaDangNhap(NhanVienDTO nv, KhachHangDTO kh) {
            this.nhanVien = nv;
            this.khachHang = kh;
        }

        public static KetQuaDangNhap laNhanVien(NhanVienDTO nv) {
            return new KetQuaDangNhap(nv, null);
        }

        public static KetQuaDangNhap laKhachHang(KhachHangDTO kh) {
            return new KetQuaDangNhap(null, kh);
        }

        public boolean laNhanVien() { return nhanVien != null; }
        public boolean laKhachHang() { return khachHang != null; }
    }

    public KetQuaDangNhap dangNhap(String tenDangNhap, String matKhau) {
        ValidatorUtil.requireNotEmpty(tenDangNhap, "Tên đăng nhập");
        ValidatorUtil.requireNotEmpty(matKhau, "Mật khẩu");

        String hash = PasswordUtil.hash(matKhau);

        NhanVienDTO nv = nhanVienDAO.timTheoTenDangNhap(tenDangNhap.trim());
        if (nv != null && hash.equalsIgnoreCase(nv.getMatKhauHash())) {
            return KetQuaDangNhap.laNhanVien(nv);
        }

        KhachHangDTO kh = khachHangDAO.timTheoTenDangNhap(tenDangNhap.trim());
        if (kh != null && hash.equalsIgnoreCase(kh.getMatKhauHash())) {
            return KetQuaDangNhap.laKhachHang(kh);
        }

        throw new RuntimeException("Sai tên đăng nhập hoặc mật khẩu");
    }
}
