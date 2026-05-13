package gui;

import database.KetNoiCSDL;
import database.KhachHang;
import database.NhanVien;
import database.XeMay;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class FormHopDong extends JPanel implements ActionListener {
    NhanVien nhanVien;
    JComboBox<KhachHang> cboKhachHang;
    JComboBox<XeMay> cboXeMay;
    JTextField txtNgayThue, txtNgayTraDuKien;
    JButton btnTaoHopDong, btnTraXe, btnLamMoi;
    DefaultTableModel model;
    JTable bang;

    public FormHopDong(NhanVien nv) {
        this.nhanVien = nv;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        GUI();
        napCboKhachHang();
        napCboXeSanSang();
        napDuLieu();
    }

    public void GUI() {
        JPanel pForm = new JPanel(new GridLayout(2, 4, 8, 6));
        pForm.setBorder(BorderFactory.createTitledBorder("Tạo hợp đồng thuê xe"));

        pForm.add(new JLabel("Khách hàng:"));
        cboKhachHang = new JComboBox<>();
        pForm.add(cboKhachHang);

        pForm.add(new JLabel("Xe máy (sẵn sàng):"));
        cboXeMay = new JComboBox<>();
        pForm.add(cboXeMay);

        pForm.add(new JLabel("Ngày thuê (yyyy-MM-dd):"));
        txtNgayThue = new JTextField(LocalDate.now().toString());
        pForm.add(txtNgayThue);

        pForm.add(new JLabel("Ngày trả dự kiến:"));
        txtNgayTraDuKien = new JTextField(LocalDate.now().plusDays(3).toString());
        pForm.add(txtNgayTraDuKien);

        add(pForm, BorderLayout.NORTH);

        String[] cot = {"ID", "Khách hàng", "Xe máy", "Ngày thuê",
                "Trả dự kiến", "Trả thực tế", "Tổng tiền", "Trạng thái"};
        model = new DefaultTableModel(cot, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bang = new JTable(model);
        JScrollPane scroll = new JScrollPane(bang);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách hợp đồng"));
        add(scroll, BorderLayout.CENTER);

        btnTaoHopDong = new JButton("Tạo hợp đồng");
        btnTraXe = new JButton("Trả xe");
        btnLamMoi = new JButton("Làm mới");

        JPanel pNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        pNut.add(btnTaoHopDong);
        pNut.add(btnTraXe);
        pNut.add(btnLamMoi);
        add(pNut, BorderLayout.SOUTH);

        btnTaoHopDong.addActionListener(this);
        btnTraXe.addActionListener(this);
        btnLamMoi.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnTaoHopDong) taoHopDong();
        else if (e.getSource() == btnTraXe) traXe();
        else if (e.getSource() == btnLamMoi) {
            napCboKhachHang();
            napCboXeSanSang();
            napDuLieu();
        }
    }

    private void napCboKhachHang() {
        cboKhachHang.removeAllItems();
        String sql = "SELECT * FROM khachhang ORDER BY hoten";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cboKhachHang.addItem(new KhachHang(
                        rs.getInt("id"),
                        rs.getString("cmnd"),
                        rs.getString("hoten"),
                        rs.getString("sdt"),
                        rs.getString("diachi")
                ));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải khách: " + ex.getMessage());
        }
    }

    private void napCboXeSanSang() {
        cboXeMay.removeAllItems();
        String sql = "SELECT * FROM xemay WHERE trangthai = 'SAN_SANG' ORDER BY bienso";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cboXeMay.addItem(new XeMay(
                        rs.getInt("id"),
                        rs.getString("bienso"),
                        rs.getString("hangxe"),
                        rs.getString("model"),
                        rs.getLong("giathue"),
                        rs.getString("trangthai")
                ));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải xe: " + ex.getMessage());
        }
    }

    private void napDuLieu() {
        model.setRowCount(0);
        String sql = "SELECT h.id, k.hoten AS ten_kh, " +
                "CONCAT(x.bienso, ' - ', x.hangxe, ' ', x.model) AS thong_tin_xe, " +
                "h.ngaythue, h.ngaytra_dukien, h.ngaytra_thucte, h.tongtien, h.trangthai " +
                "FROM hopdong h " +
                "JOIN khachhang k ON h.makh = k.id " +
                "JOIN xemay x ON h.maxe = x.id " +
                "ORDER BY h.id DESC";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Date traTt = rs.getDate("ngaytra_thucte");
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("ten_kh"),
                        rs.getString("thong_tin_xe"),
                        rs.getDate("ngaythue").toString(),
                        rs.getDate("ngaytra_dukien").toString(),
                        traTt == null ? "" : traTt.toString(),
                        rs.getLong("tongtien"),
                        rs.getString("trangthai")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải hợp đồng: " + ex.getMessage());
        }
    }

    private void taoHopDong() {
        KhachHang kh = (KhachHang) cboKhachHang.getSelectedItem();
        XeMay xe = (XeMay) cboXeMay.getSelectedItem();
        if (kh == null || xe == null) {
            JOptionPane.showMessageDialog(this, "Chọn khách hàng và xe!");
            return;
        }

        LocalDate nThue, nTra;
        try {
            nThue = LocalDate.parse(txtNgayThue.getText().trim());
            nTra = LocalDate.parse(txtNgayTraDuKien.getText().trim());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Ngày sai định dạng yyyy-MM-dd!");
            return;
        }
        if (!nTra.isAfter(nThue)) {
            JOptionPane.showMessageDialog(this, "Ngày trả phải sau ngày thuê!");
            return;
        }

        String sqlHd = "INSERT INTO hopdong(makh, maxe, manv, ngaythue, ngaytra_dukien, trangthai) " +
                "VALUES (?,?,?,?,?, 'DANG_THUE')";
        String sqlXe = "UPDATE xemay SET trangthai='DANG_THUE' WHERE id=?";

        try (Connection con = KetNoiCSDL.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps1 = con.prepareStatement(sqlHd);
                 PreparedStatement ps2 = con.prepareStatement(sqlXe)) {
                ps1.setInt(1, kh.getId());
                ps1.setInt(2, xe.getId());
                ps1.setInt(3, nhanVien.getId());
                ps1.setDate(4, Date.valueOf(nThue));
                ps1.setDate(5, Date.valueOf(nTra));
                ps1.executeUpdate();

                ps2.setInt(1, xe.getId());
                ps2.executeUpdate();

                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
            JOptionPane.showMessageDialog(this, "Tạo hợp đồng thành công!");
            napCboXeSanSang();
            napDuLieu();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tạo hợp đồng: " + ex.getMessage());
        }
    }

    private void traXe() {
        int row = bang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn hợp đồng cần trả xe!");
            return;
        }
        if (!"DANG_THUE".equals(model.getValueAt(row, 7))) {
            JOptionPane.showMessageDialog(this, "Hợp đồng này đã trả xe rồi!");
            return;
        }
        int hopDongId = (int) model.getValueAt(row, 0);

        String nhap = JOptionPane.showInputDialog(this,
                "Nhập ngày trả thực tế (yyyy-MM-dd):", LocalDate.now().toString());
        if (nhap == null) return;
        LocalDate ngayTraThucTe;
        try {
            ngayTraThucTe = LocalDate.parse(nhap.trim());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Sai định dạng ngày!");
            return;
        }

        String sqlGet = "SELECT h.maxe, h.ngaythue, h.ngaytra_dukien, x.giathue " +
                "FROM hopdong h JOIN xemay x ON h.maxe = x.id WHERE h.id = ?";
        String sqlUpdHd = "UPDATE hopdong SET ngaytra_thucte=?, tongtien=?, trangthai='DA_TRA' WHERE id=?";
        String sqlUpdXe = "UPDATE xemay SET trangthai='SAN_SANG' WHERE id=?";

        try (Connection con = KetNoiCSDL.getConnection()) {
            int maXe;
            LocalDate ngayThue, ngayTraDuKien;
            long giaThue;

            try (PreparedStatement ps = con.prepareStatement(sqlGet)) {
                ps.setInt(1, hopDongId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy hợp đồng!");
                        return;
                    }
                    maXe = rs.getInt("maxe");
                    ngayThue = rs.getDate("ngaythue").toLocalDate();
                    ngayTraDuKien = rs.getDate("ngaytra_dukien").toLocalDate();
                    giaThue = rs.getLong("giathue");
                }
            }

            long tongTien = tinhTien(ngayThue, ngayTraDuKien, ngayTraThucTe, giaThue);

            con.setAutoCommit(false);
            try (PreparedStatement ps1 = con.prepareStatement(sqlUpdHd);
                 PreparedStatement ps2 = con.prepareStatement(sqlUpdXe)) {
                ps1.setDate(1, Date.valueOf(ngayTraThucTe));
                ps1.setLong(2, tongTien);
                ps1.setInt(3, hopDongId);
                ps1.executeUpdate();

                ps2.setInt(1, maXe);
                ps2.executeUpdate();

                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }

            JOptionPane.showMessageDialog(this,
                    String.format("Đã trả xe.\nTổng tiền: %,d VNĐ", tongTien));
            napCboXeSanSang();
            napDuLieu();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi trả xe: " + ex.getMessage());
        }
    }

    private long tinhTien(LocalDate ngayThue, LocalDate ngayTraDuKien,
                          LocalDate ngayTraThucTe, long giaThue) {
        long soNgay = ChronoUnit.DAYS.between(ngayThue, ngayTraThucTe);
        if (soNgay <= 0) soNgay = 1;
        long tong = soNgay * giaThue;
        if (ngayTraThucTe.isAfter(ngayTraDuKien)) {
            long soNgayTre = ChronoUnit.DAYS.between(ngayTraDuKien, ngayTraThucTe);
            tong += (long) (soNgayTre * giaThue * 1.5);
        }
        return tong;
    }
}
