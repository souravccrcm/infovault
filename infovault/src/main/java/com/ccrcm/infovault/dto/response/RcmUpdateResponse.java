package com.ccrcm.infovault.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public RcmUpdateResponse(Long id,
                             String title,
                             String source,
                             String country,
                             String updateType,
                             String clinicalType) {
        this.id = id;
        this.title = title;
        this.source = source;
        this.country = country;
        this.updateType = updateType;
        this.clinicalType = clinicalType;
    }
}



