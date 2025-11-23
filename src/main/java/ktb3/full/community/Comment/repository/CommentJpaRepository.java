package ktb3.full.community.Comment.repository;

import ktb3.full.community.Comment.domain.Comment;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Profile("jpa")
public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost_IdOrderByIdDesc(Long postId);
    List<Comment> findAllByPost_IdAndDeletedFalse(Long id);

}
