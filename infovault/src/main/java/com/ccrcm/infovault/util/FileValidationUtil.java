package com.ccrcm.infovault.util;

import com.ccrcm.infovault.exception.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public final class FileValidationUtil {

    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024; // 20 MB

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "pdf", "doc", "docx", "xls", "xlsx"
    );

    private FileValidationUtil() {}

    public static void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File must not be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size must be less than 20 MB");
        }

        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BadRequestException("Invalid file type");
        }
    }
}
