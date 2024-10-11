package org.example.blogapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import org.example.blogapi.entity.CommentEntity;
import org.example.blogapi.repository.ICommentRepository;
import org.example.blogapi.repository.IPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentaryServiceImpl implements ICommentaryService{
    @Autowired
    private ICommentRepository commentRepository;

    @Autowired
    private IPostRepository postRepository;

    @Override
    public CommentEntity createComment(Long postId, CommentEntity comment) {
        val post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post not found"));

        comment.setPost(post);

        return commentRepository.save(comment);
    }

    @Override
    public CommentEntity getCommentById(Long commentId) {
        return null;
    }

    @Override
    public CommentEntity updateComment() {
        return null;
    }

    @Override
    public void deleteCommentById(Long commentId) {

    }
}
