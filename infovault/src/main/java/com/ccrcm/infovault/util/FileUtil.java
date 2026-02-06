
package com.ccrcm.infovault.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public final class FileUtil {

    private FileUtil() {}

    public static String saveFile(String basePath, MultipartFile file) throws IOException {

        File directory = new File(basePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String fullPath = basePath + File.separator + fileName;

        file.transferTo(new File(fullPath));
        return fullPath;
    }
}
