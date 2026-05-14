package gui;

import database.KhachHang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormChinhKhach extends JFrame implements ActionListener {
    KhachHang khachHang;
    JButton btnDangXuat;

    public FormChinhKhach(KhachHang kh) {
        super("Trang khách hàng - Xin chào: " + kh.getHoTen());
        this.khachHang = kh;
        GUI();
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void GUI() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Đặt thuê xe", new FormThueXeKhach(khachHang));
        tabs.addTab("Hợp đồng của tôi", new FormHopDongCuaToi(khachHang));
        add(tabs, BorderLayout.CENTER);

        JPanel pTrang = new JPanel(new BorderLayout());
        pTrang.setBorder(BorderFactory.createEtchedBorder());
        pTrang.add(new JLabel("  Khách: " + khachHang.getHoTen()
                + " (" + khachHang.getTenDangNhap() + ")"), BorderLayout.WEST);

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
