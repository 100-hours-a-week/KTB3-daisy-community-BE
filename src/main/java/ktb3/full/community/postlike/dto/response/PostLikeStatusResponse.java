package ktb3.full.community.postlike.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostLikeStatusResponse {
    private long likeCount;
    boolean liked;

    public static PostLikeStatusResponse of(long likeCount, boolean liked) {
        return new PostLikeStatusResponse(likeCount, liked);
    }
}
