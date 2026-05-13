package gui;

import database.KetNoiCSDL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FormXeMay extends JPanel implements ActionListener {
    JTextField txtBienSo, txtHangXe, txtModel, txtGia, txtTimKiem;
    JComboBox<String> cboTrangThai;
    JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTim;
    DefaultTableModel model;
    JTable bang;

    public FormXeMay() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        GUI();
        napDuLieu();
    }

    public void GUI() {
        JPanel pForm = new JPanel(new GridLayout(3, 4, 8, 6));
        pForm.setBorder(BorderFactory.createTitledBorder("Thông tin xe máy"));

        pForm.add(new JLabel("Biển số:"));
        txtBienSo = new JTextField();
        pForm.add(txtBienSo);

        pForm.add(new JLabel("Hãng xe:"));
        txtHangXe = new JTextField();
        pForm.add(txtHangXe);

        pForm.add(new JLabel("Model:"));
        txtModel = new JTextField();
        pForm.add(txtModel);

        pForm.add(new JLabel("Giá thuê (VNĐ/ngày):"));
        txtGia = new JTextField();
        pForm.add(txtGia);

        pForm.add(new JLabel("Trạng thái:"));
        cboTrangThai = new JComboBox<>(new String[]{"SAN_SANG", "DANG_THUE", "BAO_TRI"});
        pForm.add(cboTrangThai);

        add(pForm, BorderLayout.NORTH);

        String[] cot = {"ID", "Biển số", "Hãng", "Model", "Giá thuê", "Trạng thái"};
        model = new DefaultTableModel(cot, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bang = new JTable(model);
        bang.getSelectionModel().addListSelectionListener(e -> chonDong());
        add(new JScrollPane(bang), BorderLayout.CENTER);

        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xoá");
        btnLamMoi = new JButton("Làm mới");
        btnTim = new JButton("Tìm");
        txtTimKiem = new JTextField(15);

        JPanel pNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        pNut.add(btnThem);
        pNut.add(btnSua);
        pNut.add(btnXoa);
        pNut.add(btnLamMoi);
        pNut.add(new JLabel("       Tìm kiếm:"));
        pNut.add(txtTimKiem);
        pNut.add(btnTim);
        add(pNut, BorderLayout.SOUTH);

        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnTim.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnThem) them();
        else if (e.getSource() == btnSua) sua();
        else if (e.getSource() == btnXoa) xoa();
        else if (e.getSource() == btnLamMoi) {
            xoaForm();
            txtTimKiem.setText("");
            napDuLieu();
        }
        else if (e.getSource() == btnTim) tim();
    }

    private void napDuLieu() {
        model.setRowCount(0);
        String sql = "SELECT * FROM xemay ORDER BY id";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
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
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải xe: " + ex.getMessage());
        }
    }

    private void chonDong() {
        int row = bang.getSelectedRow();
        if (row < 0) return;
        txtBienSo.setText(model.getValueAt(row, 1).toString());
        txtHangXe.setText(model.getValueAt(row, 2).toString());
        txtModel.setText(model.getValueAt(row, 3).toString());
        txtGia.setText(model.getValueAt(row, 4).toString());
        cboTrangThai.setSelectedItem(model.getValueAt(row, 5).toString());
    }

    private void them() {
        if (!kiemTraForm()) return;
        String sql = "INSERT INTO xemay(bienso, hangxe, model, giathue, trangthai) VALUES (?,?,?,?,?)";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, txtBienSo.getText().trim());
            ps.setString(2, txtHangXe.getText().trim());
            ps.setString(3, txtModel.getText().trim());
            ps.setLong(4, Long.parseLong(txtGia.getText().trim()));
            ps.setString(5, (String) cboTrangThai.getSelectedItem());
            ps.executeUpdate();
            xoaForm();
            napDuLieu();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm xe: " + ex.getMessage());
        }
    }

    private void sua() {
        int row = bang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn dòng cần sửa!");
            return;
        }
        if (!kiemTraForm()) return;
        int id = (int) model.getValueAt(row, 0);
        String sql = "UPDATE xemay SET bienso=?, hangxe=?, model=?, giathue=?, trangthai=? WHERE id=?";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, txtBienSo.getText().trim());
            ps.setString(2, txtHangXe.getText().trim());
            ps.setString(3, txtModel.getText().trim());
            ps.setLong(4, Long.parseLong(txtGia.getText().trim()));
            ps.setString(5, (String) cboTrangThai.getSelectedItem());
            ps.setInt(6, id);
            ps.executeUpdate();
            napDuLieu();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa xe: " + ex.getMessage());
        }
    }

    private void xoa() {
        int row = bang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn dòng cần xoá!");
            return;
        }
        int chon = JOptionPane.showConfirmDialog(this, "Xác nhận xoá xe này?",
                "Xoá", JOptionPane.YES_NO_OPTION);
        if (chon != JOptionPane.YES_OPTION) return;

        int id = (int) model.getValueAt(row, 0);
        String sql = "DELETE FROM xemay WHERE id=?";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            xoaForm();
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

    private boolean kiemTraForm() {
        if (txtBienSo.getText().trim().isEmpty()
                || txtHangXe.getText().trim().isEmpty()
                || txtGia.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập đủ biển số, hãng và giá thuê!");
            return false;
        }
        try {
            Long.parseLong(txtGia.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá thuê phải là số!");
            return false;
        }
        return true;
    }

    private void xoaForm() {
        txtBienSo.setText("");
        txtHangXe.setText("");
        txtModel.setText("");
        txtGia.setText("");
        cboTrangThai.setSelectedIndex(0);
        bang.clearSelection();
    }
}
