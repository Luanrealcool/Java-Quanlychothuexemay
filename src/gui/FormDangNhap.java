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
        super("Đăng nhập - Quản lý cho thuê xe máy");
        GUI();
        setSize(900, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

    public void GUI() {
        setLayout(new BorderLayout());
        add(taoPanelTrai(), BorderLayout.WEST);
        add(taoPanelPhai(), BorderLayout.CENTER);
        getRootPane().setDefaultButton(btnDangNhap);
    }

    private JPanel taoPanelTrai() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(GiaoDien.XANH_DAM);
        p.setPreferredSize(new Dimension(380, 0));

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel lblIcon = new JLabel("🏍");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 100));
        lblIcon.setForeground(Color.WHITE);
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(lblIcon);

        inner.add(Box.createVerticalStrut(20));

        JLabel lblTen = new JLabel("MOTO RENTAL");
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTen.setForeground(Color.WHITE);
        lblTen.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(lblTen);

        inner.add(Box.createVerticalStrut(8));

        JLabel lblPhu = new JLabel("Cho thuê xe máy du lịch");
        lblPhu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPhu.setForeground(new Color(200, 215, 235));
        lblPhu.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(lblPhu);

        inner.add(Box.createVerticalStrut(40));

        JPanel pLine = new JPanel();
        pLine.setOpaque(false);
        pLine.setMaximumSize(new Dimension(160, 3));
        pLine.setBackground(new Color(100, 150, 220));
        pLine.setAlignmentX(Component.CENTER_ALIGNMENT);
        pLine.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(100, 150, 220)));
        inner.add(pLine);

        inner.add(Box.createVerticalStrut(30));

        JLabel lblMota = new JLabel("<html><div style='text-align:center;color:rgb(200,215,235);font-size:12px;line-height:1.7;'>Giải pháp quản lý<br>cho thuê xe máy du lịch<br>đơn giản và tiện lợi</div></html>");
        lblMota.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(lblMota);

        p.add(inner);
        return p;
    }

    private JPanel taoPanelPhai() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(GiaoDien.TRANG);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 60));

        JLabel lblTieuDe = new JLabel("Đăng nhập");
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTieuDe.setForeground(new Color(33, 37, 41));
        lblTieuDe.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblTieuDe);

        form.add(Box.createVerticalStrut(8));

        JLabel lblPhuDe = new JLabel("Vui lòng nhập thông tin tài khoản của bạn");
        lblPhuDe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPhuDe.setForeground(new Color(108, 117, 125));
        lblPhuDe.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblPhuDe);

        form.add(Box.createVerticalStrut(35));

        JLabel lblTk = new JLabel("TÊN ĐĂNG NHẬP");
        lblTk.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTk.setForeground(new Color(73, 80, 87));
        lblTk.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblTk);
        form.add(Box.createVerticalStrut(8));

        txtTaiKhoan = new JTextField();
        styleInput(txtTaiKhoan);
        form.add(txtTaiKhoan);

        form.add(Box.createVerticalStrut(20));

        JLabel lblMk = new JLabel("MẬT KHẨU");
        lblMk.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblMk.setForeground(new Color(73, 80, 87));
        lblMk.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblMk);
        form.add(Box.createVerticalStrut(8));

        txtMatKhau = new JPasswordField();
        styleInput(txtMatKhau);
        form.add(txtMatKhau);

        form.add(Box.createVerticalStrut(30));

        btnDangNhap = new JButton("ĐĂNG NHẬP");
        GiaoDien.styleNut(btnDangNhap, GiaoDien.XANH_CHINH);
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDangNhap.setBorder(BorderFactory.createEmptyBorder(14, 0, 14, 0));
        btnDangNhap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnDangNhap.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnDangNhap.addActionListener(this);
        form.add(btnDangNhap);

        form.add(Box.createVerticalStrut(10));

        btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnThoat.setBackground(GiaoDien.TRANG);
        btnThoat.setForeground(new Color(108, 117, 125));
        btnThoat.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218)),
                BorderFactory.createEmptyBorder(10, 0, 10, 0)));
        btnThoat.setFocusPainted(false);
        btnThoat.setOpaque(true);
        btnThoat.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnThoat.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnThoat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThoat.addActionListener(this);
        form.add(btnThoat);

        form.add(Box.createVerticalStrut(30));

        JLabel lblHint = new JLabel("<html><div style='color:#6c757d;font-size:11px;line-height:1.7;'>"
                + "<b style='color:#495057;'>TÀI KHOẢN MẪU</b><br>"
                + "• Nhân viên: <b>admin</b> / 123456<br>"
                + "• Khách hàng: <b>kh001</b> / 123<br>"
                + "• Khách hàng: <b>kh002</b> / 123"
                + "</div></html>");
        lblHint.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblHint);

        p.add(form);
        return p;
    }

    private void styleInput(JTextField tf) {
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                BorderFactory.createEmptyBorder(11, 14, 11, 14)));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnDangNhap) dangNhap();
        else if (e.getSource() == btnThoat) System.exit(0);
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
