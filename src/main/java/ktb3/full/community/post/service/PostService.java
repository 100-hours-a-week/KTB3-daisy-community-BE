package ktb3.full.community.post.service;

import jakarta.transaction.Transactional;
import ktb3.full.community.common.exception.ErrorDetail;
import ktb3.full.community.common.exception.custom.BadRequestException;
import ktb3.full.community.common.exception.custom.ForbiddenException;
import ktb3.full.community.common.exception.custom.NotFoundException;
import ktb3.full.community.common.pagination.ScrollPaginationCollection;
import ktb3.full.community.post.domain.Post;
import ktb3.full.community.post.dto.request.PostCreateRequest;
import ktb3.full.community.post.dto.request.PostUpdateRequest;
import ktb3.full.community.post.dto.response.PostResponse;
import ktb3.full.community.post.dto.response.PostScrollResponse;
import ktb3.full.community.post.repository.PostJpaRepository;
import ktb3.full.community.post.repository.PostRepository;
import ktb3.full.community.user.domain.User;
import ktb3.full.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private static final int SCROLL_SIZE = 7;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostJpaRepository postJpaRepository;

    @Transactional
    public PostScrollResponse scroll(Long cursor) {

        List<Post> posts = postRepository.findScroll(cursor, SCROLL_SIZE + 1);

        ScrollPaginationCollection<Post> scrollPage =
                ScrollPaginationCollection.of(posts, SCROLL_SIZE);

        boolean last = scrollPage.isLastScroll();

        List<PostResponse> items = scrollPage.getCurrentScrollItems().stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());

        Long nextCursor = null;
        if(!last && !items.isEmpty()) {
            Post nextCursorPost = scrollPage.getNextCursor();
            nextCursor = nextCursorPost.getId();
        }

        return new PostScrollResponse(items, nextCursor, last);
    }

    @Transactional
    public PostResponse getAndIncreaseView(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException(List.of(
                    new ErrorDetail("id", "invalid_value", "유효하지 않은 ID입니다.")
            ));
        }
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        List.of(new ErrorDetail("id", "post_not_found", "게시글을 찾을 수 없습니다."))
                ));
        post.increaseViewCount();
        postRepository.save(post);
        return PostResponse.from(post);
    }

    @Transactional
    public PostResponse create(Long userId, PostCreateRequest dto) {
        System.out.println(">>> PostService.create userId = " + userId);
        User user = findUserBy(userId);
        Post post = dto.toEntity(user);
        Post savedPost = postRepository.save(post);
        return PostResponse.from(savedPost);
    }

    @Transactional
    public PostResponse update(Long postId, Long userId, PostUpdateRequest dto) {
        if (postId == null || postId <= 0) {
            throw new BadRequestException(List.of(
                    new ErrorDetail("id", "invalid_value", "유효하지 않은 ID입니다.")
            ));
        }
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(
                        List.of(new ErrorDetail("id", "post_not_found", "게시글을 찾을 수 없습니다."))
                ));
        if(!post.getUser().getId().equals(userId)) {
            throw new ForbiddenException(List.of(
                    new ErrorDetail("userId", "not_author", "작성자만 수정할 수 있습니다.")
            ));
        }
        post.update(dto.getTitle(), dto.getContent(), dto.getImage());
        return PostResponse.from(post);
    }

    @Transactional
    public void delete(Long postId, Long userId) {
        if (postId == null || postId <= 0) {
            throw new BadRequestException(List.of(
                    new ErrorDetail("id", "invalid_value", "유효하지 않은 ID입니다.")
            ));
        }
        Post post = findPostBy(postId);
        if(!post.getUser().getId().equals(userId)) {
            throw new ForbiddenException(List.of(new ErrorDetail("userId", "not_author", "작성자만 삭제할 수 있습니다.")));
        }
        post.softDelete();
    }

    private User findUserBy(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(List.of(
                        new ErrorDetail("userId", "user_not_found", "사용자를 찾을 수 없습니다.")
                )));
    }

    private Post findPostBy(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(List.of(
                        new ErrorDetail("postId", "post_not_found", "게시글을 찾을 수 없습니다.")
                )));
    }
}
