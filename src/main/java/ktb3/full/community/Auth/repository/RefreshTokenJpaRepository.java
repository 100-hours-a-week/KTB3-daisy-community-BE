package ktb3.full.community.Auth.repository;

import ktb3.full.community.Auth.domain.RefreshToken;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Profile("jpa")
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);

    @Modifying
    @Query("delete from RefreshToken rt where rt.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

}
