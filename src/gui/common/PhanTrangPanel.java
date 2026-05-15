package gui.common;

import constant.AppConstant;
import gui.GiaoDien;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

public class PhanTrangPanel extends JPanel {
    private int currentPage = 1;
    private int pageSize = AppConstant.PAGE_SIZE_DEFAULT;
    private int totalCount = 0;

    private final JLabel lblInfo = new JLabel();
    private final JLabel lblPageInfo = new JLabel();
    private final JButton btnFirst = new JButton("⟪");
    private final JButton btnPrev = new JButton("<");
    private final JButton btnNext = new JButton(">");
    private final JButton btnLast = new JButton("⟫");
    private final JComboBox<Integer> cboSize = new JComboBox<>(AppConstant.PAGE_SIZE_OPTIONS);
    private final JTextField txtPage = new JTextField(3);

    private BiConsumer<Integer, Integer> onChange;  // (page, size)

    public PhanTrangPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        left.setOpaque(false);
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(GiaoDien.CHU_PHU);
        left.add(lblInfo);
        add(left, BorderLayout.WEST);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        center.setOpaque(false);
        styleNutNav(btnFirst);
        styleNutNav(btnPrev);
        styleNutNav(btnNext);
        styleNutNav(btnLast);
        txtPage.setHorizontalAlignment(SwingConstants.CENTER);
        txtPage.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GiaoDien.VIEN_INPUT),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        center.add(btnFirst);
        center.add(btnPrev);
        center.add(new JLabel("Trang"));
        center.add(txtPage);
        center.add(lblPageInfo);
        center.add(btnNext);
        center.add(btnLast);
        add(center, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        right.setOpaque(false);
        right.add(new JLabel("/ trang"));
        cboSize.setSelectedItem(pageSize);
        right.add(cboSize);
        add(right, BorderLayout.EAST);

        btnFirst.addActionListener(e -> goTo(1));
        btnPrev.addActionListener(e -> goTo(currentPage - 1));
        btnNext.addActionListener(e -> goTo(currentPage + 1));
        btnLast.addActionListener(e -> goTo(getTotalPages()));
        txtPage.addActionListener(e -> {
            try { goTo(Integer.parseInt(txtPage.getText().trim())); }
            catch (NumberFormatException ex) { capNhatHienThi(); }
        });
        cboSize.addActionListener(e -> {
            Integer v = (Integer) cboSize.getSelectedItem();
            if (v != null && v != pageSize) {
                pageSize = v;
                currentPage = 1;
                fireChange();
            }
        });

        capNhatHienThi();
    }

    private void styleNutNav(JButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBackground(GiaoDien.TRANG);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GiaoDien.VIEN_INPUT),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void setOnChange(BiConsumer<Integer, Integer> cb) { this.onChange = cb; }

    public int getCurrentPage() { return currentPage; }
    public int getPageSize() { return pageSize; }

    public void setTotal(int totalCount, int currentPage, int pageSize) {
        this.totalCount = totalCount;
        this.pageSize = pageSize > 0 ? pageSize : 20;
        cboSize.setSelectedItem(this.pageSize);
        this.currentPage = Math.max(1, Math.min(currentPage, Math.max(1, getTotalPages())));
        capNhatHienThi();
    }

    private int getTotalPages() {
        if (pageSize <= 0) return 1;
        return Math.max(1, (int) Math.ceil((double) totalCount / pageSize));
    }

    private void goTo(int page) {
        int max = getTotalPages();
        int newPage = Math.max(1, Math.min(page, max));
        if (newPage != currentPage) {
            currentPage = newPage;
            fireChange();
        } else {
            capNhatHienThi();
        }
    }

    private void fireChange() {
        if (onChange != null) onChange.accept(currentPage, pageSize);
    }

    private void capNhatHienThi() {
        int total = getTotalPages();
        int from = totalCount == 0 ? 0 : (currentPage - 1) * pageSize + 1;
        int to = Math.min(currentPage * pageSize, totalCount);
        lblInfo.setText(String.format("Hiển thị %d-%d / %d bản ghi", from, to, totalCount));
        lblPageInfo.setText("/ " + total);
        txtPage.setText(String.valueOf(currentPage));

        btnFirst.setEnabled(currentPage > 1);
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < total);
        btnLast.setEnabled(currentPage < total);
    }
}
