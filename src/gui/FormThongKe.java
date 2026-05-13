package gui;

import database.KetNoiCSDL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class FormThongKe extends JPanel implements ActionListener {
    JTextField txtTuNgay, txtDenNgay;
    JLabel lblTongDoanhThu;
    JButton btnThongKe;
    DefaultTableModel model;
    JTable bang;

    public FormThongKe() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        GUI();
    }

    public void GUI() {
        JPanel pForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        pForm.setBorder(BorderFactory.createTitledBorder("Thống kê doanh thu"));

        pForm.add(new JLabel("Từ ngày:"));
        txtTuNgay = new JTextField(LocalDate.now().withDayOfMonth(1).toString(), 10);
        pForm.add(txtTuNgay);

        pForm.add(new JLabel("Đến ngày:"));
        txtDenNgay = new JTextField(LocalDate.now().toString(), 10);
        pForm.add(txtDenNgay);

        lblTongDoanhThu = new JLabel("   Tổng doanh thu: 0 VNĐ");
        lblTongDoanhThu.setFont(new Font("Tahoma", Font.BOLD, 13));
        pForm.add(lblTongDoanhThu);

        add(pForm, BorderLayout.NORTH);

        String[] cot = {"ID HĐ", "Khách hàng", "Xe máy", "Ngày trả", "Tổng tiền"};
        model = new DefaultTableModel(cot, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bang = new JTable(model);
        add(new JScrollPane(bang), BorderLayout.CENTER);

        btnThongKe = new JButton("Thống kê");
        JPanel pNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        pNut.add(btnThongKe);
        add(pNut, BorderLayout.SOUTH);

        btnThongKe.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnThongKe) {
            thongKe();
        }
    }

    private void thongKe() {
        LocalDate tu, den;
        try {
            tu = LocalDate.parse(txtTuNgay.getText().trim());
            den = LocalDate.parse(txtDenNgay.getText().trim());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Ngày sai định dạng yyyy-MM-dd!");
            return;
        }
        if (den.isBefore(tu)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu!");
            return;
        }

        model.setRowCount(0);
        long tong = 0;
        String sql = "SELECT h.id, k.hoten AS ten_kh, " +
                "CONCAT(x.hangxe, ' ', x.model, ' (', x.bienso, ')') AS thong_tin_xe, " +
                "h.ngaytra_thucte, h.tongtien " +
                "FROM hopdong h " +
                "JOIN khachhang k ON h.makh = k.id " +
                "JOIN xemay x ON h.maxe = x.id " +
                "WHERE h.trangthai = 'DA_TRA' AND h.ngaytra_thucte BETWEEN ? AND ? " +
                "ORDER BY h.ngaytra_thucte";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(tu));
            ps.setDate(2, Date.valueOf(den));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long tien = rs.getLong("tongtien");
                    tong += tien;
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("ten_kh"),
                            rs.getString("thong_tin_xe"),
                            rs.getDate("ngaytra_thucte").toString(),
                            tien
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi thống kê: " + ex.getMessage());
            return;
        }

        lblTongDoanhThu.setText(String.format("   Tổng doanh thu: %,d VNĐ", tong));
    }
}
