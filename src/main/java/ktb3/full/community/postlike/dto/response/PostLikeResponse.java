package ktb3.full.community.postlike.dto.response;

import ktb3.full.community.postlike.domain.PostLike;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostLikeResponse {
    private Long id;
    private Long postId;
    private Long userId;
    private Long likeCount;

    public static PostLikeResponse from(PostLike postLike, long likesCount) {
        return new PostLikeResponse(
                postLike.getId(),
                postLike.getPost().getId(),
                postLike.getUser().getId(),
                likesCount
        );
    }
}
