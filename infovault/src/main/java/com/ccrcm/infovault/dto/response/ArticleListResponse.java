package com.ccrcm.infovault.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListResponse {
    private LocalDateTime lastLogoutTime;
    private long newTotalCount;   // total new articles
    private List<ArticleTypeCountDTO> typeCounts; // by articleType
    private List<ArticleResponse> articles; // actual articles

}
