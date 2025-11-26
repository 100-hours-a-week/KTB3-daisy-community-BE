package ktb3.full.community.Comment.repository;

import ktb3.full.community.Comment.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
@RequiredArgsConstructor
public class CommentRepositoryAdapter implements CommentRepository {
    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Comment save(Comment comment) {
        return commentJpaRepository.save(comment);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentJpaRepository.findById(id);
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        return commentJpaRepository.findAllByPost_IdOrderByIdDesc(postId);
    }

    @Override
    public List<Comment> findByPost_IdAndDeletedFalse(Long id) {
        return commentJpaRepository.findAllByPost_IdAndDeletedFalse(id);
    }

    @Override
    public List<Comment> findScroll(Long postId, Long cursor, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        if(cursor == null) {
            return commentJpaRepository.findByPost_IdAndDeletedFalseOrderByIdDesc(postId, pageRequest);
        }
        return commentJpaRepository.findByPost_IdAndIdLessThanAndDeletedFalseOrderByIdDesc(postId, cursor, pageRequest);
    }
}