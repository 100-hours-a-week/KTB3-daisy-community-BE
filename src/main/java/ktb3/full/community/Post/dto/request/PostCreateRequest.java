package ktb3.full.community.Post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ktb3.full.community.Post.domain.Post;
import ktb3.full.community.User.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static ktb3.full.community.Common.exception.MessageConstants.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateRequest {
    @NotBlank(message = NOT_NULL_TITLE)
    @Size(min = 1, max = 26, message = TITLE_PATTERN)
    private String title;

    @NotBlank(message = NOT_NULL_CONTENT)
    private String content;

    private String image;

    public Post toEntity(User user) {
        return Post.builder()
                .user(user)
                .title(title)
                .content(content)
                .image(image)
                .build();
    }
}
