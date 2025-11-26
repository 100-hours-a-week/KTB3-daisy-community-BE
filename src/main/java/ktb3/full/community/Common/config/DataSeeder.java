package ktb3.full.community.Common.config;

import jakarta.transaction.Transactional;
import ktb3.full.community.Comment.repository.CommentRepository;
import ktb3.full.community.Comment.domain.Comment;
import ktb3.full.community.Post.domain.Post;
import ktb3.full.community.Post.repository.PostRepository;
import ktb3.full.community.User.domain.User;
import ktb3.full.community.User.repository.UserRepository;
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
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) return;
        User user1 = new User("test1@test.com", "123456aA!", "test1", null);
        User user2 = new User("test2@test.com", "123456bB!", "test2", null);
        userRepository.save(user1);
        userRepository.save(user2);

        Post post1 = new Post(user1, "첫 번째 게시글", "내용입니다1", "https://image.kr/p1.jpg");
        Post post2 = new Post(user1, "두 번째 게시글", "내용입니다2", null);
        Post post3 = new Post(user2, "세 번째 게시글", "내용입니다3", "https://image.kr/p2.jpg");

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        Comment comment1 = new Comment(user2, post1, "첫 번째 댓글");
        Comment comment2 = new Comment(user1, post1, "두 번째 댓글");
        Comment comment3 = new Comment(user1, post3, "세 번째 댓글");

        post1.increaseViewCount();
        post1.increaseViewCount();
        post3.increaseViewCount();

        post1.increaseCommentCount();
        post1.increaseCommentCount();
        post3.increaseCommentCount();


        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
    }
}
