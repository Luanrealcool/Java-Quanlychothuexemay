package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class GiaoDien {
    // === TÔNG MÀU CAM AMBER (warm, năng động) ===
    public static final Color XANH_CHINH = new Color(234, 88, 12);    // Orange-600 (primary)
    public static final Color XANH_DAM   = new Color(194, 65, 12);    // Orange-700 (darker)
    public static final Color XANH_LA    = new Color(22, 163, 74);    // Green-600 (success)
    public static final Color CAM        = new Color(245, 158, 11);   // Amber-500 (warning)
    public static final Color DO         = new Color(220, 38, 38);    // Red-600 (danger)
    public static final Color TIM        = new Color(219, 39, 119);   // Pink-600 (accent)
    public static final Color HONG       = new Color(236, 72, 153);   // Pink-500
    public static final Color XAM        = new Color(120, 113, 108);  // Stone-500
    public static final Color XAM_DAM    = new Color(87, 83, 78);     // Stone-600
    public static final Color XAM_NHAT   = new Color(245, 239, 230);  // Warm beige (page bg)
    public static final Color SIDEBAR    = new Color(28, 25, 23);     // Stone-900 (nâu-đen)
    public static final Color SIDEBAR_HOVER = new Color(41, 37, 36);  // Stone-800
    public static final Color VIEN       = new Color(232, 221, 201);  // Warm tan border
    public static final Color VIEN_INPUT = new Color(220, 201, 166);  // Tan input border
    public static final Color CHU_CHINH  = new Color(41, 37, 36);     // Stone-800
    public static final Color CHU_PHU    = new Color(120, 113, 108);  // Stone-500
    public static final Color CHU_LABEL  = new Color(68, 64, 60);     // Stone-700
    public static final Color TRANG      = new Color(255, 251, 245);  // Cream off-white (card bg)
    public static final Color TRANG_TINH = Color.WHITE;               // Pure white (text on dark)
    public static final Color DEN        = new Color(28, 25, 23);

    public static final Font FONT_TIEU_DE  = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_NUT      = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_THUONG   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_HEADER   = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_LABEL    = new Font("Segoe UI", Font.BOLD, 11);
    public static final Font FONT_CARD_TT  = new Font("Segoe UI", Font.BOLD, 12);

    public static void styleNut(JButton btn, Color mau) {
        btn.setBackground(mau);
        btn.setForeground(TRANG);
        btn.setFont(FONT_NUT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void styleInput(JComponent c) {
        c.setFont(FONT_THUONG);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VIEN_INPUT),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)));
    }

    public static JLabel taoLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL);
        l.setForeground(CHU_LABEL);
        return l;
    }

    public static JPanel taoPageHeader(String tieuDe, String moTa) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel(tieuDe);
        lblTitle.setFont(FONT_TIEU_DE);
        lblTitle.setForeground(CHU_CHINH);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lblTitle);

        JLabel lblSub = new JLabel(moTa);
        lblSub.setFont(FONT_THUONG);
        lblSub.setForeground(CHU_PHU);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(Box.createVerticalStrut(4));
        p.add(lblSub);
        return p;
    }

    public static JPanel taoStatCard(String label, JLabel lblValue, Color accent, String icon) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(TRANG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
                BorderFactory.createEmptyBorder(14, 18, 14, 18)));

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        lblIcon.setForeground(accent);
        card.add(lblIcon, BorderLayout.WEST);

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblValue.setForeground(CHU_CHINH);
        inner.add(lblValue);

        JLabel lblName = new JLabel(label);
        lblName.setFont(FONT_LABEL);
        lblName.setForeground(CHU_PHU);
        inner.add(lblName);

        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    public static JPanel taoCardHeader(String text) {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(XAM_NHAT);
        h.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, VIEN),
                BorderFactory.createEmptyBorder(10, 18, 10, 18)));
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_CARD_TT);
        lbl.setForeground(CHU_LABEL);
        h.add(lbl, BorderLayout.WEST);
        return h;
    }

    public static JPanel taoCard() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(TRANG);
        p.setBorder(BorderFactory.createLineBorder(VIEN));
        return p;
    }

    public static JPanel taoHeader(String tieuDe) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(XANH_DAM);
        p.setBorder(BorderFactory.createEmptyBorder(14, 25, 14, 25));
        JLabel lbl = new JLabel(tieuDe);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(TRANG);
        p.add(lbl, BorderLayout.WEST);
        return p;
    }

    public static void styleBang(JTable bang) {
        bang.setRowHeight(32);
        bang.setFont(FONT_THUONG);
        bang.setShowGrid(false);
        bang.setIntercellSpacing(new Dimension(0, 0));
        bang.setBackground(TRANG);
        bang.setSelectionBackground(new Color(255, 237, 213));  // Orange-100
        bang.setSelectionForeground(CHU_CHINH);
        bang.setGridColor(VIEN);

        JTableHeader h = bang.getTableHeader();
        h.setFont(FONT_HEADER);
        h.setBackground(XAM_NHAT);
        h.setForeground(CHU_LABEL);
        h.setOpaque(true);
        h.setReorderingAllowed(false);
        h.setPreferredSize(new Dimension(0, 38));
        h.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, VIEN));
        ((DefaultTableCellRenderer) h.getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);
    }

    public static DefaultTableCellRenderer rendererTrangThai() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean focus, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, focus, r, c);
                String s = v == null ? "" : v.toString();
                Color bg = TRANG;
                Color fg = CHU_CHINH;
                if ("SAN_SANG".equals(s))      { bg = new Color(220, 252, 231); fg = new Color(21, 128, 61); }
                else if ("DANG_THUE".equals(s)){ bg = new Color(255, 237, 213); fg = new Color(194, 65, 12); }
                else if ("BAO_TRI".equals(s))  { bg = new Color(254, 226, 226); fg = new Color(185, 28, 28); }
                else if ("DA_TRA".equals(s))   { bg = new Color(231, 229, 228); fg = new Color(68, 64, 60); }
                if (sel) bg = new Color(255, 237, 213);
                setBackground(bg);
                setForeground(fg);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                setHorizontalAlignment(CENTER);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        };
    }

    public static DefaultTableCellRenderer rendererTien() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean focus, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, focus, r, c);
                if (v != null && !v.toString().isEmpty()) {
                    try {
                        long tien = Long.parseLong(v.toString());
                        setText(String.format("%,d đ", tien));
                    } catch (NumberFormatException ex) {
                        setText(v.toString());
                    }
                } else {
                    setText("");
                }
                setHorizontalAlignment(RIGHT);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 12));
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                if (!sel) setForeground(CHU_CHINH);
                return this;
            }
        };
    }

    public static void setRenderer(JTable bang, int col, DefaultTableCellRenderer r) {
        if (col >= 0 && col < bang.getColumnCount()) {
            bang.getColumnModel().getColumn(col).setCellRenderer(r);
        }
    }
}
