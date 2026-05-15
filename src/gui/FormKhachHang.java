package gui;

import bus.KhachHangBUS;
import dto.KhachHangDTO;
import dto.PageResult;
import gui.common.PhanTrangPanel;
import util.DateUtil;
import util.LoggerUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class FormKhachHang extends JPanel implements ActionListener {
    private final KhachHangBUS bus = new KhachHangBUS();

    JTextField txtTimKiem;
    JButton btnThem, btnSua, btnXoa, btnTim;
    DefaultTableModel model;
    JTable bang;
    JLabel lblStatTong, lblStatCoTk, lblStatChuaTk;
    PhanTrangPanel phanTrangPanel;

    public FormKhachHang() {
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

        top.add(GiaoDien.taoPageHeader("Quản lý khách hàng",
                "Danh sách khách hàng trong hệ thống"));
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
        JPanel p = new JPanel(new GridLayout(1, 3, 14, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblStatTong = new JLabel("0");
        lblStatCoTk = new JLabel("0");
        lblStatChuaTk = new JLabel("0");

        p.add(GiaoDien.taoStatCard("TỔNG KHÁCH HÀNG", lblStatTong, GiaoDien.XANH_CHINH, "👥"));
        p.add(GiaoDien.taoStatCard("CÓ TÀI KHOẢN", lblStatCoTk, GiaoDien.XANH_LA, "🔑"));
        p.add(GiaoDien.taoStatCard("CHƯA CÓ TK", lblStatChuaTk, GiaoDien.XAM, "👤"));
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
        txtTimKiem = new JTextField(22);
        GiaoDien.styleInput(txtTimKiem);
        pTim.add(txtTimKiem);
        btnTim = new JButton("Tìm");
        GiaoDien.styleNut(btnTim, GiaoDien.TIM);
        pTim.add(btnTim);
        toolbar.add(pTim, BorderLayout.WEST);

        JPanel pAct = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        pAct.setOpaque(false);
        btnThem = new JButton("+ Thêm khách hàng");
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
        card.add(GiaoDien.taoCardHeader("DANH SÁCH KHÁCH HÀNG"), BorderLayout.NORTH);

        String[] cot = {"ID", "CMND", "Họ tên", "SĐT", "Ngày sinh", "Giới tính", "Tên đăng nhập"};
        model = new DefaultTableModel(cot, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bang = new JTable(model);
        GiaoDien.styleBang(bang);

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
            LoggerUtil.error("Lỗi FormKhachHang", ex);
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void taiTrang(int trang) {
        try {
            String kw = txtTimKiem.getText().trim();
            PageResult<KhachHangDTO> kq = bus.phanTrang(trang, phanTrangPanel.getPageSize(),
                    kw, "id", "ASC");

            model.setRowCount(0);
            for (KhachHangDTO kh : kq.getItems()) {
                model.addRow(new Object[]{
                        kh.getId(), kh.getCmnd(), kh.getHoTen(), kh.getSdt(),
                        kh.getNgaySinh() == null ? "" : kh.getNgaySinh().toString(),
                        kh.getGioiTinh() == null ? "" : kh.getGioiTinh(),
                        kh.getTenDangNhap()
                });
            }
            phanTrangPanel.setTotal(kq.getTotalCount(), kq.getCurrentPage(), kq.getPageSize());
        } catch (Exception ex) {
            LoggerUtil.error("Lỗi tải KH", ex);
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void capNhatStats() {
        try {
            int tong = bus.demTatCa();
            int co = bus.demCoTaiKhoan();
            lblStatTong.setText(String.valueOf(tong));
            lblStatCoTk.setText(String.valueOf(co));
            lblStatChuaTk.setText(String.valueOf(tong - co));
        } catch (Exception ex) {
            LoggerUtil.error("Lỗi stats", ex);
        }
    }

    private void hienDialog(boolean laEdit) {
        Integer idEdit = null;
        KhachHangDTO khEdit = null;
        if (laEdit) {
            int row = bang.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Chọn dòng cần sửa!");
                return;
            }
            idEdit = (int) model.getValueAt(row, 0);
            khEdit = bus.timTheoId(idEdit);
        }

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                laEdit ? "Sửa khách hàng" : "Thêm khách hàng",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());

        Color hc = laEdit ? GiaoDien.XANH_CHINH : GiaoDien.XANH_LA;
        dialog.add(FormXeMay.taoDialogHeader(
                laEdit ? "Sửa thông tin khách hàng" : "Thêm khách hàng mới",
                laEdit ? "✎" : "+", hc), BorderLayout.NORTH);

        JTextField txtCmnd = new JTextField();
        JTextField txtHoTen = new JTextField();
        JTextField txtSdt = new JTextField();
        JTextField txtNgaySinh = new JTextField();
        JComboBox<String> cboGt = new JComboBox<>(new String[]{"", "NAM", "NU", "KHAC"});
        JTextField txtTdn = new JTextField();
        JTextField txtMk = new JTextField();
        for (JComponent c : new JComponent[]{txtCmnd, txtHoTen, txtSdt,
                txtNgaySinh, cboGt, txtTdn, txtMk}) {
            GiaoDien.styleInput(c);
        }

        if (laEdit && khEdit != null) {
            txtCmnd.setText(khEdit.getCmnd());
            txtHoTen.setText(khEdit.getHoTen());
            txtSdt.setText(khEdit.getSdt());
            if (khEdit.getNgaySinh() != null) txtNgaySinh.setText(khEdit.getNgaySinh().toString());
            cboGt.setSelectedItem(khEdit.getGioiTinh() == null ? "" : khEdit.getGioiTinh());
            txtTdn.setText(khEdit.getTenDangNhap());
        }

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(GiaoDien.TRANG);
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(7, 6, 7, 6);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        FormXeMay.addFormRow(form, g, 0, "CMND/CCCD *", txtCmnd);
        FormXeMay.addFormRow(form, g, 1, "Họ tên *", txtHoTen);
        FormXeMay.addFormRow(form, g, 2, "Số điện thoại", txtSdt);
        FormXeMay.addFormRow(form, g, 3, "Ngày sinh (yyyy-MM-dd)", txtNgaySinh);
        FormXeMay.addFormRow(form, g, 4, "Giới tính", cboGt);
        FormXeMay.addFormRow(form, g, 5, "Tên đăng nhập (tuỳ chọn)", txtTdn);
        FormXeMay.addFormRow(form, g, 6, laEdit ? "Mật khẩu mới (để trống nếu không đổi)" : "Mật khẩu", txtMk);

        dialog.add(new JScrollPane(form), BorderLayout.CENTER);

        final Integer idF = idEdit;
        dialog.add(FormXeMay.taoDialogFooter(dialog, laEdit, () -> {
            try {
                KhachHangDTO kh = new KhachHangDTO();
                if (laEdit) kh.setId(idF);
                kh.setCmnd(txtCmnd.getText().trim());
                kh.setHoTen(txtHoTen.getText().trim());
                kh.setSdt(txtSdt.getText().trim());
                if (!txtNgaySinh.getText().trim().isEmpty()) {
                    LocalDate ns = DateUtil.parse(txtNgaySinh.getText());
                    kh.setNgaySinh(ns);
                }
                String gt = (String) cboGt.getSelectedItem();
                kh.setGioiTinh(gt == null || gt.isEmpty() ? null : gt);
                String tdn = txtTdn.getText().trim();
                kh.setTenDangNhap(tdn.isEmpty() ? null : tdn);
                String mk = txtMk.getText().trim();

                if (laEdit) bus.sua(kh, mk);
                else bus.them(kh, mk);

                capNhatStats();
                taiTrang(phanTrangPanel.getCurrentPage());
                return true;
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Lỗi", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }), BorderLayout.SOUTH);

        dialog.setSize(560, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void xoa() {
        int row = bang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn dòng cần xoá!");
            return;
        }
        String tenKh = model.getValueAt(row, 2).toString();
        int chon = JOptionPane.showConfirmDialog(this,
                "Xác nhận xoá khách hàng \"" + tenKh + "\"?",
                "Xoá khách hàng", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (chon != JOptionPane.YES_OPTION) return;

        int id = (int) model.getValueAt(row, 0);
        bus.xoa(id);
        capNhatStats();
        taiTrang(phanTrangPanel.getCurrentPage());
    }
}
