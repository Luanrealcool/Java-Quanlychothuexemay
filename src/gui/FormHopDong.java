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
    JButton btnTaoHopDong, btnTraXe;
    DefaultTableModel model;
    JTable bang;
    JLabel lblStatTong, lblStatDangThue, lblStatDaTra, lblStatDoanhThu;

    public FormHopDong(NhanVien nv) {
        this.nhanVien = nv;
        setLayout(new BorderLayout());
        setBackground(GiaoDien.XAM_NHAT);
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GUI();
        napDuLieu();
    }

    public void GUI() {
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        top.add(GiaoDien.taoPageHeader("Quản lý hợp đồng",
                "Tạo hợp đồng thuê xe và xử lý trả xe"));
        top.add(Box.createVerticalStrut(16));
        top.add(taoStatsRow());
        top.add(Box.createVerticalStrut(16));
        top.add(taoToolbar());
        top.add(Box.createVerticalStrut(10));

        add(top, BorderLayout.NORTH);
        add(taoTableCard(), BorderLayout.CENTER);
    }

    private JPanel taoStatsRow() {
        JPanel p = new JPanel(new GridLayout(1, 4, 14, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblStatTong = new JLabel("0");
        lblStatDangThue = new JLabel("0");
        lblStatDaTra = new JLabel("0");
        lblStatDoanhThu = new JLabel("0 đ");

        p.add(GiaoDien.taoStatCard("TỔNG HỢP ĐỒNG", lblStatTong, GiaoDien.XANH_CHINH, "📄"));
        p.add(GiaoDien.taoStatCard("ĐANG THUÊ", lblStatDangThue, GiaoDien.CAM, "⏱"));
        p.add(GiaoDien.taoStatCard("ĐÃ TRẢ", lblStatDaTra, GiaoDien.XAM, "✓"));
        p.add(GiaoDien.taoStatCard("TỔNG DOANH THU", lblStatDoanhThu, GiaoDien.XANH_LA, "💰"));
        return p;
    }

    private JPanel taoToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        toolbar.setOpaque(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        btnTaoHopDong = new JButton("+ Tạo hợp đồng");
        btnTraXe = new JButton("✓ Trả xe");
        GiaoDien.styleNut(btnTaoHopDong, GiaoDien.XANH_LA);
        GiaoDien.styleNut(btnTraXe, GiaoDien.CAM);

        toolbar.add(btnTaoHopDong);
        toolbar.add(btnTraXe);

        btnTaoHopDong.addActionListener(this);
        btnTraXe.addActionListener(this);
        return toolbar;
    }

    private JPanel taoTableCard() {
        JPanel card = GiaoDien.taoCard();
        card.add(GiaoDien.taoCardHeader("DANH SÁCH HỢP ĐỒNG"), BorderLayout.NORTH);

        String[] cot = {"ID", "Khách hàng", "Xe máy", "Ngày thuê",
                "Trả dự kiến", "Trả thực tế", "Tổng tiền", "Trạng thái"};
        model = new DefaultTableModel(cot, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bang = new JTable(model);
        GiaoDien.styleBang(bang);
        GiaoDien.setRenderer(bang, 6, GiaoDien.rendererTien());
        GiaoDien.setRenderer(bang, 7, GiaoDien.rendererTrangThai());

        JScrollPane scroll = new JScrollPane(bang);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(GiaoDien.TRANG);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnTaoHopDong) hienDialogTaoHopDong();
        else if (e.getSource() == btnTraXe) traXe();
    }

    private void napDuLieu() {
        model.setRowCount(0);
        int tong = 0, dt = 0, da = 0;
        long doanhThu = 0;
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
                String tt = rs.getString("trangthai");
                long tien = rs.getLong("tongtien");
                tong++;
                if ("DANG_THUE".equals(tt)) dt++;
                else if ("DA_TRA".equals(tt)) { da++; doanhThu += tien; }
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("ten_kh"),
                        rs.getString("thong_tin_xe"),
                        rs.getDate("ngaythue").toString(),
                        rs.getDate("ngaytra_dukien").toString(),
                        traTt == null ? "" : traTt.toString(),
                        tien,
                        tt
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải hợp đồng: " + ex.getMessage());
        }
        lblStatTong.setText(String.valueOf(tong));
        lblStatDangThue.setText(String.valueOf(dt));
        lblStatDaTra.setText(String.valueOf(da));
        lblStatDoanhThu.setText(String.format("%,d đ", doanhThu));
    }

    private void hienDialogTaoHopDong() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                "Tạo hợp đồng thuê xe", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());

        dialog.add(FormXeMay.taoDialogHeader("Tạo hợp đồng thuê xe", "+", GiaoDien.XANH_LA),
                BorderLayout.NORTH);

        JComboBox<KhachHang> cboKh = new JComboBox<>();
        JComboBox<XeMay> cboXe = new JComboBox<>();
        JTextField txtNgayThue = new JTextField(LocalDate.now().toString());
        JTextField txtNgayTra = new JTextField(LocalDate.now().plusDays(3).toString());
        GiaoDien.styleInput(cboKh);
        GiaoDien.styleInput(cboXe);
        GiaoDien.styleInput(txtNgayThue);
        GiaoDien.styleInput(txtNgayTra);

        try (Connection con = KetNoiCSDL.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM khachhang ORDER BY hoten");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cboKh.addItem(new KhachHang(rs.getInt("id"), rs.getString("cmnd"),
                            rs.getString("hoten"), rs.getString("sdt"), rs.getString("diachi")));
                }
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM xemay WHERE trangthai='SAN_SANG' ORDER BY bienso");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cboXe.addItem(new XeMay(rs.getInt("id"), rs.getString("bienso"),
                            rs.getString("hangxe"), rs.getString("model"),
                            rs.getLong("giathue"), rs.getString("trangthai")));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(dialog, "Lỗi tải dữ liệu: " + ex.getMessage());
            return;
        }

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(GiaoDien.TRANG);
        form.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 6, 8, 6);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        FormXeMay.addFormRow(form, g, 0, "Khách hàng *", cboKh);
        FormXeMay.addFormRow(form, g, 1, "Xe máy *", cboXe);
        FormXeMay.addFormRow(form, g, 2, "Ngày thuê (yyyy-MM-dd) *", txtNgayThue);
        FormXeMay.addFormRow(form, g, 3, "Ngày trả dự kiến *", txtNgayTra);

        dialog.add(form, BorderLayout.CENTER);

        dialog.add(FormXeMay.taoDialogFooter(dialog, false, () -> {
            KhachHang kh = (KhachHang) cboKh.getSelectedItem();
            XeMay xe = (XeMay) cboXe.getSelectedItem();
            if (kh == null || xe == null) {
                JOptionPane.showMessageDialog(dialog, "Chọn khách hàng và xe!");
                return false;
            }
            LocalDate nThue, nTra;
            try {
                nThue = LocalDate.parse(txtNgayThue.getText().trim());
                nTra = LocalDate.parse(txtNgayTra.getText().trim());
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Ngày sai định dạng yyyy-MM-dd!");
                return false;
            }
            if (!nTra.isAfter(nThue)) {
                JOptionPane.showMessageDialog(dialog, "Ngày trả phải sau ngày thuê!");
                return false;
            }

            try (Connection con = KetNoiCSDL.getConnection()) {
                con.setAutoCommit(false);
                try (PreparedStatement ps1 = con.prepareStatement(
                        "INSERT INTO hopdong(makh, maxe, manv, ngaythue, ngaytra_dukien, trangthai) VALUES (?,?,?,?,?, 'DANG_THUE')");
                     PreparedStatement ps2 = con.prepareStatement(
                             "UPDATE xemay SET trangthai='DANG_THUE' WHERE id=?")) {
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
                napDuLieu();
                JOptionPane.showMessageDialog(dialog, "Tạo hợp đồng thành công!");
                return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage());
                return false;
            }
        }), BorderLayout.SOUTH);

        dialog.setSize(560, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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

        try (Connection con = KetNoiCSDL.getConnection()) {
            int maXe;
            LocalDate ngayThue, ngayTraDuKien;
            long giaThue;

            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT h.maxe, h.ngaythue, h.ngaytra_dukien, x.giathue " +
                    "FROM hopdong h JOIN xemay x ON h.maxe = x.id WHERE h.id = ?")) {
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
            try (PreparedStatement ps1 = con.prepareStatement(
                    "UPDATE hopdong SET ngaytra_thucte=?, tongtien=?, trangthai='DA_TRA' WHERE id=?");
                 PreparedStatement ps2 = con.prepareStatement(
                         "UPDATE xemay SET trangthai='SAN_SANG' WHERE id=?")) {
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
