package gui;

import bus.HopDongBUS;
import bus.XeMayBUS;
import dto.KhachHangDTO;
import dto.XeMayDTO;
import util.DateUtil;
import util.LoggerUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FormThueXeKhach extends JPanel implements ActionListener {
    private final HopDongBUS hopDongBUS = new HopDongBUS();
    private final XeMayBUS xeMayBUS = new XeMayBUS();
    KhachHangDTO khachHang;
    JButton btnDatThue;
    DefaultTableModel model;
    JTable bang;

    public FormThueXeKhach(KhachHangDTO kh) {
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

        top.add(GiaoDien.taoPageHeader("Đặt thuê xe máy",
                "Chọn xe trong danh sách rồi bấm \"Đặt thuê\""));
        top.add(Box.createVerticalStrut(16));
        top.add(taoToolbar());
        top.add(Box.createVerticalStrut(10));

        add(top, BorderLayout.NORTH);
        add(taoTableCard(), BorderLayout.CENTER);
    }

    private JPanel taoToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        toolbar.setOpaque(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        btnDatThue = new JButton("✓ Đặt thuê");
        GiaoDien.styleNut(btnDatThue, GiaoDien.XANH_LA);
        toolbar.add(btnDatThue);

        btnDatThue.addActionListener(this);
        return toolbar;
    }

    private JPanel taoTableCard() {
        JPanel card = GiaoDien.taoCard();
        card.add(GiaoDien.taoCardHeader("DANH SÁCH XE ĐANG SẴN SÀNG"), BorderLayout.NORTH);

        String[] cot = {"ID", "Biển số", "Hãng", "Model", "Giá thuê/ngày"};
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
        if (e.getSource() == btnDatThue) hienDialogDatThue();
    }

    private void napDuLieu() {
        model.setRowCount(0);
        try {
            for (XeMayDTO x : xeMayBUS.layXeSanSang()) {
                model.addRow(new Object[]{
                        x.getId(), x.getBienSo(), x.getHangXe(), x.getModel(), x.getGiaThue()
                });
            }
        } catch (Exception ex) {
            LoggerUtil.error("Lỗi tải xe", ex);
            JOptionPane.showMessageDialog(this, "Lỗi tải xe: " + ex.getMessage());
        }
    }

    private void hienDialogDatThue() {
        int row = bang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn 1 xe trong danh sách trước!");
            return;
        }
        int idXe = (int) model.getValueAt(row, 0);
        String thongTinXe = model.getValueAt(row, 1) + " - "
                + model.getValueAt(row, 2) + " " + model.getValueAt(row, 3);
        long giaThue = (long) model.getValueAt(row, 4);

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                "Đặt thuê xe", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());

        dialog.add(FormXeMay.taoDialogHeader("Đặt thuê xe máy", "✓", GiaoDien.XANH_LA),
                BorderLayout.NORTH);

        JLabel lblXe = new JLabel(thongTinXe);
        lblXe.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblXe.setForeground(GiaoDien.XANH_CHINH);

        JLabel lblGia = new JLabel(String.format("%,d đ / ngày", giaThue));
        lblGia.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblGia.setForeground(GiaoDien.CHU_CHINH);

        JTextField txtNgayThue = new JTextField(LocalDate.now().toString());
        JTextField txtNgayTra = new JTextField(LocalDate.now().plusDays(3).toString());
        GiaoDien.styleInput(txtNgayThue);
        GiaoDien.styleInput(txtNgayTra);

        JLabel lblTamTinh = new JLabel("0 đ");
        lblTamTinh.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTamTinh.setForeground(GiaoDien.XANH_LA);

        Runnable capNhatTamTinh = () -> {
            try {
                LocalDate nT = LocalDate.parse(txtNgayThue.getText().trim());
                LocalDate nTr = LocalDate.parse(txtNgayTra.getText().trim());
                long ngay = ChronoUnit.DAYS.between(nT, nTr);
                if (ngay <= 0) { lblTamTinh.setText("0 đ"); return; }
                lblTamTinh.setText(String.format("%,d đ  (%d ngày)", ngay * giaThue, ngay));
            } catch (Exception ex) {
                lblTamTinh.setText("0 đ");
            }
        };
        txtNgayThue.addActionListener(e -> capNhatTamTinh.run());
        txtNgayTra.addActionListener(e -> capNhatTamTinh.run());
        capNhatTamTinh.run();

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(GiaoDien.TRANG);
        form.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 6, 8, 6);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        FormXeMay.addFormRow(form, g, 0, "Xe đã chọn", lblXe);
        FormXeMay.addFormRow(form, g, 1, "Giá thuê", lblGia);
        FormXeMay.addFormRow(form, g, 2, "Ngày thuê (yyyy-MM-dd)", txtNgayThue);
        FormXeMay.addFormRow(form, g, 3, "Ngày trả dự kiến", txtNgayTra);
        FormXeMay.addFormRow(form, g, 4, "Tạm tính", lblTamTinh);

        dialog.add(form, BorderLayout.CENTER);

        dialog.add(FormXeMay.taoDialogFooter(dialog, false, () -> {
            try {
                LocalDate nThue = DateUtil.parse(txtNgayThue.getText());
                LocalDate nTra = DateUtil.parse(txtNgayTra.getText());
                hopDongBUS.taoHopDong(khachHang.getId(), idXe, null, nThue, nTra, 0);
                JOptionPane.showMessageDialog(dialog,
                        "Đặt thuê thành công! Mời bạn đến cửa hàng nhận xe.");
                napDuLieu();
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
}
