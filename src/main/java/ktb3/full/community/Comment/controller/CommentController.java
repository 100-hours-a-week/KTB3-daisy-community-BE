package ktb3.full.community.Comment.controller;

import jakarta.validation.Valid;
import ktb3.full.community.Auth.resolver.LoginUser;
import ktb3.full.community.Comment.dto.request.CommentCreateRequest;
import ktb3.full.community.Comment.dto.request.CommentUpdateRequest;
import ktb3.full.community.Comment.dto.response.CommentResponse;
import ktb3.full.community.Comment.dto.response.CommentScrollResponse;
import ktb3.full.community.Comment.service.CommentService;
import ktb3.full.community.Common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<ApiResponse<CommentScrollResponse>> list(
            @PathVariable Long postId,
            @RequestParam(required = false) Long cursor) {
        CommentScrollResponse comments = commentService.scroll(postId, cursor);
        return ResponseEntity.ok(ApiResponse.ok("comments loaded successfully", comments));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> create(
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest dto,
            @LoginUser Long userId
    ) {
        CommentResponse comment = commentService.create(postId, userId, dto);
        return  ResponseEntity.ok(ApiResponse.ok("comment created successfully", comment));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> update(
            @PathVariable Long postId, @PathVariable Long id,
            @Valid @RequestBody CommentUpdateRequest dto,
            @LoginUser Long userId
    ) {
        CommentResponse comment = commentService.update(id, userId, dto);
        return ResponseEntity.ok(ApiResponse.ok("comment updated successfully", comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> delete(
            @PathVariable Long postId, @PathVariable Long id,
            @LoginUser Long userId
    ) {
        commentService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }
}
