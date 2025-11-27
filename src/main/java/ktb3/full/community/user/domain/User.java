package ktb3.full.community.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String email;
    private String password;
    private String nickname;
    private String profileImage;
    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;
    private boolean deleted;

    protected User() {}

    public User(String email, String password, String nickname, String profileImage) {
        this.role = Role.USER;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.deleted = false;
    }

    public void assignId(Long id){
        this.id = id;
    }

    public void update(String nickname, String profileImage){
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void softDelete() {
        this.deleted = true;
    }
}
