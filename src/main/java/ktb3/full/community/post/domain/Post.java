package ktb3.full.community.post.domain;

import jakarta.persistence.*;
import ktb3.full.community.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String title;
    private String content;
    private String image;
    private int viewCount;
    private int likeCount;
    private int commentCount;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
    private boolean deleted;

    protected Post() {}

    @Builder
    public Post(User user, String title, String content, String image) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.image = image;
        this.viewCount = 0;
        this.likeCount = 0;
        this.commentCount = 0;
        this.deleted = false;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public void update(String title, String content, String image) {
        this.title = title;
        this.content = content;
        this.image = image;
    }

    public void softDelete() {
        deleted = true;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }
    public void decreaseCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }


}
