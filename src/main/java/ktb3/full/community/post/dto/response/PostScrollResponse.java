package ktb3.full.community.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostScrollResponse {
    private final List<PostResponse> items;
    private final Long nextCursor;
    private final boolean last;



}
