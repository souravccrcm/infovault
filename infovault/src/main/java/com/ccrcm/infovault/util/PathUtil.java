package com.ccrcm.infovault.util;

public final class PathUtil {

    private PathUtil() {}

    public static String buildArticlePath(
            String basePath,
            String country,
            String type
    ) {
        return basePath + "/" + country + "/" + type;
    }
}