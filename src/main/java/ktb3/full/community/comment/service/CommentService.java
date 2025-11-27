package ktb3.full.community.comment.service;

import ktb3.full.community.comment.dto.response.CommentScrollResponse;
import ktb3.full.community.comment.repository.CommentRepository;
import ktb3.full.community.comment.domain.Comment;
import ktb3.full.community.comment.dto.request.CommentCreateRequest;
import ktb3.full.community.comment.dto.request.CommentUpdateRequest;
import ktb3.full.community.comment.dto.response.CommentResponse;
import ktb3.full.community.common.exception.ErrorDetail;
import ktb3.full.community.common.exception.custom.BadRequestException;
import ktb3.full.community.common.exception.custom.ForbiddenException;
import ktb3.full.community.common.exception.custom.NotFoundException;
import ktb3.full.community.common.pagination.ScrollPaginationCollection;
import ktb3.full.community.post.domain.Post;
import ktb3.full.community.post.repository.PostRepository;
import ktb3.full.community.user.domain.User;
import ktb3.full.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private static final int SCROLL_SIZE = 7;

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentScrollResponse scroll(Long postId, Long cursor) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new NotFoundException(List.of(
                        new ErrorDetail("postId", "post_not_found", "게시글을 찾을 수 없습니다.")
                )));
        List<Comment> comments = commentRepository.findScroll(postId, cursor, SCROLL_SIZE + 1);
        ScrollPaginationCollection<Comment> scrollPage =
                ScrollPaginationCollection.of(comments, SCROLL_SIZE);

        boolean last = scrollPage.isLastScroll();

        List<CommentResponse> items = scrollPage.getCurrentScrollItems().stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());

        Long nextCursor = null;
        if(!last && !items.isEmpty()) {
            Comment nextCursorComment = scrollPage.getNextCursor();
            nextCursor = nextCursorComment.getId();
        }
        return new CommentScrollResponse(items, nextCursor, last);
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
            post.decreaseCommentCount();
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
