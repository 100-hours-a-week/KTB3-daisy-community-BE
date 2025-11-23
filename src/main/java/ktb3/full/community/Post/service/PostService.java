package ktb3.full.community.Post.service;

import jakarta.transaction.Transactional;
import ktb3.full.community.Common.exception.ErrorDetail;
import ktb3.full.community.Common.exception.custom.BadRequestException;
import ktb3.full.community.Common.exception.custom.ForbiddenException;
import ktb3.full.community.Common.exception.custom.NotFoundException;
import ktb3.full.community.Post.domain.Post;
import ktb3.full.community.Post.dto.request.PostCreateRequest;
import ktb3.full.community.Post.dto.request.PostUpdateRequest;
import ktb3.full.community.Post.dto.response.PostResponse;
import ktb3.full.community.Post.repository.PostRepository;
import ktb3.full.community.User.domain.User;
import ktb3.full.community.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<PostResponse> list(String sort, int limit) {
        if (limit <= 0 || limit > 20) {
            throw new BadRequestException(List.of(
                    new ErrorDetail("limit", "invalid_value", "limit은 1 ~ 20 사이입니다.")
            ));
        }

        Comparator<Post> comparator = PostSort.of(sort).comparator;
        return postRepository.findAllByDeletedFalse().stream()
                .sorted(comparator)
                .limit(limit > 0 ? limit : 20)
                .map(PostResponse::from)
                .collect(Collectors.toList());
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
