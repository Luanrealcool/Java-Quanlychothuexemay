package dto;

import java.time.LocalDateTime;

public class NhanVienDTO {
    private int id;
    private String tenDangNhap;
    private String matKhauHash;
    private String hoTen;
    private String sdt;
    private String vaiTro;
    private String trangThai;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public NhanVienDTO() {}

    public int getId() { return id; }
    public String getTenDangNhap() { return tenDangNhap; }
    public String getMatKhauHash() { return matKhauHash; }
    public String getHoTen() { return hoTen; }
    public String getSdt() { return sdt; }
    public String getVaiTro() { return vaiTro; }
    public String getTrangThai() { return trangThai; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(int v) { this.id = v; }
    public void setTenDangNhap(String v) { this.tenDangNhap = v; }
    public void setMatKhauHash(String v) { this.matKhauHash = v; }
    public void setHoTen(String v) { this.hoTen = v; }
    public void setSdt(String v) { this.sdt = v; }
    public void setVaiTro(String v) { this.vaiTro = v; }
    public void setTrangThai(String v) { this.trangThai = v; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public void setUpdatedAt(LocalDateTime v) { this.updatedAt = v; }
}
