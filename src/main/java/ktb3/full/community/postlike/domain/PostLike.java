package ktb3.full.community.postlike.domain;

import jakarta.persistence.*;
import ktb3.full.community.post.domain.Post;
import ktb3.full.community.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Entity
@Table(name = "post_likes")
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @CreationTimestamp
    private Instant createdAt;

    protected PostLike(){}

    @Builder
    public PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}
