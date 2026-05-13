package database;

public class XeMay {
    private int id;
    private String bienSo;
    private String hangXe;
    private String model;
    private long giaThue;
    private String trangThai;

    public XeMay() {}

    public XeMay(int id, String bienSo, String hangXe, String model, long giaThue, String trangThai) {
        this.id = id;
        this.bienSo = bienSo;
        this.hangXe = hangXe;
        this.model = model;
        this.giaThue = giaThue;
        this.trangThai = trangThai;
    }

    public int getId() { return id; }
    public String getBienSo() { return bienSo; }
    public String getHangXe() { return hangXe; }
    public String getModel() { return model; }
    public long getGiaThue() { return giaThue; }
    public String getTrangThai() { return trangThai; }

    public void setId(int id) { this.id = id; }
    public void setBienSo(String s) { this.bienSo = s; }
    public void setHangXe(String s) { this.hangXe = s; }
    public void setModel(String s) { this.model = s; }
    public void setGiaThue(long g) { this.giaThue = g; }
    public void setTrangThai(String s) { this.trangThai = s; }

    @Override
    public String toString() {
        return String.format("%s - %s %s (%,dđ/ngày)", bienSo, hangXe, model, giaThue);
    }
}
