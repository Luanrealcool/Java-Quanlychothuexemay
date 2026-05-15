package dto;

import java.time.LocalDate;

public class KhachHangDTO {
    private int id;
    private String cmnd;
    private String hoTen;
    private String sdt;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String tenDangNhap;
    private String matKhauHash;

    public KhachHangDTO() {}

    public int getId() { return id; }
    public String getCmnd() { return cmnd; }
    public String getHoTen() { return hoTen; }
    public String getSdt() { return sdt; }
    public LocalDate getNgaySinh() { return ngaySinh; }
    public String getGioiTinh() { return gioiTinh; }
    public String getTenDangNhap() { return tenDangNhap; }
    public String getMatKhauHash() { return matKhauHash; }

    public void setId(int v) { this.id = v; }
    public void setCmnd(String v) { this.cmnd = v; }
    public void setHoTen(String v) { this.hoTen = v; }
    public void setSdt(String v) { this.sdt = v; }
    public void setNgaySinh(LocalDate v) { this.ngaySinh = v; }
    public void setGioiTinh(String v) { this.gioiTinh = v; }
    public void setTenDangNhap(String v) { this.tenDangNhap = v; }
    public void setMatKhauHash(String v) { this.matKhauHash = v; }

    @Override
    public String toString() {
        return hoTen + " (" + cmnd + ")";
    }
}
