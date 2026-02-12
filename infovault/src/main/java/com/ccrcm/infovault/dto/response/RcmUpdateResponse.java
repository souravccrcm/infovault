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
    private String type;
    private String clinicalType;
}

