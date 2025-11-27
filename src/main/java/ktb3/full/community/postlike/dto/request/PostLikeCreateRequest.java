package ktb3.full.community.postlike.dto.request;

import ktb3.full.community.post.domain.Post;
import ktb3.full.community.postlike.domain.PostLike;
import ktb3.full.community.user.domain.User;

public class PostLikeCreateRequest {
    public PostLike toEntity(Post post, User user) {
        return PostLike.builder()
                .post(post)
                .user(user)
                .build();
    }
}
