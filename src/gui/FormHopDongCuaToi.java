package gui;

import database.KetNoiCSDL;
import database.KhachHang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FormHopDongCuaToi extends JPanel implements ActionListener {
    KhachHang khachHang;
    JButton btnLamMoi;
    JLabel lblTongChiTieu;
    DefaultTableModel model;
    JTable bang;

    public FormHopDongCuaToi(KhachHang kh) {
        this.khachHang = kh;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        GUI();
        napDuLieu();
    }

    public void GUI() {
        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        pTop.setBorder(BorderFactory.createTitledBorder("Tổng kết"));
        lblTongChiTieu = new JLabel("Tổng đã chi: 0 VNĐ");
        lblTongChiTieu.setFont(new Font("Tahoma", Font.BOLD, 13));
        pTop.add(lblTongChiTieu);
        add(pTop, BorderLayout.NORTH);

        String[] cot = {"ID", "Xe máy", "Ngày thuê", "Trả dự kiến",
                "Trả thực tế", "Tổng tiền", "Trạng thái"};
        model = new DefaultTableModel(cot, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bang = new JTable(model);
        JScrollPane scroll = new JScrollPane(bang);
        scroll.setBorder(BorderFactory.createTitledBorder("Hợp đồng của bạn"));
        add(scroll, BorderLayout.CENTER);

        btnLamMoi = new JButton("Làm mới");
        JPanel pNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        pNut.add(btnLamMoi);
        add(pNut, BorderLayout.SOUTH);

        btnLamMoi.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLamMoi) napDuLieu();
    }

    private void napDuLieu() {
        model.setRowCount(0);
        long tong = 0;
        String sql = "SELECT h.id, " +
                "CONCAT(x.hangxe, ' ', x.model, ' (', x.bienso, ')') AS thong_tin_xe, " +
                "h.ngaythue, h.ngaytra_dukien, h.ngaytra_thucte, h.tongtien, h.trangthai " +
                "FROM hopdong h JOIN xemay x ON h.maxe = x.id " +
                "WHERE h.makh = ? ORDER BY h.id DESC";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, khachHang.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date traTt = rs.getDate("ngaytra_thucte");
                    long tien = rs.getLong("tongtien");
                    tong += tien;
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("thong_tin_xe"),
                            rs.getDate("ngaythue").toString(),
                            rs.getDate("ngaytra_dukien").toString(),
                            traTt == null ? "" : traTt.toString(),
                            tien,
                            rs.getString("trangthai")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải hợp đồng: " + ex.getMessage());
        }
        lblTongChiTieu.setText(String.format("Tổng đã chi: %,d VNĐ", tong));
    }
}
