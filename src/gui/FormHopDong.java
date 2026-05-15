package gui;

import bus.HopDongBUS;
import bus.KhachHangBUS;
import bus.XeMayBUS;
import constant.TrangThaiHopDong;
import dto.HopDongDTO;
import dto.KhachHangDTO;
import dto.NhanVienDTO;
import dto.PageResult;
import dto.XeMayDTO;
import gui.common.PhanTrangPanel;
import util.DateUtil;
import util.LoggerUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class FormHopDong extends JPanel implements ActionListener {
    private final HopDongBUS hopDongBUS = new HopDongBUS();
    private final XeMayBUS xeMayBUS = new XeMayBUS();
    private final KhachHangBUS khachHangBUS = new KhachHangBUS();

    NhanVienDTO nhanVien;
    JButton btnTaoHopDong, btnTraXe;
    JComboBox<String> cboLocTrangThai;
    DefaultTableModel model;
    JTable bang;
    JLabel lblStatTong, lblStatDangThue, lblStatDaTra, lblStatDoanhThu;
    PhanTrangPanel phanTrangPanel;

    public FormHopDong(NhanVienDTO nv) {
        this.nhanVien = nv;
        setLayout(new BorderLayout());
        setBackground(GiaoDien.XAM_NHAT);
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GUI();
        capNhatStats();
        taiTrang(1);
    }

    public void GUI() {
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        top.add(GiaoDien.taoPageHeader("Quản lý hợp đồng",
                "Tạo hợp đồng thuê xe và xử lý trả xe"));
        top.add(Box.createVerticalStrut(16));
        top.add(taoStatsRow());
        top.add(Box.createVerticalStrut(16));
        top.add(taoToolbar());
        top.add(Box.createVerticalStrut(10));

        add(top, BorderLayout.NORTH);
        add(taoTableCard(), BorderLayout.CENTER);

        phanTrangPanel = new PhanTrangPanel();
        phanTrangPanel.setOnChange((p, s) -> taiTrang(p));
        add(phanTrangPanel, BorderLayout.SOUTH);
    }

    private JPanel taoStatsRow() {
        JPanel p = new JPanel(new GridLayout(1, 4, 14, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblStatTong = new JLabel("0");
        lblStatDangThue = new JLabel("0");
        lblStatDaTra = new JLabel("0");
        lblStatDoanhThu = new JLabel("0 đ");

        p.add(GiaoDien.taoStatCard("TỔNG HỢP ĐỒNG", lblStatTong, GiaoDien.XANH_CHINH, "📄"));
        p.add(GiaoDien.taoStatCard("ĐANG THUÊ", lblStatDangThue, GiaoDien.CAM, "⏱"));
        p.add(GiaoDien.taoStatCard("ĐÃ TRẢ", lblStatDaTra, GiaoDien.XAM, "✓"));
        p.add(GiaoDien.taoStatCard("TỔNG DOANH THU", lblStatDoanhThu, GiaoDien.XANH_LA, "💰"));
        return p;
    }

    private JPanel taoToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        left.setOpaque(false);
        left.add(new JLabel("Lọc trạng thái:"));
        cboLocTrangThai = new JComboBox<>(new String[]{"-- Tất cả --",
                TrangThaiHopDong.DANG_THUE, TrangThaiHopDong.DA_TRA, TrangThaiHopDong.HUY});
        GiaoDien.styleInput(cboLocTrangThai);
        cboLocTrangThai.addActionListener(e -> taiTrang(1));
        left.add(cboLocTrangThai);
        toolbar.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.setOpaque(false);
        btnTaoHopDong = new JButton("+ Tạo hợp đồng");
        btnTraXe = new JButton("✓ Trả xe");
        GiaoDien.styleNut(btnTaoHopDong, GiaoDien.XANH_LA);
        GiaoDien.styleNut(btnTraXe, GiaoDien.CAM);
        right.add(btnTaoHopDong);
        right.add(btnTraXe);
        toolbar.add(right, BorderLayout.EAST);

        btnTaoHopDong.addActionListener(this);
        btnTraXe.addActionListener(this);
        return toolbar;
    }

    private JPanel taoTableCard() {
        JPanel card = GiaoDien.taoCard();
        card.add(GiaoDien.taoCardHeader("DANH SÁCH HỢP ĐỒNG"), BorderLayout.NORTH);

        String[] cot = {"ID", "Mã HĐ", "Khách hàng", "Xe máy", "Ngày thuê",
                "Trả dự kiến", "Trả thực tế", "Tổng tiền", "Trạng thái"};
        model = new DefaultTableModel(cot, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bang = new JTable(model);
        GiaoDien.styleBang(bang);
        GiaoDien.setRenderer(bang, 7, GiaoDien.rendererTien());
        GiaoDien.setRenderer(bang, 8, GiaoDien.rendererTrangThai());

        JScrollPane scroll = new JScrollPane(bang);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(GiaoDien.TRANG);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == btnTaoHopDong) hienDialogTaoHopDong();
            else if (e.getSource() == btnTraXe) traXe();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            LoggerUtil.error("Lỗi FormHopDong", ex);
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void taiTrang(int trang) {
        try {
            int idx = cboLocTrangThai.getSelectedIndex();
            String trangThai = idx <= 0 ? null : (String) cboLocTrangThai.getSelectedItem();
            PageResult<HopDongDTO> kq = hopDongBUS.phanTrang(trang,
                    phanTrangPanel.getPageSize(), trangThai, null, null, null, "id", "ASC");

            model.setRowCount(0);
            for (HopDongDTO hd : kq.getItems()) {
                model.addRow(new Object[]{
                        hd.getId(),
                        hd.getMaSoHopDong(),
                        hd.getTenKhachHang(),
                        hd.getThongTinXe(),
                        hd.getNgayThue(),
                        hd.getNgayTraDuKien(),
                        hd.getNgayTraThucTe() == null ? "" : hd.getNgayTraThucTe(),
                        hd.getTongTien(),
                        hd.getTrangThai()
                });
            }
            phanTrangPanel.setTotal(kq.getTotalCount(), kq.getCurrentPage(), kq.getPageSize());
        } catch (Exception ex) {
            LoggerUtil.error("Lỗi tải hợp đồng", ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void capNhatStats() {
        try {
            lblStatTong.setText(String.valueOf(hopDongBUS.demTatCa()));
            lblStatDangThue.setText(String.valueOf(hopDongBUS.demTheoTrangThai(TrangThaiHopDong.DANG_THUE)));
            lblStatDaTra.setText(String.valueOf(hopDongBUS.demTheoTrangThai(TrangThaiHopDong.DA_TRA)));
            lblStatDoanhThu.setText(String.format("%,d đ", hopDongBUS.tongDoanhThu()));
        } catch (Exception ex) {
            LoggerUtil.error("Lỗi stats", ex);
        }
    }

    private void hienDialogTaoHopDong() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                "Tạo hợp đồng thuê xe", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());

        dialog.add(FormXeMay.taoDialogHeader("Tạo hợp đồng thuê xe", "+", GiaoDien.XANH_LA),
                BorderLayout.NORTH);

        JComboBox<KhachHangDTO> cboKh = new JComboBox<>();
        JComboBox<XeMayDTO> cboXe = new JComboBox<>();
        JTextField txtNgayThue = new JTextField(LocalDate.now().toString());
        JTextField txtNgayTra = new JTextField(LocalDate.now().plusDays(3).toString());
        JTextField txtTienCoc = new JTextField("0");
        GiaoDien.styleInput(cboKh);
        GiaoDien.styleInput(cboXe);
        GiaoDien.styleInput(txtNgayThue);
        GiaoDien.styleInput(txtNgayTra);
        GiaoDien.styleInput(txtTienCoc);

        for (KhachHangDTO kh : khachHangBUS.layTatCa()) cboKh.addItem(kh);
        for (XeMayDTO xe : xeMayBUS.layXeSanSang()) cboXe.addItem(xe);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(GiaoDien.TRANG);
        form.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 6, 8, 6);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        FormXeMay.addFormRow(form, g, 0, "Khách hàng *", cboKh);
        FormXeMay.addFormRow(form, g, 1, "Xe máy *", cboXe);
        FormXeMay.addFormRow(form, g, 2, "Ngày thuê (yyyy-MM-dd) *", txtNgayThue);
        FormXeMay.addFormRow(form, g, 3, "Ngày trả dự kiến *", txtNgayTra);
        FormXeMay.addFormRow(form, g, 4, "Tiền đặt cọc", txtTienCoc);

        dialog.add(form, BorderLayout.CENTER);

        dialog.add(FormXeMay.taoDialogFooter(dialog, false, () -> {
            try {
                KhachHangDTO kh = (KhachHangDTO) cboKh.getSelectedItem();
                XeMayDTO xe = (XeMayDTO) cboXe.getSelectedItem();
                LocalDate nT = DateUtil.parse(txtNgayThue.getText());
                LocalDate nTr = DateUtil.parse(txtNgayTra.getText());
                long coc = Long.parseLong(txtTienCoc.getText().trim().replaceAll("[,.\\s]", ""));

                hopDongBUS.taoHopDong(
                        kh == null ? 0 : kh.getId(),
                        xe == null ? 0 : xe.getId(),
                        nhanVien.getId(), nT, nTr, coc);

                JOptionPane.showMessageDialog(dialog, "Tạo hợp đồng thành công!");
                capNhatStats();
                taiTrang(phanTrangPanel.getCurrentPage());
                return true;
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Lỗi", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }), BorderLayout.SOUTH);

        dialog.setSize(560, 460);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void traXe() {
        int row = bang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn hợp đồng cần trả xe!");
            return;
        }
        if (!TrangThaiHopDong.DANG_THUE.equals(model.getValueAt(row, 8))) {
            JOptionPane.showMessageDialog(this, "Hợp đồng này không ở trạng thái đang thuê!");
            return;
        }
        int hopDongId = (int) model.getValueAt(row, 0);

        String nhap = JOptionPane.showInputDialog(this,
                "Nhập ngày trả thực tế (yyyy-MM-dd):", LocalDate.now().toString());
        if (nhap == null) return;

        LocalDate ngayTra = DateUtil.parse(nhap);
        long tong = hopDongBUS.traXe(hopDongId, ngayTra);
        JOptionPane.showMessageDialog(this,
                String.format("Đã trả xe.\nTổng tiền: %,d VNĐ", tong));
        capNhatStats();
        taiTrang(phanTrangPanel.getCurrentPage());
    }
}
