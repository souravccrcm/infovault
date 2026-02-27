package com.ccrcm.infovault.repository;

import com.ccrcm.infovault.entity.ArticleVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleVideoRepository extends JpaRepository<ArticleVideo, Long> {

    // Optional: Get all active videos of an article
    List<ArticleVideo> findByArticleIdAndActiveTrue(Long articleId);

}
