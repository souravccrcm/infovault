package com.ccrcm.infovault.repository;

import com.ccrcm.infovault.entity.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleImageRepository extends JpaRepository<ArticleImage, Long> {

    // Optional: Get all active images of an article
    List<ArticleImage> findByArticleIdAndActiveTrue(Long articleId);

}
