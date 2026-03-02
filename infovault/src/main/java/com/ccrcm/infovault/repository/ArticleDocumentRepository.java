package com.ccrcm.infovault.repository;

import com.ccrcm.infovault.entity.ArticleDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleDocumentRepository extends JpaRepository<ArticleDocument, Long> {

    List<ArticleDocument> findByArticleIdAndActiveTrue(Long articleId);

    Optional<ArticleDocument> findByIdAndActiveTrue(Long id);
}
