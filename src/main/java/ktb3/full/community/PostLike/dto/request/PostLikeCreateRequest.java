package ktb3.full.community.PostLike.dto.request;

import ktb3.full.community.Post.domain.Post;
import ktb3.full.community.PostLike.domain.PostLike;
import ktb3.full.community.User.domain.User;

public class PostLikeCreateRequest {
    public PostLike toEntity(Post post, User user) {
        return PostLike.builder()
                .post(post)
                .user(user)
                .build();
    }
}
