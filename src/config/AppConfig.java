package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static final Properties props = new Properties();

    static {
        boolean loaded = false;
        try (InputStream in = new FileInputStream("config/config.properties")) {
            props.load(in);
            loaded = true;
        } catch (IOException ignored) {}

        if (!loaded) {
            try (InputStream in = AppConfig.class.getResourceAsStream("/config.properties")) {
                if (in != null) {
                    props.load(in);
                    loaded = true;
                }
            } catch (IOException ignored) {}
        }

        if (!loaded) {
            System.err.println("Cảnh báo: không tìm thấy config.properties, dùng giá trị mặc định");
        }
    }

    public static String get(String key, String def) {
        return props.getProperty(key, def);
    }

    public static int getInt(String key, int def) {
        try {
            return Integer.parseInt(props.getProperty(key));
        } catch (Exception e) {
            return def;
        }
    }
}
