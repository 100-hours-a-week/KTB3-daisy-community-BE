package ktb3.full.community.Comment.service;

import ktb3.full.community.Comment.repository.CommentRepository;
import ktb3.full.community.Comment.domain.Comment;
import ktb3.full.community.Comment.dto.request.CommentCreateRequest;
import ktb3.full.community.Comment.dto.request.CommentUpdateRequest;
import ktb3.full.community.Comment.dto.response.CommentResponse;
import ktb3.full.community.Common.exception.ErrorDetail;
import ktb3.full.community.Common.exception.custom.BadRequestException;
import ktb3.full.community.Common.exception.custom.ForbiddenException;
import ktb3.full.community.Common.exception.custom.NotFoundException;
import ktb3.full.community.Post.domain.Post;
import ktb3.full.community.Post.repository.PostRepository;
import ktb3.full.community.User.domain.User;
import ktb3.full.community.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<CommentResponse> list(Long postId, String sort, int limit) {
        if (limit == 0 || limit > 20) {
            throw new BadRequestException(List.of(
                    new ErrorDetail("limit", "invalid_value", "limit은 1 ~ 20 사이입니다.")
            ));
        }
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new NotFoundException(List.of(
                        new ErrorDetail("postId", "post_not_found", "게시글을 찾을 수 없습니다.")
                )));
        Comparator<Comment> comparator = CommentSort.of(sort).comparator;

        return commentRepository.findByPost_IdAndDeletedFalse(post.getId()).stream()
                .sorted(comparator)
                .limit(limit)
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse create(Long postId, Long userId, CommentCreateRequest dto) {
        User user = findUserBy(userId);
        if (postId == null || postId <= 0) {
            throw new BadRequestException(List.of(
                    new ErrorDetail("postId", "invalid_value", "유효하지 않은 게시글ID입니다.")
            ));
        }
        Post post = findPostBy(postId);
        Comment comment = dto.toEntity(user, post);
        Comment savedComment = commentRepository.save(comment);

        post.increaseCommentCount();
        return CommentResponse.from(savedComment);
    }

    @Transactional
    public CommentResponse update(Long commentId, Long userId, CommentUpdateRequest dto) {
        if (commentId == null || commentId <= 0) {
            throw new BadRequestException(List.of(
                    new ErrorDetail("postId", "invalid_value", "유효하지 않은 댓글ID입니다.")
            ));
        }

        Comment comment = findCommentBy(commentId);

        if (!comment.getUser().getId().equals(userId)) {
            throw new ForbiddenException(List.of(
                    new ErrorDetail("userId", "not_author", "작성자만 수정할 수 있습니다.")
            ));
        }
        comment.update(dto.getContent());
        return CommentResponse.from(comment);
    }

    @Transactional
    public void delete(Long commentId, Long userId) {
        if (commentId == null || commentId <= 0) {
            throw new BadRequestException(List.of(
                    new ErrorDetail("postId", "invalid_value", "유효하지 않은 댓글ID입니다.")
            ));
        }

        Comment comment = findCommentBy(commentId);

        if (!comment.getUser().getId().equals(userId)) {
            throw new ForbiddenException(List.of(
                    new ErrorDetail("userId", "not_author", "작성자만 삭제할 수 있습니다.")
            ));
        }
        if(!comment.isDeleted()) {
            comment.softDelete();

            Post post = comment.getPost();
            post.increaseCommentCount();
        }

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

    private Comment findCommentBy(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(List.of(
                        new ErrorDetail("commentId", "comment_not_found", "댓글을 찾을 수 없습니다.")
                )));
    }

}
