package gui;

import bus.HopDongBUS;
import constant.TrangThaiHopDong;
import dto.HopDongDTO;
import dto.KhachHangDTO;
import util.LoggerUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormHopDongCuaToi extends JPanel {
    private final HopDongBUS hopDongBUS = new HopDongBUS();
    KhachHangDTO khachHang;
    JLabel lblStatTong, lblStatDangThue, lblStatDaTra, lblStatTongChi;
    DefaultTableModel model;
    JTable bang;

    public FormHopDongCuaToi(KhachHangDTO kh) {
        this.khachHang = kh;
        setLayout(new BorderLayout());
        setBackground(GiaoDien.XAM_NHAT);
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GUI();
        napDuLieu();
    }

    public void GUI() {
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        top.add(GiaoDien.taoPageHeader("Hợp đồng của tôi",
                "Danh sách các hợp đồng thuê xe của bạn"));
        top.add(Box.createVerticalStrut(16));
        top.add(taoStatsRow());
        top.add(Box.createVerticalStrut(16));

        add(top, BorderLayout.NORTH);
        add(taoTableCard(), BorderLayout.CENTER);
    }

    private JPanel taoStatsRow() {
        JPanel p = new JPanel(new GridLayout(1, 4, 14, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblStatTong = new JLabel("0");
        lblStatDangThue = new JLabel("0");
        lblStatDaTra = new JLabel("0");
        lblStatTongChi = new JLabel("0 đ");

        p.add(GiaoDien.taoStatCard("TỔNG HỢP ĐỒNG", lblStatTong, GiaoDien.XANH_CHINH, "📄"));
        p.add(GiaoDien.taoStatCard("ĐANG THUÊ", lblStatDangThue, GiaoDien.CAM, "⏱"));
        p.add(GiaoDien.taoStatCard("ĐÃ TRẢ", lblStatDaTra, GiaoDien.XAM, "✓"));
        p.add(GiaoDien.taoStatCard("TỔNG ĐÃ CHI", lblStatTongChi, GiaoDien.XANH_LA, "💰"));
        return p;
    }

    private JPanel taoTableCard() {
        JPanel card = GiaoDien.taoCard();
        card.add(GiaoDien.taoCardHeader("CHI TIẾT HỢP ĐỒNG"), BorderLayout.NORTH);

        String[] cot = {"ID", "Mã HĐ", "Xe máy", "Ngày thuê", "Trả dự kiến",
                "Trả thực tế", "Tổng tiền", "Trạng thái"};
        model = new DefaultTableModel(cot, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bang = new JTable(model);
        GiaoDien.styleBang(bang);
        GiaoDien.setRenderer(bang, 6, GiaoDien.rendererTien());
        GiaoDien.setRenderer(bang, 7, GiaoDien.rendererTrangThai());

        JScrollPane scroll = new JScrollPane(bang);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(GiaoDien.TRANG);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private void napDuLieu() {
        model.setRowCount(0);
        int tong = 0, dt = 0, da = 0;
        long tongChi = 0;
        try {
            List<HopDongDTO> list = hopDongBUS.hopDongCuaKhach(khachHang.getId());
            for (HopDongDTO hd : list) {
                tong++;
                if (TrangThaiHopDong.DANG_THUE.equals(hd.getTrangThai())) dt++;
                else if (TrangThaiHopDong.DA_TRA.equals(hd.getTrangThai())) {
                    da++;
                    tongChi += hd.getTongTien();
                }
                model.addRow(new Object[]{
                        hd.getId(),
                        hd.getMaSoHopDong(),
                        hd.getThongTinXe(),
                        hd.getNgayThue(),
                        hd.getNgayTraDuKien(),
                        hd.getNgayTraThucTe() == null ? "" : hd.getNgayTraThucTe(),
                        hd.getTongTien(),
                        hd.getTrangThai()
                });
            }
        } catch (Exception ex) {
            LoggerUtil.error("Lỗi tải hợp đồng của khách", ex);
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
        lblStatTong.setText(String.valueOf(tong));
        lblStatDangThue.setText(String.valueOf(dt));
        lblStatDaTra.setText(String.valueOf(da));
        lblStatTongChi.setText(String.format("%,d đ", tongChi));
    }
}
