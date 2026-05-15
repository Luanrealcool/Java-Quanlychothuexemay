package gui;

import database.KetNoiCSDL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FormKhachHang extends JPanel implements ActionListener {
    JTextField txtTimKiem;
    JButton btnThem, btnSua, btnXoa, btnTim;
    DefaultTableModel model;
    JTable bang;
    JLabel lblStatTong, lblStatCoTk, lblStatChuaTk;

    public FormKhachHang() {
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

        top.add(GiaoDien.taoPageHeader("Quản lý khách hàng",
                "Danh sách khách hàng trong hệ thống"));
        top.add(Box.createVerticalStrut(16));
        top.add(taoStatsRow());
        top.add(Box.createVerticalStrut(16));
        top.add(taoToolbar());
        top.add(Box.createVerticalStrut(10));

        add(top, BorderLayout.NORTH);
        add(taoTableCard(), BorderLayout.CENTER);
    }

    private JPanel taoStatsRow() {
        JPanel p = new JPanel(new GridLayout(1, 3, 14, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblStatTong = new JLabel("0");
        lblStatCoTk = new JLabel("0");
        lblStatChuaTk = new JLabel("0");

        p.add(GiaoDien.taoStatCard("TỔNG KHÁCH HÀNG", lblStatTong, GiaoDien.XANH_CHINH, "👥"));
        p.add(GiaoDien.taoStatCard("CÓ TÀI KHOẢN", lblStatCoTk, GiaoDien.XANH_LA, "🔑"));
        p.add(GiaoDien.taoStatCard("CHƯA CÓ TK", lblStatChuaTk, GiaoDien.XAM, "👤"));
        return p;
    }

    private JPanel taoToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        JPanel pTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pTim.setOpaque(false);
        JLabel lblTim = new JLabel("🔍");
        lblTim.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        pTim.add(lblTim);
        txtTimKiem = new JTextField(22);
        GiaoDien.styleInput(txtTimKiem);
        pTim.add(txtTimKiem);
        btnTim = new JButton("Tìm");
        GiaoDien.styleNut(btnTim, GiaoDien.TIM);
        pTim.add(btnTim);
        toolbar.add(pTim, BorderLayout.WEST);

        JPanel pAct = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        pAct.setOpaque(false);
        btnThem = new JButton("+ Thêm khách hàng");
        btnSua = new JButton("✎ Sửa");
        btnXoa = new JButton("✕ Xoá");
        GiaoDien.styleNut(btnThem, GiaoDien.XANH_LA);
        GiaoDien.styleNut(btnSua, GiaoDien.XANH_CHINH);
        GiaoDien.styleNut(btnXoa, GiaoDien.DO);
        pAct.add(btnThem);
        pAct.add(btnSua);
        pAct.add(btnXoa);
        toolbar.add(pAct, BorderLayout.EAST);

        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnTim.addActionListener(this);
        return toolbar;
    }

    private JPanel taoTableCard() {
        JPanel card = GiaoDien.taoCard();
        card.add(GiaoDien.taoCardHeader("DANH SÁCH KHÁCH HÀNG"), BorderLayout.NORTH);

        String[] cot = {"ID", "CMND", "Họ tên", "SĐT", "Địa chỉ", "Tên đăng nhập"};
        model = new DefaultTableModel(cot, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bang = new JTable(model);
        GiaoDien.styleBang(bang);

        JScrollPane scroll = new JScrollPane(bang);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(GiaoDien.TRANG);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnThem) hienDialog(false);
        else if (e.getSource() == btnSua) hienDialog(true);
        else if (e.getSource() == btnXoa) xoa();
        else if (e.getSource() == btnTim) tim();
    }

    private void napDuLieu() {
        model.setRowCount(0);
        int tong = 0, co = 0;
        String sql = "SELECT * FROM khachhang ORDER BY id";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String tdn = rs.getString("tendangnhap");
                tong++;
                if (tdn != null && !tdn.isEmpty()) co++;
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("cmnd"),
                        rs.getString("hoten"),
                        rs.getString("sdt"),
                        rs.getString("diachi"),
                        tdn
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải khách hàng: " + ex.getMessage());
        }
        lblStatTong.setText(String.valueOf(tong));
        lblStatCoTk.setText(String.valueOf(co));
        lblStatChuaTk.setText(String.valueOf(tong - co));
    }

    private void hienDialog(boolean laEdit) {
        int row = -1;
        if (laEdit) {
            row = bang.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Chọn dòng cần sửa!");
                return;
            }
        }

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                laEdit ? "Sửa khách hàng" : "Thêm khách hàng",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());

        Color headerColor = laEdit ? GiaoDien.XANH_CHINH : GiaoDien.XANH_LA;
        dialog.add(FormXeMay.taoDialogHeader(
                laEdit ? "Sửa thông tin khách hàng" : "Thêm khách hàng mới",
                laEdit ? "✎" : "+", headerColor), BorderLayout.NORTH);

        JTextField txtCmnd = new JTextField();
        JTextField txtHoTen = new JTextField();
        JTextField txtSdt = new JTextField();
        JTextField txtDiaChi = new JTextField();
        JTextField txtTdn = new JTextField();
        JTextField txtMk = new JTextField();
        GiaoDien.styleInput(txtCmnd);
        GiaoDien.styleInput(txtHoTen);
        GiaoDien.styleInput(txtSdt);
        GiaoDien.styleInput(txtDiaChi);
        GiaoDien.styleInput(txtTdn);
        GiaoDien.styleInput(txtMk);

        if (laEdit) {
            txtCmnd.setText(getStr(row, 1));
            txtHoTen.setText(getStr(row, 2));
            txtSdt.setText(getStr(row, 3));
            txtDiaChi.setText(getStr(row, 4));
            txtTdn.setText(getStr(row, 5));
        }

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(GiaoDien.TRANG);
        form.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 6, 8, 6);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        FormXeMay.addFormRow(form, g, 0, "CMND/CCCD *", txtCmnd);
        FormXeMay.addFormRow(form, g, 1, "Họ tên *", txtHoTen);
        FormXeMay.addFormRow(form, g, 2, "Số điện thoại", txtSdt);
        FormXeMay.addFormRow(form, g, 3, "Địa chỉ", txtDiaChi);
        FormXeMay.addFormRow(form, g, 4, "Tên đăng nhập (tuỳ chọn)", txtTdn);
        FormXeMay.addFormRow(form, g, 5,
                laEdit ? "Mật khẩu (để trống nếu không đổi)" : "Mật khẩu", txtMk);

        dialog.add(form, BorderLayout.CENTER);

        final int finalRow = row;
        dialog.add(FormXeMay.taoDialogFooter(dialog, laEdit, () -> {
            if (txtCmnd.getText().trim().isEmpty() || txtHoTen.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nhập đủ CMND và họ tên!");
                return false;
            }
            try (Connection con = KetNoiCSDL.getConnection()) {
                if (laEdit) {
                    int id = (int) model.getValueAt(finalRow, 0);
                    String mkMoi = txtMk.getText().trim();
                    String sql = mkMoi.isEmpty()
                            ? "UPDATE khachhang SET cmnd=?, hoten=?, sdt=?, diachi=?, tendangnhap=? WHERE id=?"
                            : "UPDATE khachhang SET cmnd=?, hoten=?, sdt=?, diachi=?, tendangnhap=?, matkhau=? WHERE id=?";
                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setString(1, txtCmnd.getText().trim());
                        ps.setString(2, txtHoTen.getText().trim());
                        ps.setString(3, txtSdt.getText().trim());
                        ps.setString(4, txtDiaChi.getText().trim());
                        setOrNull(ps, 5, txtTdn.getText().trim());
                        if (mkMoi.isEmpty()) ps.setInt(6, id);
                        else { ps.setString(6, mkMoi); ps.setInt(7, id); }
                        ps.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO khachhang(cmnd, hoten, sdt, diachi, tendangnhap, matkhau) VALUES (?,?,?,?,?,?)")) {
                        ps.setString(1, txtCmnd.getText().trim());
                        ps.setString(2, txtHoTen.getText().trim());
                        ps.setString(3, txtSdt.getText().trim());
                        ps.setString(4, txtDiaChi.getText().trim());
                        setOrNull(ps, 5, txtTdn.getText().trim());
                        setOrNull(ps, 6, txtMk.getText().trim());
                        ps.executeUpdate();
                    }
                }
                napDuLieu();
                return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage());
                return false;
            }
        }), BorderLayout.SOUTH);

        dialog.setSize(520, 520);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private String getStr(int row, int col) {
        Object v = model.getValueAt(row, col);
        return v == null ? "" : v.toString();
    }

    private void setOrNull(PreparedStatement ps, int idx, String v) throws SQLException {
        if (v == null || v.isEmpty()) ps.setNull(idx, Types.VARCHAR);
        else ps.setString(idx, v);
    }

    private void xoa() {
        int row = bang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn dòng cần xoá!");
            return;
        }
        String tenKh = model.getValueAt(row, 2).toString();
        int chon = JOptionPane.showConfirmDialog(this,
                "Xác nhận xoá khách hàng \"" + tenKh + "\"?",
                "Xoá khách hàng", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (chon != JOptionPane.YES_OPTION) return;

        int id = (int) model.getValueAt(row, 0);
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM khachhang WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            napDuLieu();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Không xoá được (có thể khách đang có hợp đồng): " + ex.getMessage());
        }
    }

    private void tim() {
        String tu = txtTimKiem.getText().trim();
        if (tu.isEmpty()) { napDuLieu(); return; }
        model.setRowCount(0);
        String sql = "SELECT * FROM khachhang WHERE cmnd LIKE ? OR hoten LIKE ? ORDER BY id";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String like = "%" + tu + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("cmnd"),
                            rs.getString("hoten"),
                            rs.getString("sdt"),
                            rs.getString("diachi"),
                            rs.getString("tendangnhap")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm: " + ex.getMessage());
        }
    }
}
