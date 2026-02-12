package com.ccrcm.infovault.repository;

import com.ccrcm.infovault.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a FROM Article a WHERE a.active = true")
    List<Article> findByActiveTrue();

    @Query("""
        SELECT COUNT(a)
        FROM Article a
        WHERE a.active = true
          AND a.createdAt > :fromTime
    """)
    long countNewArticles(LocalDateTime fromTime);

    @Query("""
        SELECT a.articleType, COUNT(a)
        FROM Article a
        WHERE a.active = true
          AND a.createdAt > :fromTime
        GROUP BY a.articleType
    """)
    List<Object[]> countByArticleType(@Param("fromTime") LocalDateTime fromTime);

    long countByActiveTrue();
}