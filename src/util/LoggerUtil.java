package util;

import config.AppConfig;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerUtil {
    private static final Path LOG_FILE = Paths.get(AppConfig.get("log.file", "logs/app.log"));
    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        try {
            Path parent = LOG_FILE.getParent();
            if (parent != null && !Files.exists(parent)) Files.createDirectories(parent);
            if (!Files.exists(LOG_FILE)) Files.createFile(LOG_FILE);
        } catch (IOException ignored) {}
    }

    public static void info(String msg) {
        write("INFO", msg, null);
    }

    public static void warn(String msg) {
        write("WARN", msg, null);
    }

    public static void error(String msg, Throwable t) {
        write("ERROR", msg, t);
    }

    private static synchronized void write(String level, String msg, Throwable t) {
        String time = LocalDateTime.now().format(TS);
        StringBuilder line = new StringBuilder();
        line.append('[').append(time).append("] ").append(level).append(" - ").append(msg);
        if (t != null) {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            line.append(System.lineSeparator()).append(sw);
        }
        line.append(System.lineSeparator());

        System.out.print(line);
        try {
            Files.write(LOG_FILE, line.toString().getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ignored) {}
    }
}
