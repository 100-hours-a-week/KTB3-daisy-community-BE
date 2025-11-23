package ktb3.full.community.Comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import ktb3.full.community.Comment.domain.Comment;
import ktb3.full.community.Post.domain.Post;
import ktb3.full.community.User.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static ktb3.full.community.Common.exception.MessageConstants.NOT_NULL_CONTENT;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCreateRequest {
    @NotBlank(message = NOT_NULL_CONTENT)
    private String content;

    public Comment toEntity(User user, Post post) {
        return Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .build();
    }
}
