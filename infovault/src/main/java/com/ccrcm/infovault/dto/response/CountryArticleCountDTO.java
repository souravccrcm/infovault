package com.ccrcm.infovault.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryArticleCountDTO {
    private String countryName;
    private String countryCode; // ISO2 or short form via CountryUtil
    private Long count;
}
