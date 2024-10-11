package org.example.blogapi.dto;

import org.example.blogapi.entity.PostEntity;

import java.util.List;

public record PostResponse(
    List<PostEntity> content,
    int currentPage,
    Long totalItems,
    int totalPages,
    String sortBy,
    String sortDir
) {
}
