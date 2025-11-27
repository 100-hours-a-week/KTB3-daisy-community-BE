package ktb3.full.community.post.dto.response;

import ktb3.full.community.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private Long userId;
    private String nickname;
    private String profileImage;
    private String title;
    private String content;
    private int viewCount;
    private int likeCount;
    private int commentCount;
    private Instant createdAt;
    private Instant updatedAt;

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getUser().getId(),
                post.getUser().getNickname(),
                post.getUser().getProfileImage(),
                post.getTitle(),
                post.getContent(),
                post.getViewCount(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
