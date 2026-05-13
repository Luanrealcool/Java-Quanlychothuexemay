package database;

public class KhachHang {
    private int id;
    private String cmnd;
    private String hoTen;
    private String sdt;
    private String diaChi;

    public KhachHang() {}

    public KhachHang(int id, String cmnd, String hoTen, String sdt, String diaChi) {
        this.id = id;
        this.cmnd = cmnd;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.diaChi = diaChi;
    }

    public int getId() { return id; }
    public String getCmnd() { return cmnd; }
    public String getHoTen() { return hoTen; }
    public String getSdt() { return sdt; }
    public String getDiaChi() { return diaChi; }

    public void setId(int id) { this.id = id; }
    public void setCmnd(String s) { this.cmnd = s; }
    public void setHoTen(String s) { this.hoTen = s; }
    public void setSdt(String s) { this.sdt = s; }
    public void setDiaChi(String s) { this.diaChi = s; }

    @Override
    public String toString() {
        return hoTen + " (" + cmnd + ")";
    }
}
