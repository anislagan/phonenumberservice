package com.belong.phonenumberservice.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileUtils {

    public static void ensureDirectoryExists(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
    }

    public static void backupFile(Path source, Path backupDir) throws IOException {
        if (Files.exists(source)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            Path backup = backupDir.resolve(source.getFileName() + "." + timestamp + ".backup");
            Files.copy(source, backup);
        }
    }

    public static void atomicWrite(Path file, String content) throws IOException {
        Path temp = Files.createTempFile(file.getParent(), "temp", null);
        try {
            Files.writeString(temp, content);
            Files.move(temp, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temp);
        }
    }
}