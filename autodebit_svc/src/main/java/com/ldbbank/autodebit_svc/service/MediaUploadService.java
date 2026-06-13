package com.ldbbank.autodebit_svc.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MediaUploadService {
    private final Path baseDir;

    public MediaUploadService() {
        this.baseDir = Path.of(System.getProperty("user.dir"), "uploads");
    }

    public Map<String, String> store(MultipartFile file, String subDir) throws IOException {
        if (file == null || file.isEmpty()) throw new IOException("Empty file");
        String date = LocalDate.now().toString().replace("-", "");
        Path dir = baseDir.resolve(subDir).resolve(date);
        Files.createDirectories(dir);
        String orig = Path.of(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename()).getFileName().toString();
        String ext = "";
        int idx = orig.lastIndexOf('.');
        if (idx > 0) {
            ext = orig.substring(idx);
        }
        String name = UUID.randomUUID().toString() + ext;
        Path dest = dir.resolve(name);
        try (var in = file.getInputStream()) {
            Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
        }
        Map<String, String> res = new HashMap<>();
        res.put("path", dest.toAbsolutePath().toString());
        res.put("name", orig);
        return res;
    }
}
