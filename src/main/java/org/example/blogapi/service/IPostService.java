package org.example.blogapi.service;


import org.example.blogapi.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPostService {
    PostEntity createPost(PostEntity post);
    List<PostEntity> getPosts();
    Page<PostEntity> getPosts(int page, int size, String sortBy, String sortDirection);
    PostEntity getPostById(Long id);
    PostEntity updatePost(PostEntity post);
    void deletePost(Long id);
}
