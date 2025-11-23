package ktb3.full.community.Post.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import ktb3.full.community.Common.response.ApiResponse;
import ktb3.full.community.Post.domain.Post;
import ktb3.full.community.Post.dto.request.PostCreateRequest;
import ktb3.full.community.Post.dto.request.PostUpdateRequest;
import ktb3.full.community.Post.dto.response.PostResponse;
import ktb3.full.community.Post.repository.PostRepository;
import ktb3.full.community.Post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> list(
            @RequestParam(defaultValue = "recent") String sort,
            @RequestParam(defaultValue = "20") int limit
    ) {
        List<PostResponse> posts = postService.list(sort, limit);
        return ResponseEntity.ok(ApiResponse.ok("posts loaded successfully",posts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> get(@PathVariable Long id) {
        PostResponse post = postService.getAndIncreaseView(id);
        return ResponseEntity.ok(ApiResponse.ok("post loaded successfully",post));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> create(@Valid @RequestBody PostCreateRequest dto, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        PostResponse post = postService.create(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("post created successfully",post));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> update(@PathVariable Long id, @Valid @RequestBody PostUpdateRequest dto, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        Post post = postService.update(id, userId, dto);
        return ResponseEntity.ok(ApiResponse.ok("post updated", PostResponse.from(post)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        postService.delete(id, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<ApiResponse<PostResponse>> like(
            @PathVariable Long id,
            HttpServletRequest req
    ) {
        Long userId = (Long) req.getAttribute("userId");
        PostResponse updated = postService.like(id, userId);
        return ResponseEntity.ok(ApiResponse.ok("post liked successfully", updated));
    }

    @DeleteMapping("/{id}/likes")
    public ResponseEntity<ApiResponse<PostResponse>> unlike(
            @PathVariable Long id,
            HttpServletRequest req
    ) {
        Long userId = (Long) req.getAttribute("userId");
        postService.unlike(id, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }






}
