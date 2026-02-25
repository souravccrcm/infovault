package com.ccrcm.infovault.converter;

import com.ccrcm.infovault.enums.ArticleStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class ArticleStatusConverter implements AttributeConverter<ArticleStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ArticleStatus status) {
        return status != null ? status.getCode() : null;
    }

    @Override
    public ArticleStatus convertToEntityAttribute(Integer code) {
        return code != null ? ArticleStatus.fromCode(code) : null;
    }
}