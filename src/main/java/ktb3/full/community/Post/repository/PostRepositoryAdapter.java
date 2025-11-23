package ktb3.full.community.Post.repository;

import ktb3.full.community.Post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
@RequiredArgsConstructor
public class PostRepositoryAdapter implements PostRepository {
    private final PostJpaRepository postJpaRepository;

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(post);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postJpaRepository.findById(id);
    }

    @Override
    public List<Post> findAll() {
        return postJpaRepository.findAll();
    }

    @Override
    public List<Post> findAllByDeletedFalse() {
        return postJpaRepository.findAllByDeletedFalse();
    }

}
