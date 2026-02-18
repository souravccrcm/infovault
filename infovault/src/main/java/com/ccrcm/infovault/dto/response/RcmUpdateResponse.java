package com.ccrcm.infovault.dto.response;

import com.ccrcm.infovault.entity.ImpactLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RcmUpdateResponse {

    private Long id;
    private String title;
    private String source;
    private String country;
    private String updateType;
    private String clinicalType;
    private String articleContent;
    private String fileBase64; // add this
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime createdAt;
    private String impactLevel;
    private String fileName;
    private Long fileSize;



    public RcmUpdateResponse(Long id,
                             String title,
                             String source,
                             String country,
                             String updateType,
                             String clinicalType,
                             LocalDateTime createdAt,
                             String impactLevel,
                             String fileName,
                             Long fileSize) {
        this.id = id;
        this.title = title;
        this.source = source;
        this.country = country;
        this.updateType = updateType;
        this.clinicalType = clinicalType;
        this.createdAt = createdAt;
        this.impactLevel = impactLevel;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }
}



