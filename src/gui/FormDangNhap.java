package gui;

import database.KetNoiCSDL;
import database.KhachHang;
import database.NhanVien;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FormDangNhap extends JFrame implements ActionListener {
    JTextField txtTaiKhoan;
    JPasswordField txtMatKhau;
    JButton btnDangNhap, btnThoat;

    public FormDangNhap() {
        super("Đăng nhập hệ thống");
        GUI();
        setSize(380, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

    public void GUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel tieuDe = new JLabel("QUẢN LÝ CHO THUÊ XE MÁY", SwingConstants.CENTER);
        tieuDe.setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(tieuDe, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1;
        txtTaiKhoan = new JTextField(15);
        panel.add(txtTaiKhoan, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        txtMatKhau = new JPasswordField(15);
        panel.add(txtMatKhau, gbc);

        btnDangNhap = new JButton("Đăng nhập");
        btnThoat = new JButton("Thoát");
        JPanel pNut = new JPanel();
        pNut.add(btnDangNhap);
        pNut.add(btnThoat);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(pNut, gbc);

        add(panel);

        btnDangNhap.addActionListener(this);
        btnThoat.addActionListener(this);
        getRootPane().setDefaultButton(btnDangNhap);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnDangNhap) {
            dangNhap();
        } else if (e.getSource() == btnThoat) {
            System.exit(0);
        }
    }

    private void dangNhap() {
        String taiKhoan = txtTaiKhoan.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());

        if (taiKhoan.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập đủ tên đăng nhập và mật khẩu!");
            return;
        }

        try (Connection con = KetNoiCSDL.getConnection()) {
            NhanVien nv = timNhanVien(con, taiKhoan, matKhau);
            if (nv != null) {
                new FormChinh(nv).setVisible(true);
                dispose();
                return;
            }

            KhachHang kh = timKhachHang(con, taiKhoan, matKhau);
            if (kh != null) {
                new FormChinhKhach(kh).setVisible(true);
                dispose();
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Sai tên đăng nhập hoặc mật khẩu!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMatKhau.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi kết nối CSDL: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private NhanVien timNhanVien(Connection con, String taiKhoan, String matKhau) throws SQLException {
        String sql = "SELECT id, tendangnhap, hoten FROM nhanvien WHERE tendangnhap=? AND matkhau=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, taiKhoan);
            ps.setString(2, matKhau);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new NhanVien(rs.getInt("id"),
                            rs.getString("tendangnhap"),
                            rs.getString("hoten"));
                }
            }
        }
        return null;
    }

    private KhachHang timKhachHang(Connection con, String taiKhoan, String matKhau) throws SQLException {
        String sql = "SELECT id, cmnd, hoten, sdt, diachi, tendangnhap " +
                "FROM khachhang WHERE tendangnhap=? AND matkhau=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, taiKhoan);
            ps.setString(2, matKhau);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    KhachHang kh = new KhachHang(
                            rs.getInt("id"),
                            rs.getString("cmnd"),
                            rs.getString("hoten"),
                            rs.getString("sdt"),
                            rs.getString("diachi")
                    );
                    kh.setTenDangNhap(rs.getString("tendangnhap"));
                    return kh;
                }
            }
        }
        return null;
    }
}
