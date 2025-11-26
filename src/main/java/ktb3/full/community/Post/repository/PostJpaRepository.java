package ktb3.full.community.Post.repository;

import ktb3.full.community.Post.domain.Post;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Profile("jpa")
public interface PostJpaRepository extends JpaRepository<Post, Long> {
    @Modifying
    @Query("update Post p set p.likeCount = p.likeCount + 1 where p.id = :postId")
    int increaseLikeCount(@Param("postId") Long postId);

    @Modifying
    @Query("update Post p set p.likeCount = case when p.likeCount > 0 then p.likeCount - 1 else 0 end where p.id = :postId")
    int decreaseLikeCount(@Param("postId") Long postId);

    List<Post> findAllByDeletedFalse();
    List<Post> findByDeletedFalseOrderByIdDesc(Pageable pageable);
    List<Post> findByIdLessThanAndDeletedFalseOrderByIdDesc(Long id, Pageable pageable);

}
