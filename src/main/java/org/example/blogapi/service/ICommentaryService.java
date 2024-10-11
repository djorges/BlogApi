package org.example.blogapi.service;

import org.example.blogapi.entity.CommentEntity;

public interface ICommentaryService {
    CommentEntity createComment(Long postId, CommentEntity comment);
    CommentEntity getCommentById(Long commentId);
    CommentEntity updateComment();
    void deleteCommentById(Long commentId);
}
