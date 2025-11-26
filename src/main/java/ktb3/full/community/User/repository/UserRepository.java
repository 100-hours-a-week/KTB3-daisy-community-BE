package ktb3.full.community.User.repository;

import ktb3.full.community.User.domain.User;

import java.util.Optional;

public interface UserRepository {
    long count();
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    //List<User> findAll();

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

}
