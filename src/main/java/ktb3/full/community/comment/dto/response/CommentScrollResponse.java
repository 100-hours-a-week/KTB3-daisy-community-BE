package ktb3.full.community.Comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommentScrollResponse {
    private final List<CommentResponse> items;
    private final Long nextCursor;
    private final boolean last;
}