package database;

import java.time.LocalDate;

public class HopDong {
    private int id;
    private int maKh;
    private int maXe;
    private int maNv;
    private LocalDate ngayThue;
    private LocalDate ngayTraDuKien;
    private LocalDate ngayTraThucTe;
    private long tongTien;
    private String trangThai;

    public HopDong() {}

    public int getId() { return id; }
    public int getMaKh() { return maKh; }
    public int getMaXe() { return maXe; }
    public int getMaNv() { return maNv; }
    public LocalDate getNgayThue() { return ngayThue; }
    public LocalDate getNgayTraDuKien() { return ngayTraDuKien; }
    public LocalDate getNgayTraThucTe() { return ngayTraThucTe; }
    public long getTongTien() { return tongTien; }
    public String getTrangThai() { return trangThai; }

    public void setId(int id) { this.id = id; }
    public void setMaKh(int v) { this.maKh = v; }
    public void setMaXe(int v) { this.maXe = v; }
    public void setMaNv(int v) { this.maNv = v; }
    public void setNgayThue(LocalDate d) { this.ngayThue = d; }
    public void setNgayTraDuKien(LocalDate d) { this.ngayTraDuKien = d; }
    public void setNgayTraThucTe(LocalDate d) { this.ngayTraThucTe = d; }
    public void setTongTien(long v) { this.tongTien = v; }
    public void setTrangThai(String s) { this.trangThai = s; }
}
