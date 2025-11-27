package ktb3.full.community.comment.repository;

import ktb3.full.community.comment.domain.Comment;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Profile("jpa")
public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost_IdOrderByIdDesc(Long postId);
    List<Comment> findAllByPost_IdAndDeletedFalse(Long id);
    List<Comment> findByPost_IdAndDeletedFalseOrderByIdDesc(Long postId, Pageable pageable);
    List<Comment> findByPost_IdAndIdLessThanAndDeletedFalseOrderByIdDesc(Long postId ,Long id, Pageable pageable);
}