package com.ccrcm.infovault.repository;

import com.ccrcm.infovault.dto.response.RcmUpdateResponse;
import com.ccrcm.infovault.entity.Article;
import com.ccrcm.infovault.enums.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

    @Query("SELECT a FROM Article a WHERE a.active = true")
    List<Article> findByActiveTrue();

    List<Article> findAllByActiveTrue();

    @Query("""
        SELECT COUNT(a)
        FROM Article a
        WHERE a.active = true
          AND a.createdAt > :fromTime
    """)
    long countNewArticles(@Param("fromTime") LocalDateTime fromTime);

    @Query("""
        SELECT a.updateType.name, COUNT(a)
        FROM Article a
        WHERE a.active = true
          AND a.createdAt > :fromTime
        GROUP BY a.updateType.name
    """)
    List<Object[]> countByUpdateType(@Param("fromTime") LocalDateTime fromTime);

    @Query("""
        SELECT a.country.name, COUNT(a)
        FROM Article a
        WHERE a.active = true
          AND a.createdAt > :fromTime
        GROUP BY a.country.name
    """)
    List<Object[]> countByCountrySince(@Param("fromTime") LocalDateTime fromTime);

    @Query("""
        SELECT a.country.name, COUNT(a)
        FROM Article a
        WHERE a.active = true
        GROUP BY a.country.name
    """)
    List<Object[]> countByCountryActive();

    @Query("""
   SELECT new com.ccrcm.infovault.dto.response.RcmUpdateResponse(
       a.id,
       a.title,
       s.name,
       c.name,
       u.name,
       ct.name,
       a.createdAt,
       il.name
   )
   FROM Article a
   JOIN a.source s
   JOIN a.country c
   JOIN a.updateType u
   JOIN a.clinicalType ct
   JOIN a.impactLevel il
   WHERE a.active = true
""")
    List<RcmUpdateResponse> fetchRcmUpdates();
    long countByActiveTrue();


    List<Article> findByCountryIdAndActiveTrueOrderByCreatedAtDesc(
            Long countryId,
            Pageable pageable
    );

    List<Article> findByActiveTrueAndStatus(ArticleStatus status);

    List<Article> findByCountryIdAndIdNotAndActiveTrueOrderByCreatedAtDesc(Long countryId, Long excludeArticleId, Pageable pageable);
    @Query(value = """
        SELECT a.*
        FROM articles a
        LEFT JOIN sources s ON s.id = a.source_id
        LEFT JOIN countries c ON c.id = a.country_id
        LEFT JOIN update_types ut ON ut.id = a.update_type_id
        LEFT JOIN clinical_types ct ON ct.id = a.clinical_type_id
        LEFT JOIN impact_levels il ON il.id = a.impact_level_id
        WHERE 
            a.active = 1
            AND a.status = :code
            AND (:countryIds IS NULL OR a.country_id IN (:countryIds))
            AND (:updateTypeIds IS NULL OR a.update_type_id IN (:updateTypeIds))
            AND (:clinicalTypeIds IS NULL OR a.clinical_type_id IN (:clinicalTypeIds))
            AND (
                :search IS NULL
                OR LOWER(a.title) LIKE CONCAT('%',:search,'%')
                OR LOWER(a.article_content) LIKE CONCAT('%',:search,'%')
                OR LOWER(s.name) LIKE CONCAT('%',:search,'%')
                OR LOWER(c.name) LIKE CONCAT('%',:search,'%')
                OR LOWER(ut.name) LIKE CONCAT('%',:search,'%')
                OR LOWER(ct.name) LIKE CONCAT('%',:search,'%')
                OR LOWER(il.name) LIKE CONCAT('%',:search,'%')
            )
        ORDER BY
            CASE
                WHEN LOWER(a.title) LIKE CONCAT(:search,'%') THEN 1
                WHEN LOWER(a.title) LIKE CONCAT('%',:search,'%') THEN 2
                WHEN LOWER(a.article_content) LIKE CONCAT('%',:search,'%') THEN 3
                WHEN LOWER(s.name) LIKE CONCAT('%',:search,'%') THEN 4
                WHEN LOWER(c.name) LIKE CONCAT('%',:search,'%') THEN 5
                WHEN LOWER(ut.name) LIKE CONCAT('%',:search,'%') THEN 6
                WHEN LOWER(ct.name) LIKE CONCAT('%',:search,'%') THEN 7
                WHEN LOWER(il.name) LIKE CONCAT('%',:search,'%') THEN 8
                ELSE 9
            END,
            a.created_at DESC
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM articles a
        WHERE 
            a.active = 1
            AND a.status = :code
            AND (:countryIds IS NULL OR a.country_id IN (:countryIds))
            AND (:updateTypeIds IS NULL OR a.update_type_id IN (:updateTypeIds))
            AND (:clinicalTypeIds IS NULL OR a.clinical_type_id IN (:clinicalTypeIds))
        """,
            nativeQuery = true)
    Page<Article> searchArticles(
            @Param("countryIds") List<Long> countryIds,
            @Param("updateTypeIds") List<Long> updateTypeIds,
            @Param("clinicalTypeIds") List<Long> clinicalTypeIds,
            @Param("search") String search,
            @Param("code") int code,
            Pageable pageable
    );

    @Query(value = """
        SELECT a.*
        FROM articles a
        LEFT JOIN sources s ON s.id = a.source_id
        LEFT JOIN countries c ON c.id = a.country_id
        LEFT JOIN update_types ut ON ut.id = a.update_type_id
        LEFT JOIN clinical_types ct ON ct.id = a.clinical_type_id
        
        WHERE
            a.active = 1
            
            AND (:sourceIdsEmpty = 1 OR a.source_id IN (:sourceIds))
            AND (:countryIdsEmpty = 1 OR a.country_id IN (:countryIds))
            AND (:updateTypeIdsEmpty = 1 OR a.update_type_id IN (:updateTypeIds))
            AND (:clinicalTypeIdsEmpty = 1 OR a.clinical_type_id IN (:clinicalTypeIds))
            AND (:statusCodesEmpty = 1 OR a.status IN (:statusCodes))

            AND (
                :search IS NULL
                OR LOWER(a.title) LIKE CONCAT('%',:search,'%')
            )

        ORDER BY
            CASE
                WHEN LOWER(a.title) LIKE CONCAT(:search,'%') THEN 1
                WHEN LOWER(a.title) LIKE CONCAT('%',:search,'%') THEN 2
                ELSE 3
            END,
            a.created_at DESC
        """,

            countQuery = """
        SELECT COUNT(*)
        FROM articles a
        WHERE
            a.active = 1
            
            AND (:sourceIdsEmpty = 1 OR a.source_id IN (:sourceIds))
            AND (:countryIdsEmpty = 1 OR a.country_id IN (:countryIds))
            AND (:updateTypeIdsEmpty = 1 OR a.update_type_id IN (:updateTypeIds))
            AND (:clinicalTypeIdsEmpty = 1 OR a.clinical_type_id IN (:clinicalTypeIds))
            AND (:statusCodesEmpty = 1 OR a.status IN (:statusCodes))

            AND (
                :search IS NULL
                OR LOWER(a.title) LIKE CONCAT('%',:search,'%')
            )
        """,
            nativeQuery = true)
    Page<Article> searchArticlesWithFilters(

            @Param("sourceIds") List<Long> sourceIds,
            @Param("sourceIdsEmpty") int sourceIdsEmpty,

            @Param("countryIds") List<Long> countryIds,
            @Param("countryIdsEmpty") int countryIdsEmpty,

            @Param("updateTypeIds") List<Long> updateTypeIds,
            @Param("updateTypeIdsEmpty") int updateTypeIdsEmpty,

            @Param("clinicalTypeIds") List<Long> clinicalTypeIds,
            @Param("clinicalTypeIdsEmpty") int clinicalTypeIdsEmpty,

            @Param("statusCodes") List<Integer> statusCodes,
            @Param("statusCodesEmpty") int statusCodesEmpty,

            @Param("search") String search,
            Pageable pageable
    );

}
