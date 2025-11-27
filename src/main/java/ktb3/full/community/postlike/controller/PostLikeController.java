package ktb3.full.community.postlike.controller;

import ktb3.full.community.auth.resolver.LoginUser;
import ktb3.full.community.common.response.ApiResponse;
import ktb3.full.community.postlike.dto.response.PostLikeStatusResponse;
import ktb3.full.community.postlike.dto.response.PostLikeResponse;
import ktb3.full.community.postlike.repository.PostLikeJpaRepository;
import ktb3.full.community.postlike.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/likes")

public class PostLikeController {
    private final PostLikeService postLikeService;
    private final PostLikeJpaRepository postLikeJpaRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<PostLikeStatusResponse>> getStatus(@PathVariable Long postId, @LoginUser Long userId) {
        PostLikeStatusResponse response =
                postLikeService.getStatus(postId, userId);

        return ResponseEntity.ok(ApiResponse.ok("status loaded", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostLikeResponse>> like(
            @PathVariable Long postId,
            @LoginUser Long userId
    ) {
        PostLikeResponse response = postLikeService.like(postId, userId);
        return ResponseEntity.ok(ApiResponse.ok("post liked successfully", response));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> unlike(
            @PathVariable Long postId,
            @LoginUser Long userId
    ) {
        postLikeService.unlike(postId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

/*    @GetMapping
    public ResponseEntity<ApiResponse<PostLikeStatusResponse>> count(
            @PathVariable Long postId
    ) {
        long likeCount = postLikeJpaRepository.countByPost_Id(postId);
        return ResponseEntity.ok(ApiResponse.ok("likeCount retrieved successfully", PostLikeResponse.of(likeCount,liked)));
    }*/
}

