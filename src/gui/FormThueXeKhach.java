package gui;

import database.KetNoiCSDL;
import database.KhachHang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class FormThueXeKhach extends JPanel implements ActionListener {
    KhachHang khachHang;
    JTextField txtNgayThue, txtNgayTraDuKien;
    JLabel lblXeChon, lblTamTinh;
    JButton btnDatThue, btnLamMoi;
    DefaultTableModel model;
    JTable bang;

    long giaThueXeChon = 0;
    int idXeChon = -1;

    public FormThueXeKhach(KhachHang kh) {
        this.khachHang = kh;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        GUI();
        napDuLieu();
    }

    public void GUI() {
        String[] cot = {"ID", "Biển số", "Hãng", "Model", "Giá thuê/ngày"};
        model = new DefaultTableModel(cot, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bang = new JTable(model);
        bang.getSelectionModel().addListSelectionListener(e -> chonXe());
        JScrollPane scroll = new JScrollPane(bang);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách xe đang sẵn sàng"));
        add(scroll, BorderLayout.CENTER);

        JPanel pForm = new JPanel(new GridLayout(4, 2, 8, 6));
        pForm.setBorder(BorderFactory.createTitledBorder("Thông tin đặt thuê"));

        pForm.add(new JLabel("Xe đã chọn:"));
        lblXeChon = new JLabel("(Chưa chọn)");
        pForm.add(lblXeChon);

        pForm.add(new JLabel("Ngày thuê (yyyy-MM-dd):"));
        txtNgayThue = new JTextField(LocalDate.now().toString());
        pForm.add(txtNgayThue);

        pForm.add(new JLabel("Ngày trả dự kiến:"));
        txtNgayTraDuKien = new JTextField(LocalDate.now().plusDays(3).toString());
        pForm.add(txtNgayTraDuKien);

        pForm.add(new JLabel("Tạm tính:"));
        lblTamTinh = new JLabel("0 VNĐ");
        lblTamTinh.setFont(new Font("Tahoma", Font.BOLD, 12));
        pForm.add(lblTamTinh);

        add(pForm, BorderLayout.NORTH);

        btnDatThue = new JButton("Đặt thuê");
        btnLamMoi = new JButton("Làm mới");

        JPanel pNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        pNut.add(btnDatThue);
        pNut.add(btnLamMoi);
        add(pNut, BorderLayout.SOUTH);

        btnDatThue.addActionListener(this);
        btnLamMoi.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnDatThue) datThue();
        else if (e.getSource() == btnLamMoi) {
            xoaForm();
            napDuLieu();
        }
    }

    private void napDuLieu() {
        model.setRowCount(0);
        String sql = "SELECT * FROM xemay WHERE trangthai='SAN_SANG' ORDER BY id";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("bienso"),
                        rs.getString("hangxe"),
                        rs.getString("model"),
                        rs.getLong("giathue")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải xe: " + ex.getMessage());
        }
    }

    private void chonXe() {
        int row = bang.getSelectedRow();
        if (row < 0) return;
        idXeChon = (int) model.getValueAt(row, 0);
        giaThueXeChon = (long) model.getValueAt(row, 4);
        lblXeChon.setText(model.getValueAt(row, 1) + " - "
                + model.getValueAt(row, 2) + " " + model.getValueAt(row, 3));
        capNhatTamTinh();
    }

    private void capNhatTamTinh() {
        if (idXeChon < 0) { lblTamTinh.setText("0 VNĐ"); return; }
        try {
            LocalDate nT = LocalDate.parse(txtNgayThue.getText().trim());
            LocalDate nTra = LocalDate.parse(txtNgayTraDuKien.getText().trim());
            long soNgay = ChronoUnit.DAYS.between(nT, nTra);
            if (soNgay <= 0) { lblTamTinh.setText("0 VNĐ"); return; }
            lblTamTinh.setText(String.format("%,d VNĐ (%d ngày)", soNgay * giaThueXeChon, soNgay));
        } catch (Exception ex) {
            lblTamTinh.setText("0 VNĐ");
        }
    }

    private void datThue() {
        if (idXeChon < 0) {
            JOptionPane.showMessageDialog(this, "Chọn 1 xe trong danh sách!");
            return;
        }
        LocalDate nThue, nTra;
        try {
            nThue = LocalDate.parse(txtNgayThue.getText().trim());
            nTra = LocalDate.parse(txtNgayTraDuKien.getText().trim());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Ngày sai định dạng yyyy-MM-dd!");
            return;
        }
        if (!nTra.isAfter(nThue)) {
            JOptionPane.showMessageDialog(this, "Ngày trả phải sau ngày thuê!");
            return;
        }

        long soNgay = ChronoUnit.DAYS.between(nThue, nTra);
        long tamTinh = soNgay * giaThueXeChon;
        int ok = JOptionPane.showConfirmDialog(this,
                String.format("Xác nhận đặt thuê?\nTạm tính: %,d VNĐ (%d ngày)\n" +
                        "Vui lòng đến cửa hàng nhận xe và thanh toán.", tamTinh, soNgay),
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;

        String sqlHd = "INSERT INTO hopdong(makh, maxe, ngaythue, ngaytra_dukien, trangthai) " +
                "VALUES (?,?,?,?, 'DANG_THUE')";
        String sqlXe = "UPDATE xemay SET trangthai='DANG_THUE' WHERE id=?";

        try (Connection con = KetNoiCSDL.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps1 = con.prepareStatement(sqlHd);
                 PreparedStatement ps2 = con.prepareStatement(sqlXe)) {
                ps1.setInt(1, khachHang.getId());
                ps1.setInt(2, idXeChon);
                ps1.setDate(3, Date.valueOf(nThue));
                ps1.setDate(4, Date.valueOf(nTra));
                ps1.executeUpdate();

                ps2.setInt(1, idXeChon);
                ps2.executeUpdate();

                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
            JOptionPane.showMessageDialog(this, "Đặt thuê thành công! Mời bạn đến cửa hàng nhận xe.");
            xoaForm();
            napDuLieu();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi đặt thuê: " + ex.getMessage());
        }
    }

    private void xoaForm() {
        idXeChon = -1;
        giaThueXeChon = 0;
        lblXeChon.setText("(Chưa chọn)");
        lblTamTinh.setText("0 VNĐ");
        bang.clearSelection();
    }
}
