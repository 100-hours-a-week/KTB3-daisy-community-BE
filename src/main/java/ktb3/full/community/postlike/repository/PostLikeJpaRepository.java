package ktb3.full.community.postlike.repository;

import ktb3.full.community.postlike.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeJpaRepository extends JpaRepository<PostLike, Long> {
    boolean existsByUser_IdAndPost_Id(Long userId, Long postId);
    void deleteByUser_IdAndPost_Id(Long userId, Long postId);
    long countByPost_Id(Long postId);
}
