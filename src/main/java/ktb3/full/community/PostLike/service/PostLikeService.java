package ktb3.full.community.PostLike.service;

import ktb3.full.community.Common.exception.ErrorDetail;
import ktb3.full.community.Common.exception.custom.BadRequestException;
import ktb3.full.community.Common.exception.custom.ConflictException;
import ktb3.full.community.Common.exception.custom.NotFoundException;
import ktb3.full.community.Post.domain.Post;
import ktb3.full.community.Post.repository.PostJpaRepository;
import ktb3.full.community.Post.repository.PostRepository;
import ktb3.full.community.PostLike.domain.PostLike;
import ktb3.full.community.PostLike.dto.response.PostLikeResponse;
import ktb3.full.community.PostLike.dto.response.PostLikeStatusResponse;
import ktb3.full.community.PostLike.repository.PostLikeJpaRepository;
import ktb3.full.community.User.domain.User;
import ktb3.full.community.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostJpaRepository postJpaRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeJpaRepository postLikeJpaRepository;

    @Transactional
    public PostLikeStatusResponse getStatus(Long postId, Long userId) {
        if (postId == null || postId <=0) {
            throw new BadRequestException(List.of(
                    new ErrorDetail("postId", "invalid_value", "유효하지 않은 게시글ID입니다.")
            ));
        }
        findPostBy(postId);

        long likeCount = postLikeJpaRepository.countByPost_Id(postId);
        boolean liked = false;
        if (userId != null) {
            liked = postLikeJpaRepository.existsByUser_IdAndPost_Id(userId, postId);
        }
        return PostLikeStatusResponse.of(likeCount, liked);

    }
    @Transactional
    public PostLikeResponse like(Long postId, Long userId) {
        if (postId == null || postId <=0) {
            throw new BadRequestException(List.of(
                    new ErrorDetail("postId", "invalid_value", "유효하지 않은 게시글ID입니다.")
            ));
        }
        Post post = findPostBy(postId);
        User user = findUserBy(userId);
        if(postLikeJpaRepository.existsByUser_IdAndPost_Id(userId, postId)) {
            throw new ConflictException(List.of(
                    new ErrorDetail("userId", "already_liked", "이미 좋아요를 누른 사용자입니다.")
            ));
        }
        PostLike postLike = PostLike.builder()
                .post(post)
                .user(user)
                .build();
        PostLike savedPostLike = postLikeJpaRepository.save(postLike);
        postJpaRepository.increaseLikeCount(postId);
        long updatedLikeCount = postLikeJpaRepository.countByPost_Id(postId);

        return PostLikeResponse.from(savedPostLike, updatedLikeCount);
    }

    @Transactional
    public void unlike(Long postId, Long userId) {
        if (postId == null || postId <=0) {
            throw new BadRequestException(List.of(
                    new ErrorDetail("postId", "invalid_value", "유효하지 않은 게시글ID입니다.")
            ));
        }
        Post post = findPostBy(postId);
        User user = findUserBy(userId);
        boolean liked = postLikeJpaRepository.existsByUser_IdAndPost_Id(userId, postId);
        if(!liked) {
            throw new NotFoundException(List.of(
                    new ErrorDetail("userId", "not_liked", "아직 좋아요를 누르지 않았습니다.")
            ));
        }
        postLikeJpaRepository.deleteByUser_IdAndPost_Id(userId, postId);
        postJpaRepository.decreaseLikeCount(postId);
    }

    private User findUserBy(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(List.of(
                        new ErrorDetail("userId", "user_not_found", "사용자를 찾을 수 없습니다.")
                )));
    }

    private Post findPostBy(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(List.of(
                        new ErrorDetail("postId", "post_not_found", "게시글을 찾을 수 없습니다.")
                )));
    }



}
