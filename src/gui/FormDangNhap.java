package gui;

import bus.AuthBUS;
import util.LoggerUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormDangNhap extends JFrame implements ActionListener {
    private final AuthBUS authBUS = new AuthBUS();
    private final bus.KhachHangBUS khachHangBUS = new bus.KhachHangBUS();
    JTextField txtTaiKhoan;
    JPasswordField txtMatKhau;
    JButton btnDangNhap, btnThoat, btnDangKy;

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

        form.add(Box.createVerticalStrut(20));

        JPanel pDangKy = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        pDangKy.setOpaque(false);
        pDangKy.setAlignmentX(Component.LEFT_ALIGNMENT);
        pDangKy.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel lblHoi = new JLabel("Chưa có tài khoản?");
        lblHoi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblHoi.setForeground(new Color(108, 117, 125));
        pDangKy.add(lblHoi);

        btnDangKy = new JButton("Đăng ký ngay");
        btnDangKy.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDangKy.setForeground(GiaoDien.XANH_CHINH);
        btnDangKy.setBackground(GiaoDien.TRANG);
        btnDangKy.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
        btnDangKy.setFocusPainted(false);
        btnDangKy.setBorderPainted(false);
        btnDangKy.setOpaque(false);
        btnDangKy.setContentAreaFilled(false);
        btnDangKy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDangKy.addActionListener(this);
        pDangKy.add(btnDangKy);

        form.add(pDangKy);

        form.add(Box.createVerticalStrut(20));

        JLabel lblHint = new JLabel("<html><div style='color:#6c757d;font-size:11px;line-height:1.7;'>"
                + "<b style='color:#495057;'>TÀI KHOẢN MẪU</b><br>"
                + "• Nhân viên: <b>admin</b> / 123456<br>"
                + "• Khách hàng: <b>kh001</b> / 123"
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
        else if (e.getSource() == btnDangKy) hienDialogDangKy();
    }

    private void hienDialogDangKy() {
        JDialog dialog = new JDialog(this, "Đăng ký tài khoản",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());

        dialog.add(FormXeMay.taoDialogHeader("Đăng ký tài khoản khách hàng", "+",
                GiaoDien.XANH_LA), BorderLayout.NORTH);

        JTextField txtCmnd = new JTextField();
        JTextField txtHoTen = new JTextField();
        JTextField txtSdt = new JTextField();
        JTextField txtTdn = new JTextField();
        JPasswordField txtMk = new JPasswordField();
        JPasswordField txtMk2 = new JPasswordField();
        for (JComponent c : new JComponent[]{txtCmnd, txtHoTen, txtSdt, txtTdn, txtMk, txtMk2}) {
            GiaoDien.styleInput(c);
        }

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(GiaoDien.TRANG);
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(7, 6, 7, 6);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        FormXeMay.addFormRow(form, g, 0, "CMND/CCCD *", txtCmnd);
        FormXeMay.addFormRow(form, g, 1, "Họ tên *", txtHoTen);
        FormXeMay.addFormRow(form, g, 2, "Số điện thoại", txtSdt);
        FormXeMay.addFormRow(form, g, 3, "Tên đăng nhập *", txtTdn);
        FormXeMay.addFormRow(form, g, 4, "Mật khẩu *", txtMk);
        FormXeMay.addFormRow(form, g, 5, "Nhập lại mật khẩu *", txtMk2);

        dialog.add(form, BorderLayout.CENTER);

        dialog.add(FormXeMay.taoDialogFooter(dialog, false, () -> {
            try {
                String mk = new String(txtMk.getPassword()).trim();
                String mk2 = new String(txtMk2.getPassword()).trim();
                if (mk.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Mật khẩu không được trống!");
                    return false;
                }
                if (!mk.equals(mk2)) {
                    JOptionPane.showMessageDialog(dialog, "Mật khẩu nhập lại không khớp!");
                    return false;
                }
                if (txtTdn.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Tên đăng nhập không được trống!");
                    return false;
                }

                dto.KhachHangDTO kh = new dto.KhachHangDTO();
                kh.setCmnd(txtCmnd.getText().trim());
                kh.setHoTen(txtHoTen.getText().trim());
                kh.setSdt(txtSdt.getText().trim());
                kh.setTenDangNhap(txtTdn.getText().trim());

                khachHangBUS.them(kh, mk);

                JOptionPane.showMessageDialog(dialog,
                        "Đăng ký thành công!\nTên đăng nhập: " + kh.getTenDangNhap()
                                + "\nMời bạn đăng nhập để tiếp tục.",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                txtTaiKhoan.setText(kh.getTenDangNhap());
                txtMatKhau.requestFocus();
                return true;
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(),
                        "Lỗi", JOptionPane.WARNING_MESSAGE);
                return false;
            } catch (Exception ex) {
                LoggerUtil.error("Lỗi đăng ký", ex);
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage());
                return false;
            }
        }), BorderLayout.SOUTH);

        dialog.setSize(520, 560);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void dangNhap() {
        String taiKhoan = txtTaiKhoan.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());

        try {
            AuthBUS.KetQuaDangNhap kq = authBUS.dangNhap(taiKhoan, matKhau);
            if (kq.laNhanVien()) {
                LoggerUtil.info("Nhân viên đăng nhập: " + kq.nhanVien.getTenDangNhap());
                new FormChinh(kq.nhanVien).setVisible(true);
                dispose();
            } else if (kq.laKhachHang()) {
                LoggerUtil.info("Khách đăng nhập: " + kq.khachHang.getTenDangNhap());
                new FormChinhKhach(kq.khachHang).setVisible(true);
                dispose();
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMatKhau.setText("");
        } catch (Exception ex) {
            LoggerUtil.error("Lỗi đăng nhập", ex);
            JOptionPane.showMessageDialog(this,
                    "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
