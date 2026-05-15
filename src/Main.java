import gui.FormDangNhap;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Font;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Font font = new Font("Segoe UI", Font.PLAIN, 13);
            UIManager.put("Label.font", font);
            UIManager.put("TextField.font", font);
            UIManager.put("PasswordField.font", font);
            UIManager.put("ComboBox.font", font);
            UIManager.put("Button.font", font);
            UIManager.put("TabbedPane.font", new Font("Segoe UI", Font.BOLD, 13));
            UIManager.put("TitledBorder.font", new Font("Segoe UI", Font.BOLD, 12));
            UIManager.put("OptionPane.messageFont", font);
            UIManager.put("OptionPane.buttonFont", font);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new FormDangNhap().setVisible(true));
    }
}
