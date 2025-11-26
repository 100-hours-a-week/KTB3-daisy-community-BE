package ktb3.full.community.Comment.repository;

import ktb3.full.community.Comment.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save (Comment comment);
    Optional<Comment> findById(Long id);
    List<Comment> findByPostId(Long id);
    List<Comment> findByPost_IdAndDeletedFalse(Long id);
    List<Comment> findScroll(Long postId, Long cursor, int size);
}