package gui;

import bus.ThongKeBUS;
import dto.HopDongDTO;
import util.DateUtil;
import util.LoggerUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class FormThongKe extends JPanel implements ActionListener {
    private final ThongKeBUS bus = new ThongKeBUS();

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
        JPanel card = GiaoDien.taoCard();
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
        JPanel card = GiaoDien.taoCard();
        card.add(GiaoDien.taoCardHeader("CHI TIẾT HỢP ĐỒNG ĐÃ TRẢ"), BorderLayout.NORTH);

        String[] cot = {"ID", "Mã HĐ", "Khách hàng", "Xe máy", "Ngày trả", "Tổng tiền"};
        model = new DefaultTableModel(cot, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bang = new JTable(model);
        GiaoDien.styleBang(bang);
        GiaoDien.setRenderer(bang, 5, GiaoDien.rendererTien());

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
        try {
            LocalDate tu = DateUtil.parse(txtTuNgay.getText());
            LocalDate den = DateUtil.parse(txtDenNgay.getText());
            ThongKeBUS.KetQuaThongKe kq = bus.thongKeTheoNgay(tu, den);

            model.setRowCount(0);
            for (HopDongDTO h : kq.danhSach) {
                model.addRow(new Object[]{
                        h.getId(),
                        h.getMaSoHopDong(),
                        h.getTenKhachHang(),
                        h.getThongTinXe(),
                        h.getNgayTraThucTe() == null ? "" : h.getNgayTraThucTe(),
                        h.getTongTien()
                });
            }
            lblTongDoanhThu.setText(String.format("%,d đ", kq.tongDoanhThu));
            lblSoHopDong.setText(String.valueOf(kq.soHopDong));
            lblTrungBinh.setText(String.format("%,d đ", kq.trungBinhMoiHd));
            lblTraTre.setText(String.valueOf(kq.soHopDongTre));
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            LoggerUtil.error("Lỗi thống kê", ex);
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
}
