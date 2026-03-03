package com.ccrcm.infovault.service.impl;

import com.ccrcm.infovault.dto.request.CommentRequest;
import com.ccrcm.infovault.dto.response.CommentResponse;
import com.ccrcm.infovault.entity.Article;
import com.ccrcm.infovault.entity.ArticleComment;
import com.ccrcm.infovault.entity.User;
import com.ccrcm.infovault.exception.BadRequestException;
import com.ccrcm.infovault.exception.UnauthorizedException;
import com.ccrcm.infovault.repository.ArticleCommentRepository;
import com.ccrcm.infovault.repository.ArticleRepository;
import com.ccrcm.infovault.repository.UserRepository;
import com.ccrcm.infovault.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ArticleCommentServiceImpl implements ArticleCommentService {

    private final ArticleCommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    // ADD COMMENT / REPLY
    @Override
    public CommentResponse addComment(CommentRequest request, Long currentUserId) {

        Article article = articleRepository.findById(request.getArticleId())
                .orElseThrow(() -> new BadRequestException("Article not found"));

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        ArticleComment comment = new ArticleComment();
        comment.setArticle(article);
        comment.setContent(request.getContent());
        comment.setUser(user);

        boolean isAdmin = "ADMIN".equalsIgnoreCase(user.getRole().getName());
        comment.setIsAdminReply(isAdmin);

        if (request.getParentId() != null) {
            ArticleComment parent = commentRepository.findById(request.getParentId())
                    .filter(ArticleComment::getActive)
                    .orElseThrow(() -> new BadRequestException("Parent comment not found"));

            comment.setParent(parent);
        }

        ArticleComment saved = commentRepository.save(comment);

        return mapWithReplies(saved);
    }
    // UPDATE COMMENT (ADMIN OR OWNER)
    @Override
    public CommentResponse updateComment(Long commentId,
                                         CommentRequest request,
                                         Long currentUserId) {

        ArticleComment comment = commentRepository.findById(commentId)
                .filter(ArticleComment::getActive)
                .orElseThrow(() -> new BadRequestException("Comment not found"));

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        boolean isAdmin = "ADMIN".equalsIgnoreCase(user.getRole().getName());
        boolean isOwner = comment.getUser().getId().equals(currentUserId);

        if (!isAdmin && !isOwner) {
            throw new UnauthorizedException("You are not allowed to update this comment");
        }

        comment.setContent(request.getContent());
        ArticleComment updated = commentRepository.save(comment);

        return mapWithReplies(updated);
    }

    // GET COMMENTS (THREADED)
    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long articleId) {

        log.info("Fetching comments for article: {}", articleId);

        List<ArticleComment> rootComments =
                commentRepository
                        .findByArticleIdAndParentIsNullAndActiveTrueOrderByIsPinnedDescIsAdminReplyDescCreatedAtDesc(articleId);

        return rootComments.stream()
                .map(this::mapWithReplies)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void pinComment(Long commentId) {
        ArticleComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // Prevent pinning replies (optional but recommended)
        if (comment.getParent() != null) {
            throw new RuntimeException("Only root comments can be pinned");
        }

        if (comment.getIsPinned()) {
            throw new RuntimeException("Comment already pinned");
        }

        long pinnedCount = commentRepository
                .countByArticleIdAndIsPinnedTrueAndActiveTrue(
                        comment.getArticle().getId());

        if (pinnedCount >= 2) {
            throw new RuntimeException("Maximum 2 comments can be pinned");
        }

        comment.setIsPinned(true);
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void unpinComment(Long commentId) {

        ArticleComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getIsPinned()) {
            throw new RuntimeException("Comment is not pinned");
        }

        comment.setIsPinned(false);
        commentRepository.save(comment);
    }

    // DELETE COMMENT (ADMIN OR OWNER)
    @Override
    public void deleteComment(Long commentId, Long currentUserId) {

        ArticleComment comment = commentRepository.findById(commentId)
                .filter(ArticleComment::getActive)
                .orElseThrow(() -> new BadRequestException("Comment not found"));

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        boolean isAdmin = "ADMIN".equalsIgnoreCase(user.getRole().getName());
        boolean isOwner = comment.getUser().getId().equals(currentUserId);

        if (!isAdmin && !isOwner) {
            throw new UnauthorizedException("You are not allowed to delete this comment");
        }

        comment.setActive(false);
        commentRepository.save(comment);
    }

    // MAPPING WITH REPLIES (Recursive)
    private CommentResponse mapWithReplies(ArticleComment comment) {

        CommentResponse response = new CommentResponse();

        User user = comment.getUser();

        response.setId(comment.getId());
        response.setUserId(user.getId());
        response.setUserName(user.getFirstName() + " " + user.getLastName());
        response.setContent(comment.getContent());
        response.setIsAdminReply(comment.getIsAdminReply());
        response.setCreatedAt(comment.getCreatedAt());

        List<ArticleComment> replies =
                commentRepository
                        .findByParentIdAndActiveTrueOrderByIsAdminReplyDescCreatedAtAsc(comment.getId());

        response.setReplies(
                replies.stream()
                        .map(this::mapWithReplies)
                        .collect(Collectors.toList())
        );

        return response;
    }
}