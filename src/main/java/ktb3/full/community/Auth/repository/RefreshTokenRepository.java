package ktb3.full.community.Auth.repository;

import ktb3.full.community.Auth.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    void save(RefreshToken refreshToken);
    Optional<RefreshToken> findByToken(String token);
    void revoke(String token);
    void delete(String token);
    void deleteByUserId(Long userId);
}
