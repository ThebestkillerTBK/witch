package me.soda.witch.shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class FileUtil {
    public static String read(String file) {
        return new String(read(new File(file)), StandardCharsets.UTF_8);
    }

    public static byte[] read(File file) {
        try (FileInputStream is = new FileInputStream(file)) {
            return is.readAllBytes();
        } catch (Exception e) {
            LogUtil.printStackTrace(e);
            return new byte[0];
        }
    }

    public static void write(File file, String data) {
        write(file, data.getBytes(StandardCharsets.UTF_8));
    }

    public static void write(File file, byte[] data) {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(data);
            }
        } catch (Exception e) {
            LogUtil.printStackTrace(e);
        }
    }
}
