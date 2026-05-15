package gui;

import bus.XeMayBUS;
import constant.TrangThaiXe;
import dto.PageResult;
import dto.XeMayDTO;
import gui.common.PhanTrangPanel;
import util.LoggerUtil;
import util.ValidatorUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormXeMay extends JPanel implements ActionListener {
    private final XeMayBUS bus = new XeMayBUS();

    JTextField txtTimKiem;
    JComboBox<String> cboLocTrangThai;
    JButton btnThem, btnSua, btnXoa, btnTim;
    DefaultTableModel model;
    JTable bang;
    JLabel lblStatTong, lblStatSanSang, lblStatDangThue, lblStatBaoTri;
    PhanTrangPanel phanTrangPanel;

    public FormXeMay() {
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

        top.add(GiaoDien.taoPageHeader("Quản lý xe máy",
                "Danh sách xe máy có trong hệ thống"));
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
        lblStatSanSang = new JLabel("0");
        lblStatDangThue = new JLabel("0");
        lblStatBaoTri = new JLabel("0");

        p.add(GiaoDien.taoStatCard("TỔNG XE", lblStatTong, GiaoDien.XANH_CHINH, "🏍"));
        p.add(GiaoDien.taoStatCard("SẴN SÀNG", lblStatSanSang, GiaoDien.XANH_LA, "✓"));
        p.add(GiaoDien.taoStatCard("ĐANG THUÊ", lblStatDangThue, GiaoDien.CAM, "⏱"));
        p.add(GiaoDien.taoStatCard("BẢO TRÌ", lblStatBaoTri, GiaoDien.DO, "🔧"));
        return p;
    }

    private JPanel taoToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        JPanel pTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pTim.setOpaque(false);
        JLabel lblTim = new JLabel("🔍");
        lblTim.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        pTim.add(lblTim);
        txtTimKiem = new JTextField(20);
        GiaoDien.styleInput(txtTimKiem);
        pTim.add(txtTimKiem);
        btnTim = new JButton("Tìm");
        GiaoDien.styleNut(btnTim, GiaoDien.TIM);
        pTim.add(btnTim);

        pTim.add(new JLabel("  Trạng thái:"));
        cboLocTrangThai = new JComboBox<>(new String[]{"-- Tất cả --",
                TrangThaiXe.SAN_SANG, TrangThaiXe.DANG_THUE, TrangThaiXe.BAO_TRI});
        GiaoDien.styleInput(cboLocTrangThai);
        cboLocTrangThai.addActionListener(e -> taiTrang(1));
        pTim.add(cboLocTrangThai);

        toolbar.add(pTim, BorderLayout.WEST);

        JPanel pAct = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        pAct.setOpaque(false);
        btnThem = new JButton("+ Thêm xe");
        btnSua = new JButton("✎ Sửa");
        btnXoa = new JButton("✕ Xoá");
        GiaoDien.styleNut(btnThem, GiaoDien.XANH_LA);
        GiaoDien.styleNut(btnSua, GiaoDien.XANH_CHINH);
        GiaoDien.styleNut(btnXoa, GiaoDien.DO);
        pAct.add(btnThem);
        pAct.add(btnSua);
        pAct.add(btnXoa);
        toolbar.add(pAct, BorderLayout.EAST);

        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnTim.addActionListener(this);
        txtTimKiem.addActionListener(e -> taiTrang(1));
        return toolbar;
    }

    private JPanel taoTableCard() {
        JPanel card = GiaoDien.taoCard();
        card.add(GiaoDien.taoCardHeader("DANH SÁCH XE MÁY"), BorderLayout.NORTH);

        String[] cot = {"ID", "Biển số", "Hãng", "Model", "Năm SX", "Màu", "Giá thuê", "Trạng thái"};
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

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == btnThem) hienDialog(false);
            else if (e.getSource() == btnSua) hienDialog(true);
            else if (e.getSource() == btnXoa) xoa();
            else if (e.getSource() == btnTim) taiTrang(1);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            LoggerUtil.error("Lỗi FormXeMay", ex);
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void taiTrang(int trang) {
        try {
            String kw = txtTimKiem.getText().trim();
            int idx = cboLocTrangThai.getSelectedIndex();
            String tt = idx <= 0 ? null : (String) cboLocTrangThai.getSelectedItem();
            PageResult<XeMayDTO> kq = bus.phanTrang(trang, phanTrangPanel.getPageSize(),
                    kw, tt, "id", "ASC");

            model.setRowCount(0);
            for (XeMayDTO x : kq.getItems()) {
                model.addRow(new Object[]{
                        x.getId(), x.getBienSo(), x.getHangXe(), x.getModel(),
                        x.getNamSx(), x.getMauXe(), x.getGiaThue(), x.getTrangThai()
                });
            }
            phanTrangPanel.setTotal(kq.getTotalCount(), kq.getCurrentPage(), kq.getPageSize());
        } catch (Exception ex) {
            LoggerUtil.error("Lỗi tải xe", ex);
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void capNhatStats() {
        try {
            lblStatTong.setText(String.valueOf(bus.demTatCa()));
            lblStatSanSang.setText(String.valueOf(bus.demTheoTrangThai(TrangThaiXe.SAN_SANG)));
            lblStatDangThue.setText(String.valueOf(bus.demTheoTrangThai(TrangThaiXe.DANG_THUE)));
            lblStatBaoTri.setText(String.valueOf(bus.demTheoTrangThai(TrangThaiXe.BAO_TRI)));
        } catch (Exception ex) {
            LoggerUtil.error("Lỗi stats", ex);
        }
    }

    private void hienDialog(boolean laEdit) {
        Integer idEdit = null;
        XeMayDTO xeEdit = null;
        if (laEdit) {
            int row = bang.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Chọn dòng cần sửa!");
                return;
            }
            idEdit = (int) model.getValueAt(row, 0);
            xeEdit = bus.timTheoId(idEdit);
        }

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                laEdit ? "Sửa xe máy" : "Thêm xe máy",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());

        Color hc = laEdit ? GiaoDien.XANH_CHINH : GiaoDien.XANH_LA;
        dialog.add(taoDialogHeader(laEdit ? "Sửa thông tin xe" : "Thêm xe máy mới",
                laEdit ? "✎" : "+", hc), BorderLayout.NORTH);

        JTextField txtBs = new JTextField();
        JTextField txtHang = new JTextField();
        JTextField txtModel = new JTextField();
        JTextField txtNamSx = new JTextField();
        JTextField txtMau = new JTextField();
        JTextField txtGia = new JTextField();
        JComboBox<String> cboTt = new JComboBox<>(TrangThaiXe.danhSach());
        GiaoDien.styleInput(txtBs);
        GiaoDien.styleInput(txtHang);
        GiaoDien.styleInput(txtModel);
        GiaoDien.styleInput(txtNamSx);
        GiaoDien.styleInput(txtMau);
        GiaoDien.styleInput(txtGia);
        GiaoDien.styleInput(cboTt);

        if (laEdit && xeEdit != null) {
            txtBs.setText(xeEdit.getBienSo());
            txtHang.setText(xeEdit.getHangXe());
            txtModel.setText(xeEdit.getModel());
            if (xeEdit.getNamSx() != null) txtNamSx.setText(String.valueOf(xeEdit.getNamSx()));
            txtMau.setText(xeEdit.getMauXe());
            txtGia.setText(String.valueOf(xeEdit.getGiaThue()));
            cboTt.setSelectedItem(xeEdit.getTrangThai());
        }

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(GiaoDien.TRANG);
        form.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 6, 8, 6);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        addFormRow(form, g, 0, "Biển số *", txtBs);
        addFormRow(form, g, 1, "Hãng xe *", txtHang);
        addFormRow(form, g, 2, "Model *", txtModel);
        addFormRow(form, g, 3, "Năm SX", txtNamSx);
        addFormRow(form, g, 4, "Màu", txtMau);
        addFormRow(form, g, 5, "Giá thuê (VNĐ/ngày) *", txtGia);
        addFormRow(form, g, 6, "Trạng thái", cboTt);

        dialog.add(form, BorderLayout.CENTER);

        final Integer idF = idEdit;
        dialog.add(taoDialogFooter(dialog, laEdit, () -> {
            try {
                XeMayDTO x = new XeMayDTO();
                if (laEdit) x.setId(idF);
                x.setBienSo(txtBs.getText().trim());
                x.setHangXe(txtHang.getText().trim());
                x.setModel(txtModel.getText().trim());
                if (!txtNamSx.getText().trim().isEmpty()) {
                    x.setNamSx((int) ValidatorUtil.parseLong(txtNamSx.getText(), "Năm SX"));
                }
                x.setMauXe(txtMau.getText().trim());
                x.setGiaThue(ValidatorUtil.parseLong(txtGia.getText(), "Giá thuê"));
                x.setTrangThai((String) cboTt.getSelectedItem());

                if (laEdit) bus.sua(x);
                else bus.them(x);

                capNhatStats();
                taiTrang(phanTrangPanel.getCurrentPage());
                return true;
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Lỗi", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }), BorderLayout.SOUTH);

        dialog.setSize(520, 540);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void xoa() {
        int row = bang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn dòng cần xoá!");
            return;
        }
        String bs = model.getValueAt(row, 1).toString();
        int chon = JOptionPane.showConfirmDialog(this,
                "Xác nhận xoá xe \"" + bs + "\"?",
                "Xoá xe máy", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (chon != JOptionPane.YES_OPTION) return;

        int id = (int) model.getValueAt(row, 0);
        bus.xoa(id);
        capNhatStats();
        taiTrang(phanTrangPanel.getCurrentPage());
    }

    // === Helper static methods dùng chung cho các Form khác ===

    static void addFormRow(JPanel form, GridBagConstraints g, int row, String label, JComponent comp) {
        g.gridx = 0; g.gridy = row; g.weightx = 0;
        form.add(GiaoDien.taoLabel(label), g);
        g.gridx = 1; g.weightx = 1;
        form.add(comp, g);
    }

    static JPanel taoDialogHeader(String title, String icon, Color color) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(color);
        header.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        lblIcon.setForeground(Color.WHITE);
        JLabel lblTitle = new JLabel("  " + title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(lblIcon);
        left.add(lblTitle);
        header.add(left, BorderLayout.WEST);
        return header;
    }

    interface LuuAction { boolean luu(); }

    static JPanel taoDialogFooter(JDialog dialog, boolean laEdit, LuuAction action) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
        footer.setBackground(GiaoDien.XAM_NHAT);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, GiaoDien.VIEN));

        JButton btnHuy = new JButton("Huỷ");
        btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnHuy.setBackground(GiaoDien.TRANG);
        btnHuy.setForeground(GiaoDien.XAM_DAM);
        btnHuy.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GiaoDien.VIEN_INPUT),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)));
        btnHuy.setFocusPainted(false);
        btnHuy.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnLuu = new JButton(laEdit ? "✓ Lưu thay đổi" : "+ Thêm");
        GiaoDien.styleNut(btnLuu, laEdit ? GiaoDien.XANH_CHINH : GiaoDien.XANH_LA);

        footer.add(btnHuy);
        footer.add(btnLuu);

        btnHuy.addActionListener(e -> dialog.dispose());
        btnLuu.addActionListener(e -> {
            if (action.luu()) dialog.dispose();
        });
        dialog.getRootPane().setDefaultButton(btnLuu);
        return footer;
    }
}
