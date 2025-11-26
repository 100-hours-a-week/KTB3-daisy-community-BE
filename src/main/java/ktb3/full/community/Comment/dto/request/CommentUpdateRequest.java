package ktb3.full.community.Comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static ktb3.full.community.Common.exception.MessageConstants.NOT_NULL_CONTENT;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateRequest {
    @NotBlank(message = NOT_NULL_CONTENT)
    private String content;
}
