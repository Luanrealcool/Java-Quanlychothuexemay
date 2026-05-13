package gui;

import database.NhanVien;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormChinh extends JFrame implements ActionListener {
    NhanVien nhanVien;
    JButton btnDangXuat;

    public FormChinh(NhanVien nv) {
        super("Quản lý cho thuê xe máy - Người dùng: " + nv.getHoTen());
        this.nhanVien = nv;
        GUI();
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void GUI() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Xe máy", new FormXeMay());
        tabs.addTab("Khách hàng", new FormKhachHang());
        tabs.addTab("Hợp đồng", new FormHopDong(nhanVien));
        tabs.addTab("Thống kê", new FormThongKe());
        add(tabs, BorderLayout.CENTER);

        JPanel pTrang = new JPanel(new BorderLayout());
        pTrang.setBorder(BorderFactory.createEtchedBorder());
        pTrang.add(new JLabel("  Đăng nhập: " + nhanVien.getHoTen()
                + " (" + nhanVien.getTenDangNhap() + ")"), BorderLayout.WEST);

        btnDangXuat = new JButton("Đăng xuất");
        btnDangXuat.addActionListener(this);
        pTrang.add(btnDangXuat, BorderLayout.EAST);

        add(pTrang, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnDangXuat) {
            dispose();
            new FormDangNhap().setVisible(true);
        }
    }
}
