package com.azeauj.takitransfer.core.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    private ZipUtils() {
    }

    public static byte[] zipFiles(List<MultipartFile> files) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue;
                }

                String filename = file.getOriginalFilename();
                if (filename == null) {
                    filename = UUID.randomUUID().toString();
                }
                ZipEntry entry = new ZipEntry(filename);
                zos.putNextEntry(entry);
                zos.write(file.getBytes());
                zos.closeEntry();
            }

            zos.finish();
            return baos.toByteArray();
        }
    }
}
