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
    JButton btnThongKe;
    JLabel lblTongDoanhThu, lblSoHopDong, lblTrungBinh, lblTraTre;
    DefaultTableModel model;
    JTable bang;

    public FormThongKe() {
        setLayout(new BorderLayout());
        setBackground(GiaoDien.XAM_NHAT);
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GUI();
        thongKe();
    }

    public void GUI() {
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        top.add(GiaoDien.taoPageHeader("Thống kê doanh thu",
                "Phân tích doanh thu cho thuê xe máy theo khoảng thời gian"));
        top.add(Box.createVerticalStrut(16));
        top.add(taoFilterCard());
        top.add(Box.createVerticalStrut(16));
        top.add(taoStatsRow());
        top.add(Box.createVerticalStrut(16));

        add(top, BorderLayout.NORTH);
        add(taoTableCard(), BorderLayout.CENTER);
    }

    private JPanel taoFilterCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(GiaoDien.TRANG);
        card.setBorder(BorderFactory.createLineBorder(GiaoDien.VIEN));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        card.add(GiaoDien.taoCardHeader("KHOẢNG THỜI GIAN"), BorderLayout.NORTH);

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(GiaoDien.TRANG);
        body.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(0, 8, 0, 8);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.weightx = 0;
        body.add(GiaoDien.taoLabel("Từ ngày:"), g);
        g.gridx = 1; g.weightx = 1;
        txtTuNgay = new JTextField(LocalDate.now().withDayOfMonth(1).toString());
        GiaoDien.styleInput(txtTuNgay);
        body.add(txtTuNgay, g);

        g.gridx = 2; g.weightx = 0;
        body.add(GiaoDien.taoLabel("Đến ngày:"), g);
        g.gridx = 3; g.weightx = 1;
        txtDenNgay = new JTextField(LocalDate.now().toString());
        GiaoDien.styleInput(txtDenNgay);
        body.add(txtDenNgay, g);

        g.gridx = 4; g.weightx = 0;
        btnThongKe = new JButton("📊  Thống kê");
        GiaoDien.styleNut(btnThongKe, GiaoDien.XANH_CHINH);
        btnThongKe.addActionListener(this);
        body.add(btnThongKe, g);

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel taoStatsRow() {
        JPanel p = new JPanel(new GridLayout(1, 4, 14, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblTongDoanhThu = new JLabel("0 đ");
        lblSoHopDong = new JLabel("0");
        lblTrungBinh = new JLabel("0 đ");
        lblTraTre = new JLabel("0");

        p.add(GiaoDien.taoStatCard("TỔNG DOANH THU", lblTongDoanhThu, GiaoDien.XANH_LA, "💰"));
        p.add(GiaoDien.taoStatCard("SỐ HỢP ĐỒNG", lblSoHopDong, GiaoDien.XANH_CHINH, "📄"));
        p.add(GiaoDien.taoStatCard("TRUNG BÌNH / HĐ", lblTrungBinh, GiaoDien.TIM, "📈"));
        p.add(GiaoDien.taoStatCard("HỢP ĐỒNG TRỄ HẠN", lblTraTre, GiaoDien.CAM, "⚠"));
        return p;
    }

    private JPanel taoTableCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(GiaoDien.TRANG);
        card.setBorder(BorderFactory.createLineBorder(GiaoDien.VIEN));
        card.add(GiaoDien.taoCardHeader("CHI TIẾT HỢP ĐỒNG ĐÃ TRẢ"), BorderLayout.NORTH);

        String[] cot = {"ID HĐ", "Khách hàng", "Xe máy", "Ngày trả", "Tổng tiền"};
        model = new DefaultTableModel(cot, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bang = new JTable(model);
        GiaoDien.styleBang(bang);
        GiaoDien.setRenderer(bang, 4, GiaoDien.rendererTien());

        JScrollPane scroll = new JScrollPane(bang);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(GiaoDien.TRANG);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnThongKe) thongKe();
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
        int soHd = 0, soTre = 0;
        String sql = "SELECT h.id, k.hoten AS ten_kh, " +
                "CONCAT(x.hangxe, ' ', x.model, ' (', x.bienso, ')') AS thong_tin_xe, " +
                "h.ngaythue, h.ngaytra_dukien, h.ngaytra_thucte, h.tongtien " +
                "FROM hopdong h " +
                "JOIN khachhang k ON h.makh = k.id " +
                "JOIN xemay x ON h.maxe = x.id " +
                "WHERE h.trangthai = 'DA_TRA' AND h.ngaytra_thucte BETWEEN ? AND ? " +
                "ORDER BY h.ngaytra_thucte DESC";
        try (Connection con = KetNoiCSDL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(tu));
            ps.setDate(2, Date.valueOf(den));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long tien = rs.getLong("tongtien");
                    tong += tien;
                    soHd++;
                    Date traTt = rs.getDate("ngaytra_thucte");
                    Date traDk = rs.getDate("ngaytra_dukien");
                    if (traTt != null && traDk != null && traTt.after(traDk)) soTre++;
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("ten_kh"),
                            rs.getString("thong_tin_xe"),
                            traTt == null ? "" : traTt.toString(),
                            tien
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi thống kê: " + ex.getMessage());
            return;
        }

        long tb = soHd == 0 ? 0 : tong / soHd;
        lblTongDoanhThu.setText(String.format("%,d đ", tong));
        lblSoHopDong.setText(String.valueOf(soHd));
        lblTrungBinh.setText(String.format("%,d đ", tb));
        lblTraTre.setText(String.valueOf(soTre));
    }
}
