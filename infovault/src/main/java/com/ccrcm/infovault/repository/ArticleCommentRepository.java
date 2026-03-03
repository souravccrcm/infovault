package com.ccrcm.infovault.repository;

import com.ccrcm.infovault.entity.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleCommentRepository
        extends JpaRepository<ArticleComment, Long> {

    List<ArticleComment> findByArticleIdAndParentIsNullAndActiveTrueOrderByIsAdminReplyDescCreatedAtDesc(Long articleId);

    List<ArticleComment> findByParentIdAndActiveTrueOrderByIsAdminReplyDescCreatedAtAsc(Long parentId);

    long countByArticleIdAndIsPinnedTrueAndActiveTrue(Long id);

    List<ArticleComment> findByArticleIdAndParentIsNullAndActiveTrueOrderByIsPinnedDescIsAdminReplyDescCreatedAtDesc(Long articleId);
}
