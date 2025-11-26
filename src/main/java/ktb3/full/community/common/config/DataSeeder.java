package ktb3.full.community.common.config;

import ktb3.full.community.comment.repository.CommentRepository;
import ktb3.full.community.comment.domain.Comment;
import ktb3.full.community.post.domain.Post;
import ktb3.full.community.post.repository.PostRepository;
import ktb3.full.community.user.domain.User;
import ktb3.full.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public void run(String... args) {
        User user1 = new User("test1@test.com", "123456aA!", "test1", null);
        User user2 = new User("test2@test.com", "123456bB!", "test2", null);
        userRepository.save(user1); // id 자동 채번됨
        userRepository.save(user2);

        Post post1 = new Post(user1.getId(), "첫 번째 게시글", "내용입니다1", "https://image.kr/p1.jpg");
        Post post2 = new Post(user1.getId(), "두 번째 게시글", "내용입니다2", null);
        Post post3 = new Post(user2.getId(), "세 번째 게시글", "내용입니다3", "https://image.kr/p2.jpg");

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        Comment comment1 = new Comment(post1.getId(), user2.getId(), "첫 번째 댓글");
        Comment comment2 = new Comment(post1.getId(), user1.getId(), "두 번째 댓글");
        Comment comment3 = new Comment(post3.getId(), user1.getId(), "세 번째 댓글");

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
    }
}
