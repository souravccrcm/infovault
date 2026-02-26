
package com.ccrcm.infovault.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class FileUtil {

    private FileUtil() {}
    private static final Set<String> DOC_ALLOWED =
            new HashSet<>(Arrays.asList("pdf"));

    private static final Set<String> IMAGE_ALLOWED =
            new HashSet<>(Arrays.asList("jpg", "jpeg", "png"));

    private static final Set<String> VIDEO_ALLOWED =
            new HashSet<>(Arrays.asList("mp4", "mov", "avi"));

    private static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    private static void validateExtension(String extension, Set<String> allowed) {
        if (!allowed.contains(extension)) {
            throw new IllegalArgumentException("Invalid file type: " + extension);
        }
    }

    private static String save(String storagePath,
                               String folderName,
                               String fileName,
                               MultipartFile file) throws IOException {

        Path folderPath = Paths.get(storagePath, folderName);

        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }

        Path fullPath = folderPath.resolve(fileName);

        file.transferTo(fullPath.toFile());

        return fullPath.toString();
    }

    // ================= DOCUMENT =================
    public static String saveDocument(String storagePath,
                                      MultipartFile file) throws IOException {

        String originalName = file.getOriginalFilename();
        String ext = getExtension(originalName);
        validateExtension(ext, DOC_ALLOWED);

        return save(storagePath, "ArticlesUpload", originalName, file);
    }

    // ================= IMAGE =================
    public static String saveImage(String storagePath,
                                   Long articleId,
                                   MultipartFile file) throws IOException {

        String originalName = file.getOriginalFilename();
        String ext = getExtension(originalName);
        validateExtension(ext, IMAGE_ALLOWED);

        String newName = articleId + "_" + originalName;

        return save(storagePath, "Articleimage", newName, file);
    }

    // ================= VIDEO =================
    public static String saveVideo(String storagePath,
                                   Long articleId,
                                   MultipartFile file) throws IOException {

        String originalName = file.getOriginalFilename();
        String ext = getExtension(originalName);
        validateExtension(ext, VIDEO_ALLOWED);

        String newName = articleId + "_" + originalName;

        return save(storagePath, "Articlevideo", newName, file);
    }
}
