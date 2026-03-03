package com.ccrcm.infovault.service;

import com.ccrcm.infovault.dto.request.CommentRequest;
import com.ccrcm.infovault.dto.response.CommentResponse;

import java.util.List;

public interface ArticleCommentService {

    CommentResponse addComment(CommentRequest request, Long currentUserId);

    CommentResponse updateComment(Long commentId,
                                  CommentRequest request,
                                  Long currentUserId);

    void deleteComment(Long commentId, Long currentUserId);

    List<CommentResponse> getComments(Long articleId);

    void pinComment(Long id);

    void unpinComment(Long id);
}