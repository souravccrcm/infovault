package com.ccrcm.infovault.enums;

public enum ArticleStatus {

    DRAFT(1),
    PUBLISHED(2),
    ARCHIVED(3);

    private final int code;

    ArticleStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ArticleStatus fromCode(int code) {
        for (ArticleStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid ArticleStatus code: " + code);
    }
}