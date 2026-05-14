package gui;

import database.KetNoiCSDL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FormKhachHang extends JPanel implements ActionListener {
    JTextField txtCmnd, txtHoTen, txtSdt, txtDiaChi, txtTenDangNhap, txtMatKhau, txtTimKiem;
    JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTim;
    DefaultTableModel model;
    JTable bang;

    public FormKhachHang() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        GUI();
        napDuLieu();
    }

    public void GUI() {
        JPanel pForm = new JPanel(new GridLayout(3, 4, 8, 6));
        pForm.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));

        pForm.add(new JLabel("CMND/CCCD:"));
        txtCmnd = new JTextField();
        pForm.add(txtCmnd);

        pForm.add(new JLabel("Họ tên:"));
        txtHoTen = new JTextField();
        pForm.add(txtHoTen);

        pForm.add(new JLabel("Số điện thoại:"));
        txtSdt = new JTextField();
        pForm.add(txtSdt);

        pForm.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField();
        pForm.add(txtDiaChi);

        pForm.add(new JLabel("Tên đăng nhập:"));
        txtTenDangNhap = new JTextField();
        pForm.add(txtTenDangNhap);

        pForm.add(new JLabel("Mật khẩu:"));
        txtMatKhau = new JTextField();
        pForm.add(txtMatKhau);

        add(pForm, BorderLayout.NORTH);

        String[] cot = {"ID", "CMND", "Họ tên", "SĐT", "Địa chỉ", "Tên đăng nhập"};
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
        String sql = "SELECT * FROM khachhang ORDER BY id";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
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
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải khách hàng: " + ex.getMessage());
        }
    }

    private void chonDong() {
        int row = bang.getSelectedRow();
        if (row < 0) return;
        txtCmnd.setText(getStr(row, 1));
        txtHoTen.setText(getStr(row, 2));
        txtSdt.setText(getStr(row, 3));
        txtDiaChi.setText(getStr(row, 4));
        txtTenDangNhap.setText(getStr(row, 5));
        txtMatKhau.setText("");
    }

    private String getStr(int row, int col) {
        Object v = model.getValueAt(row, col);
        return v == null ? "" : v.toString();
    }

    private void them() {
        if (!kiemTraForm()) return;
        String sql = "INSERT INTO khachhang(cmnd, hoten, sdt, diachi, tendangnhap, matkhau) VALUES (?,?,?,?,?,?)";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, txtCmnd.getText().trim());
            ps.setString(2, txtHoTen.getText().trim());
            ps.setString(3, txtSdt.getText().trim());
            ps.setString(4, txtDiaChi.getText().trim());
            setOrNull(ps, 5, txtTenDangNhap.getText().trim());
            setOrNull(ps, 6, txtMatKhau.getText().trim());
            ps.executeUpdate();
            xoaForm();
            napDuLieu();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm khách: " + ex.getMessage());
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

        String sql;
        String mkMoi = txtMatKhau.getText().trim();
        if (mkMoi.isEmpty()) {
            sql = "UPDATE khachhang SET cmnd=?, hoten=?, sdt=?, diachi=?, tendangnhap=? WHERE id=?";
        } else {
            sql = "UPDATE khachhang SET cmnd=?, hoten=?, sdt=?, diachi=?, tendangnhap=?, matkhau=? WHERE id=?";
        }

        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, txtCmnd.getText().trim());
            ps.setString(2, txtHoTen.getText().trim());
            ps.setString(3, txtSdt.getText().trim());
            ps.setString(4, txtDiaChi.getText().trim());
            setOrNull(ps, 5, txtTenDangNhap.getText().trim());
            if (mkMoi.isEmpty()) {
                ps.setInt(6, id);
            } else {
                ps.setString(6, mkMoi);
                ps.setInt(7, id);
            }
            ps.executeUpdate();
            napDuLieu();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa khách: " + ex.getMessage());
        }
    }

    private void xoa() {
        int row = bang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn dòng cần xoá!");
            return;
        }
        int chon = JOptionPane.showConfirmDialog(this, "Xác nhận xoá khách hàng này?",
                "Xoá", JOptionPane.YES_NO_OPTION);
        if (chon != JOptionPane.YES_OPTION) return;

        int id = (int) model.getValueAt(row, 0);
        String sql = "DELETE FROM khachhang WHERE id=?";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            xoaForm();
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

    private boolean kiemTraForm() {
        if (txtCmnd.getText().trim().isEmpty()
                || txtHoTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập đủ CMND và họ tên!");
            return false;
        }
        return true;
    }

    private void setOrNull(PreparedStatement ps, int idx, String v) throws SQLException {
        if (v == null || v.isEmpty()) ps.setNull(idx, Types.VARCHAR);
        else ps.setString(idx, v);
    }

    private void xoaForm() {
        txtCmnd.setText("");
        txtHoTen.setText("");
        txtSdt.setText("");
        txtDiaChi.setText("");
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
        bang.clearSelection();
    }
}
