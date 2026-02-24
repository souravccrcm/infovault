package com.ccrcm.infovault.service.impl;

import com.ccrcm.infovault.dto.response.ArticleListResponse;
import com.ccrcm.infovault.dto.response.ArticleResponse;
import com.ccrcm.infovault.dto.response.ArticleTypeCountDTO;
import com.ccrcm.infovault.dto.response.CountryArticleCountDTO;
import com.ccrcm.infovault.entity.UserLoginLog;
import com.ccrcm.infovault.mapper.ArticleMapper;
import com.ccrcm.infovault.repository.ArticleRepository;
import com.ccrcm.infovault.repository.UserLoginLogRepository;
import com.ccrcm.infovault.service.HomeService;
import com.ccrcm.infovault.util.CountryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final ArticleRepository articleRepository;
    private final UserLoginLogRepository userLoginLogRepository;

    @Override
    public ArticleListResponse getAllArticles() {
        Long userId = 1L; // later from SecurityContext/SSO

        // Get last session
        Optional<UserLoginLog> sessionOpt =
                userLoginLogRepository.findTopByUserIdOrderByLoginTimeDesc(userId);

        LocalDateTime fromTime = null;
        LocalDateTime lastLogoutTime = null;

        if (sessionOpt.isPresent()) {
            UserLoginLog log = sessionOpt.get();

            lastLogoutTime = log.getLogoutTime();

            fromTime = (log.getLogoutTime() != null)
                    ? log.getLogoutTime()
                    : log.getLoginTime();
        }

        //  Fetch active articles
        List<ArticleResponse> articles = articleRepository.findByActiveTrue()
                .stream()
                .map(ArticleMapper::toResponse)
                .toList();

        long totalCount;
        List<ArticleTypeCountDTO> typeCounts = new ArrayList<>();

        // Count logic
        if (fromTime != null) {

            totalCount = articleRepository.countNewArticles(fromTime);

            List<Object[]> rows =
                    articleRepository.countByUpdateType(fromTime);

            for (Object[] row : rows) {

                String typeName = (String) row[0];
                Long count = ((Number) row[1]).longValue();

                typeCounts.add(
                        new ArticleTypeCountDTO(typeName, count)
                );
            }

        } else {
            totalCount = articleRepository.countByActiveTrue();
        }

        //  Build final response
        return new ArticleListResponse(lastLogoutTime,
                totalCount,
                typeCounts,
                articles
        );
    }

    @Override
    public List<CountryArticleCountDTO> getNewArticleCountsByRegion() {

        Long userId = 1L; // later from SecurityContext/SSO

        Optional<UserLoginLog> sessionOpt =
                userLoginLogRepository.findTopByUserIdOrderByLoginTimeDesc(userId);

        LocalDateTime fromTime = null;
        if (sessionOpt.isPresent()) {
            UserLoginLog log = sessionOpt.get();
            fromTime = (log.getLogoutTime() != null)
                    ? log.getLogoutTime()
                    : log.getLoginTime();
        }

        List<Object[]> rows;
        if (fromTime != null) {
            rows = articleRepository.countByCountrySince(fromTime);
        } else {
            rows = articleRepository.countByCountryActive();
        }

        List<CountryArticleCountDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            String countryName = (String) row[0];
            Long count = ((Number) row[1]).longValue();
            String iso2 = CountryUtil.toIso2(countryName);
            result.add(new CountryArticleCountDTO(countryName, iso2, count));
        }

        return result;
    }
}
