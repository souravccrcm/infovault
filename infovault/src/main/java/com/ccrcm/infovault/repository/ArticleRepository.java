package com.ccrcm.infovault.repository;

import com.ccrcm.infovault.dto.response.RcmUpdateResponse;
import com.ccrcm.infovault.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

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
   SELECT new com.ccrcm.infovault.dto.response.RcmUpdateResponse(
       a.id,
       a.title,
       s.name,
       c.name,
       u.name,
       ct.name,
       a.createdAt,
       il.name,
       a.fileName,
       a.fileSize
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
}
