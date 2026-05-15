package dto;

import java.time.LocalDate;

public class HopDongDTO {
    private int id;
    private String maSoHopDong;
    private int maKh;
    private int maXe;
    private Integer maNv;
    private LocalDate ngayThue;
    private LocalDate ngayTraDuKien;
    private LocalDate ngayTraThucTe;
    private long giaThueTaiThoiDiem;
    private long tienDatCoc;
    private long tongTien;
    private long phuPhiTre;
    private String trangThai;
    private String lyDoHuy;
    private String ghiChu;

    // Fields tiện cho hiển thị (từ JOIN)
    private String tenKhachHang;
    private String thongTinXe;
    private String tenNhanVien;

    public HopDongDTO() {}

    public int getId() { return id; }
    public String getMaSoHopDong() { return maSoHopDong; }
    public int getMaKh() { return maKh; }
    public int getMaXe() { return maXe; }
    public Integer getMaNv() { return maNv; }
    public LocalDate getNgayThue() { return ngayThue; }
    public LocalDate getNgayTraDuKien() { return ngayTraDuKien; }
    public LocalDate getNgayTraThucTe() { return ngayTraThucTe; }
    public long getGiaThueTaiThoiDiem() { return giaThueTaiThoiDiem; }
    public long getTienDatCoc() { return tienDatCoc; }
    public long getTongTien() { return tongTien; }
    public long getPhuPhiTre() { return phuPhiTre; }
    public String getTrangThai() { return trangThai; }
    public String getLyDoHuy() { return lyDoHuy; }
    public String getGhiChu() { return ghiChu; }
    public String getTenKhachHang() { return tenKhachHang; }
    public String getThongTinXe() { return thongTinXe; }
    public String getTenNhanVien() { return tenNhanVien; }

    public void setId(int v) { this.id = v; }
    public void setMaSoHopDong(String v) { this.maSoHopDong = v; }
    public void setMaKh(int v) { this.maKh = v; }
    public void setMaXe(int v) { this.maXe = v; }
    public void setMaNv(Integer v) { this.maNv = v; }
    public void setNgayThue(LocalDate v) { this.ngayThue = v; }
    public void setNgayTraDuKien(LocalDate v) { this.ngayTraDuKien = v; }
    public void setNgayTraThucTe(LocalDate v) { this.ngayTraThucTe = v; }
    public void setGiaThueTaiThoiDiem(long v) { this.giaThueTaiThoiDiem = v; }
    public void setTienDatCoc(long v) { this.tienDatCoc = v; }
    public void setTongTien(long v) { this.tongTien = v; }
    public void setPhuPhiTre(long v) { this.phuPhiTre = v; }
    public void setTrangThai(String v) { this.trangThai = v; }
    public void setLyDoHuy(String v) { this.lyDoHuy = v; }
    public void setGhiChu(String v) { this.ghiChu = v; }
    public void setTenKhachHang(String v) { this.tenKhachHang = v; }
    public void setThongTinXe(String v) { this.thongTinXe = v; }
    public void setTenNhanVien(String v) { this.tenNhanVien = v; }
}
