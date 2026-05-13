package database;

public class NhanVien {
    private int id;
    private String tenDangNhap;
    private String matKhau;
    private String hoTen;
    private String sdt;

    public NhanVien() {}

    public NhanVien(int id, String tenDangNhap, String hoTen) {
        this.id = id;
        this.tenDangNhap = tenDangNhap;
        this.hoTen = hoTen;
    }

    public int getId() { return id; }
    public String getTenDangNhap() { return tenDangNhap; }
    public String getMatKhau() { return matKhau; }
    public String getHoTen() { return hoTen; }
    public String getSdt() { return sdt; }

    public void setId(int id) { this.id = id; }
    public void setTenDangNhap(String s) { this.tenDangNhap = s; }
    public void setMatKhau(String s) { this.matKhau = s; }
    public void setHoTen(String s) { this.hoTen = s; }
    public void setSdt(String s) { this.sdt = s; }
}
