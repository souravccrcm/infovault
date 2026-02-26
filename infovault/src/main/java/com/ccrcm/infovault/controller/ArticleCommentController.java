package com.ccrcm.infovault.controller;

import com.ccrcm.infovault.dto.request.CommentRequest;
import com.ccrcm.infovault.dto.response.CommentResponse;
import com.ccrcm.infovault.globalResposeDto.ApiResponse;
import com.ccrcm.infovault.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleCommentController {

    private final ArticleCommentService commentService;

    @PostMapping("/{articleId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(
            @PathVariable Long articleId,
            @RequestBody CommentRequest request,
            @RequestParam Long userId) {

        request.setArticleId(articleId);

        CommentResponse response =
                commentService.addComment(request, userId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Comment added", response)
        );
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequest request,
            @RequestParam Long userId) {

        CommentResponse response =
                commentService.updateComment(commentId, request, userId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Comment updated successfully", response)
        );
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long commentId,
            @RequestParam Long userId) {

        commentService.deleteComment(commentId, userId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Deleted", null)
        );
    }

    @GetMapping("/{articleId}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(
            @PathVariable Long articleId) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Comments fetched",
                        commentService.getComments(articleId))
        );
    }
}