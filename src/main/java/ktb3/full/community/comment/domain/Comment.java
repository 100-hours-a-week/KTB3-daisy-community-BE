package ktb3.full.community.Comment.domain;

import jakarta.persistence.*;
import ktb3.full.community.Post.domain.Post;
import ktb3.full.community.User.domain.User;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
    private String content;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
    private boolean deleted;

    protected Comment() {}

    @Builder
    public Comment(User user, Post post, String content) {
        this.user = user;
        this.post = post;
        this.content = content;
        this.deleted = false;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public void update(String content) {
        this.content = content;
        this.updatedAt = Instant.now();
    }

    public void softDelete() {
        deleted = true;
    }
}
