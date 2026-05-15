package dto;

public class XeMayDTO {
    private int id;
    private String bienSo;
    private String hangXe;
    private String model;
    private Integer maLoaiXe;
    private String tenLoaiXe;       // hiển thị (từ JOIN)
    private Integer namSx;
    private String mauXe;
    private long giaThue;
    private String trangThai;
    private String ghiChu;

    public XeMayDTO() {}

    public int getId() { return id; }
    public String getBienSo() { return bienSo; }
    public String getHangXe() { return hangXe; }
    public String getModel() { return model; }
    public Integer getMaLoaiXe() { return maLoaiXe; }
    public String getTenLoaiXe() { return tenLoaiXe; }
    public Integer getNamSx() { return namSx; }
    public String getMauXe() { return mauXe; }
    public long getGiaThue() { return giaThue; }
    public String getTrangThai() { return trangThai; }
    public String getGhiChu() { return ghiChu; }

    public void setId(int v) { this.id = v; }
    public void setBienSo(String v) { this.bienSo = v; }
    public void setHangXe(String v) { this.hangXe = v; }
    public void setModel(String v) { this.model = v; }
    public void setMaLoaiXe(Integer v) { this.maLoaiXe = v; }
    public void setTenLoaiXe(String v) { this.tenLoaiXe = v; }
    public void setNamSx(Integer v) { this.namSx = v; }
    public void setMauXe(String v) { this.mauXe = v; }
    public void setGiaThue(long v) { this.giaThue = v; }
    public void setTrangThai(String v) { this.trangThai = v; }
    public void setGhiChu(String v) { this.ghiChu = v; }

    @Override
    public String toString() {
        return String.format("%s - %s %s (%,dđ/ngày)", bienSo, hangXe, model, giaThue);
    }
}
