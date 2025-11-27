package ktb3.full.community.post.controller;

import jakarta.validation.Valid;
import ktb3.full.community.auth.resolver.LoginUser;
import ktb3.full.community.common.response.ApiResponse;
import ktb3.full.community.post.dto.request.PostCreateRequest;
import ktb3.full.community.post.dto.request.PostUpdateRequest;
import ktb3.full.community.post.dto.response.PostResponse;
import ktb3.full.community.post.dto.response.PostScrollResponse;
import ktb3.full.community.post.repository.PostRepository;
import ktb3.full.community.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<PostScrollResponse>> list(
            @RequestParam(required = false) Long cursor
    ) {
        PostScrollResponse posts = postService.scroll(cursor);
        return ResponseEntity.ok(ApiResponse.ok("posts loaded successfully",posts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> get(@PathVariable Long id) {
        PostResponse post = postService.getAndIncreaseView(id);
        return ResponseEntity.ok(ApiResponse.ok("post loaded successfully",post));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> create(
            @Valid @RequestBody PostCreateRequest dto,
            @LoginUser Long userId) {
        PostResponse post = postService.create(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("post created successfully", post));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest dto,
            @LoginUser Long userId) {
        PostResponse post = postService.update(id, userId, dto);
        return ResponseEntity.ok(ApiResponse.ok("post updated", post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            @LoginUser Long userId) {
        postService.delete(id, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }







}
