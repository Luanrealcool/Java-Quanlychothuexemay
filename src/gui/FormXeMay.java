package gui;

import database.KetNoiCSDL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FormXeMay extends JPanel implements ActionListener {
    JTextField txtTimKiem;
    JButton btnThem, btnSua, btnXoa, btnTim;
    DefaultTableModel model;
    JTable bang;
    JLabel lblStatTong, lblStatSanSang, lblStatDangThue, lblStatBaoTri;

    public FormXeMay() {
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

        top.add(GiaoDien.taoPageHeader("Quản lý xe máy",
                "Danh sách xe máy có trong hệ thống"));
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
        lblStatSanSang = new JLabel("0");
        lblStatDangThue = new JLabel("0");
        lblStatBaoTri = new JLabel("0");

        p.add(GiaoDien.taoStatCard("TỔNG XE", lblStatTong, GiaoDien.XANH_CHINH, "🏍"));
        p.add(GiaoDien.taoStatCard("SẴN SÀNG", lblStatSanSang, GiaoDien.XANH_LA, "✓"));
        p.add(GiaoDien.taoStatCard("ĐANG THUÊ", lblStatDangThue, GiaoDien.CAM, "⏱"));
        p.add(GiaoDien.taoStatCard("BẢO TRÌ", lblStatBaoTri, GiaoDien.DO, "🔧"));
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
        btnThem = new JButton("+ Thêm xe");
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
        card.add(GiaoDien.taoCardHeader("DANH SÁCH XE MÁY"), BorderLayout.NORTH);

        String[] cot = {"ID", "Biển số", "Hãng", "Model", "Giá thuê", "Trạng thái"};
        model = new DefaultTableModel(cot, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bang = new JTable(model);
        GiaoDien.styleBang(bang);
        GiaoDien.setRenderer(bang, 4, GiaoDien.rendererTien());
        GiaoDien.setRenderer(bang, 5, GiaoDien.rendererTrangThai());

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
        int tong = 0, ss = 0, dt = 0, bt = 0;
        String sql = "SELECT * FROM xemay ORDER BY id";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String tt = rs.getString("trangthai");
                tong++;
                if ("SAN_SANG".equals(tt)) ss++;
                else if ("DANG_THUE".equals(tt)) dt++;
                else if ("BAO_TRI".equals(tt)) bt++;
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("bienso"),
                        rs.getString("hangxe"),
                        rs.getString("model"),
                        rs.getLong("giathue"),
                        tt
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải xe: " + ex.getMessage());
        }
        lblStatTong.setText(String.valueOf(tong));
        lblStatSanSang.setText(String.valueOf(ss));
        lblStatDangThue.setText(String.valueOf(dt));
        lblStatBaoTri.setText(String.valueOf(bt));
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
                laEdit ? "Sửa xe máy" : "Thêm xe máy",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());

        Color headerColor = laEdit ? GiaoDien.XANH_CHINH : GiaoDien.XANH_LA;
        dialog.add(taoDialogHeader(laEdit ? "Sửa thông tin xe" : "Thêm xe máy mới",
                laEdit ? "✎" : "+", headerColor), BorderLayout.NORTH);

        JTextField txtBs = new JTextField();
        JTextField txtHang = new JTextField();
        JTextField txtModel = new JTextField();
        JTextField txtGia = new JTextField();
        JComboBox<String> cboTt = new JComboBox<>(new String[]{"SAN_SANG", "DANG_THUE", "BAO_TRI"});
        GiaoDien.styleInput(txtBs);
        GiaoDien.styleInput(txtHang);
        GiaoDien.styleInput(txtModel);
        GiaoDien.styleInput(txtGia);
        GiaoDien.styleInput(cboTt);

        if (laEdit) {
            txtBs.setText(model.getValueAt(row, 1).toString());
            txtHang.setText(model.getValueAt(row, 2).toString());
            txtModel.setText(model.getValueAt(row, 3).toString());
            txtGia.setText(model.getValueAt(row, 4).toString());
            cboTt.setSelectedItem(model.getValueAt(row, 5).toString());
        }

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(GiaoDien.TRANG);
        form.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 6, 8, 6);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        addFormRow(form, g, 0, "Biển số *", txtBs);
        addFormRow(form, g, 1, "Hãng xe *", txtHang);
        addFormRow(form, g, 2, "Model", txtModel);
        addFormRow(form, g, 3, "Giá thuê (VNĐ/ngày) *", txtGia);
        addFormRow(form, g, 4, "Trạng thái", cboTt);

        dialog.add(form, BorderLayout.CENTER);

        final int finalRow = row;
        dialog.add(taoDialogFooter(dialog, laEdit, () -> {
            if (txtBs.getText().trim().isEmpty() || txtHang.getText().trim().isEmpty()
                    || txtGia.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nhập đủ biển số, hãng và giá thuê!");
                return false;
            }
            long gia;
            try { gia = Long.parseLong(txtGia.getText().trim()); }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Giá thuê phải là số!");
                return false;
            }
            try (Connection con = KetNoiCSDL.getConnection()) {
                if (laEdit) {
                    int id = (int) model.getValueAt(finalRow, 0);
                    try (PreparedStatement ps = con.prepareStatement(
                            "UPDATE xemay SET bienso=?, hangxe=?, model=?, giathue=?, trangthai=? WHERE id=?")) {
                        ps.setString(1, txtBs.getText().trim());
                        ps.setString(2, txtHang.getText().trim());
                        ps.setString(3, txtModel.getText().trim());
                        ps.setLong(4, gia);
                        ps.setString(5, (String) cboTt.getSelectedItem());
                        ps.setInt(6, id);
                        ps.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO xemay(bienso, hangxe, model, giathue, trangthai) VALUES (?,?,?,?,?)")) {
                        ps.setString(1, txtBs.getText().trim());
                        ps.setString(2, txtHang.getText().trim());
                        ps.setString(3, txtModel.getText().trim());
                        ps.setLong(4, gia);
                        ps.setString(5, (String) cboTt.getSelectedItem());
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

        dialog.setSize(520, 460);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    static void addFormRow(JPanel form, GridBagConstraints g, int row, String label, JComponent comp) {
        g.gridx = 0; g.gridy = row; g.weightx = 0;
        form.add(GiaoDien.taoLabel(label), g);
        g.gridx = 1; g.weightx = 1;
        form.add(comp, g);
    }

    static JPanel taoDialogHeader(String title, String icon, Color color) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(color);
        header.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        lblIcon.setForeground(Color.WHITE);
        JLabel lblTitle = new JLabel("  " + title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(lblIcon);
        left.add(lblTitle);
        header.add(left, BorderLayout.WEST);
        return header;
    }

    interface LuuAction {
        boolean luu();
    }

    static JPanel taoDialogFooter(JDialog dialog, boolean laEdit, LuuAction action) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
        footer.setBackground(GiaoDien.XAM_NHAT);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, GiaoDien.VIEN));

        JButton btnHuy = new JButton("Huỷ");
        btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnHuy.setBackground(GiaoDien.TRANG);
        btnHuy.setForeground(GiaoDien.XAM_DAM);
        btnHuy.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GiaoDien.VIEN_INPUT),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)));
        btnHuy.setFocusPainted(false);
        btnHuy.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnLuu = new JButton(laEdit ? "✓ Lưu thay đổi" : "+ Thêm");
        GiaoDien.styleNut(btnLuu, laEdit ? GiaoDien.XANH_CHINH : GiaoDien.XANH_LA);

        footer.add(btnHuy);
        footer.add(btnLuu);

        btnHuy.addActionListener(e -> dialog.dispose());
        btnLuu.addActionListener(e -> {
            if (action.luu()) dialog.dispose();
        });
        dialog.getRootPane().setDefaultButton(btnLuu);
        return footer;
    }

    private void xoa() {
        int row = bang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn dòng cần xoá!");
            return;
        }
        String bienSo = model.getValueAt(row, 1).toString();
        int chon = JOptionPane.showConfirmDialog(this,
                "Xác nhận xoá xe \"" + bienSo + "\"?",
                "Xoá xe máy", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (chon != JOptionPane.YES_OPTION) return;

        int id = (int) model.getValueAt(row, 0);
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM xemay WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            napDuLieu();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Không xoá được (có thể xe đang có hợp đồng): " + ex.getMessage());
        }
    }

    private void tim() {
        String tu = txtTimKiem.getText().trim();
        if (tu.isEmpty()) { napDuLieu(); return; }
        model.setRowCount(0);
        String sql = "SELECT * FROM xemay WHERE bienso LIKE ? OR hangxe LIKE ? OR model LIKE ? ORDER BY id";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String like = "%" + tu + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("bienso"),
                            rs.getString("hangxe"),
                            rs.getString("model"),
                            rs.getLong("giathue"),
                            rs.getString("trangthai")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm: " + ex.getMessage());
        }
    }
}
