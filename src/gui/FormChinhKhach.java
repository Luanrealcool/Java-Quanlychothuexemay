package gui;

import dto.KhachHangDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormChinhKhach extends JFrame implements ActionListener {
    static final Color SIDEBAR_BG = GiaoDien.SIDEBAR;
    static final Color SIDEBAR_ACTIVE = GiaoDien.XANH_CHINH;

    KhachHangDTO khachHang;
    JButton btnThueXe, btnHopDongCuaToi, btnDangXuat;
    CardLayout cardLayout;
    JPanel pContent;
    JButton btnHienTai;

    public FormChinhKhach(KhachHangDTO kh) {
        super("Trang khách hàng");
        this.khachHang = kh;
        GUI();
        setMinimumSize(new Dimension(1100, 650));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
    }

    public void GUI() {
        setLayout(new BorderLayout());
        add(taoHeader(), BorderLayout.NORTH);
        add(taoSidebar(), BorderLayout.WEST);
        add(taoContent(), BorderLayout.CENTER);
        chonMenu(btnThueXe, "THUE");
    }

    private JPanel taoHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(GiaoDien.XANH_DAM);
        p.setBorder(BorderFactory.createEmptyBorder(14, 25, 14, 25));

        JLabel title = new JLabel("🏍  QUẢN LÝ CHO THUÊ XE MÁY DU LỊCH");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        p.add(title, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

        JLabel lblUser = new JLabel("👤  " + khachHang.getHoTen() + "  (Khách hàng)");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setForeground(Color.WHITE);
        right.add(lblUser);

        btnDangXuat = new JButton("Đăng xuất");
        GiaoDien.styleNut(btnDangXuat, GiaoDien.DO);
        btnDangXuat.addActionListener(this);
        right.add(btnDangXuat);

        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel taoSidebar() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(SIDEBAR_BG);
        p.setPreferredSize(new Dimension(230, 0));
        p.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel lblTitle = new JLabel("  MENU KHÁCH HÀNG");
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblTitle.setForeground(new Color(140, 160, 180));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 20, 12, 20));
        p.add(lblTitle);

        btnThueXe = nutSidebar("🏍    Đặt thuê xe");
        btnHopDongCuaToi = nutSidebar("📄    Hợp đồng của tôi");

        p.add(btnThueXe);
        p.add(Box.createVerticalStrut(2));
        p.add(btnHopDongCuaToi);
        p.add(Box.createVerticalGlue());
        return p;
    }

    private JButton nutSidebar(String text) {
        JButton b = new JButton(text);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        b.setMinimumSize(new Dimension(0, 50));
        b.setPreferredSize(new Dimension(230, 50));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        b.setBackground(SIDEBAR_BG);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addActionListener(this);
        return b;
    }

    private JPanel taoContent() {
        cardLayout = new CardLayout();
        pContent = new JPanel(cardLayout);
        pContent.setBackground(new Color(245, 247, 250));
        pContent.add(new FormThueXeKhach(khachHang), "THUE");
        pContent.add(new FormHopDongCuaToi(khachHang), "HOPDONG");
        return pContent;
    }

    private void chonMenu(JButton btn, String card) {
        if (btnHienTai != null) btnHienTai.setBackground(SIDEBAR_BG);
        btn.setBackground(SIDEBAR_ACTIVE);
        btnHienTai = btn;
        cardLayout.show(pContent, card);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnThueXe) chonMenu(btnThueXe, "THUE");
        else if (e.getSource() == btnHopDongCuaToi) chonMenu(btnHopDongCuaToi, "HOPDONG");
        else if (e.getSource() == btnDangXuat) {
            dispose();
            new FormDangNhap().setVisible(true);
        }
    }
}
