package org.example.blogapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import org.example.blogapi.entity.PostEntity;
import org.example.blogapi.repository.IPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements IPostService{

    @Autowired
    private IPostRepository postRepository;

    @Override
    public PostEntity createPost(PostEntity post) {
        return postRepository.save(post);
    }

    @Override
    public List<PostEntity> getPosts() {
        return postRepository.findAll();
    }

    @Override
    public Page<PostEntity> getPosts(int page, int size, String sortBy, String sortDirection) {
        val s = Sort.by(sortBy);

        val sort = sortDirection.equals(Sort.Direction.ASC.name())? s.ascending() :s.descending();
        //Set page request
        val pageable = PageRequest.of(page - 1,size,sort);

        return postRepository.findAll(pageable);
    }

    @Override
    public PostEntity getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Post does not exists")
        );
    }

    @Override
    public PostEntity updatePost(PostEntity post) {
        getPostById(post.getId());

        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long id) {
        getPostById(id);

        postRepository.deleteById(id);
    }
}
