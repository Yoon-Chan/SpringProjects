package com.chan.sns.service;

import com.chan.sns.exception.ErrorCode;
import com.chan.sns.exception.SnsApplicationException;
import com.chan.sns.model.Comment;
import com.chan.sns.model.Post;
import com.chan.sns.model.entity.CommentEntity;
import com.chan.sns.model.entity.LikeEntity;
import com.chan.sns.model.entity.PostEntity;
import com.chan.sns.model.entity.UserEntity;
import com.chan.sns.repository.CommentEntityRepository;
import com.chan.sns.repository.LikeEntityRepository;
import com.chan.sns.repository.PostEntityRepository;
import com.chan.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final CommentEntityRepository commentEntityRepository;

    @Transactional
    public void create(String title, String body, String userName) {
        //user find
        UserEntity userEntity = userEntityRepository
                .findByUserName(userName)
                .orElseThrow(
                        () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName))
                );
        //post save
        PostEntity saved = postEntityRepository.save(PostEntity.of(title, body, userEntity));

        //return
    }

    @Transactional
    public Post modify(String title, String body, String userName, Integer postId) {
        //user find
        UserEntity userEntity = userEntityRepository
                .findByUserName(userName)
                .orElseThrow(
                        () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName))
                );

        //post exist
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId))
        );

        //post permission
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(String userName, Integer postId) {
        //user find
        UserEntity userEntity = userEntityRepository
                .findByUserName(userName)
                .orElseThrow(
                        () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName))
                );

        //post exist
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId))
        );

        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, postId));
        }

        postEntityRepository.delete(postEntity);
    }

    public Page<Post> list(Pageable pageable) {
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String userName, Pageable pageable) {
        UserEntity userEntity = userEntityRepository
                .findByUserName(userName)
                .orElseThrow(
                        () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName))
                );
        return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }

    @Transactional
    public void like(Integer postId, String userName) {
        UserEntity userEntity = userEntityRepository
                .findByUserName(userName)
                .orElseThrow(
                        () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName))
                );

        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId))
        );

        //check liked -> throw
        likeEntityRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED, String.format("userName %s already like post %d", userName, postId));
        });

        //like save
        likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));
    }

    @Transactional
    public int likeCount(Integer postId) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId))
        );

        //check liked -> throw
//        List<LikeEntity> likeEntities = likeEntityRepository.findAllByPost(postEntity);

        //like save
        return likeEntityRepository.countByPost(postEntity);
    }

    @Transactional
    public void comment(Integer postId, String userName, String comment) {
        UserEntity userEntity = getUserOrException(userName);
        PostEntity postEntity = getPostOrException(postId);

        //comment save
        commentEntityRepository.save(CommentEntity.of(userEntity, postEntity, comment));
    }

    public Page<Comment> getComments(Integer postId, Pageable pageable) {
        PostEntity postEntity = getPostOrException(postId);
        return commentEntityRepository.findAllByPost(postEntity, pageable).map(Comment::fromEntity);
    }

    private PostEntity getPostOrException(Integer postId) {
        return postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId))
        );
    }

    private UserEntity getUserOrException(String userName) {
        return userEntityRepository
                .findByUserName(userName)
                .orElseThrow(
                        () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName))
                );
    }
}
