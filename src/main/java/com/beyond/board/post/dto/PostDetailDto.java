package com.beyond.board.post.dto;


import com.beyond.board.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PostDetailDto {

    private Long id;
    private String title;
    private String contents;
    private String authorEmail;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostDetailDto fromEntity(Post post) {
        return PostDetailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .authorEmail(post.getAuthor().getEmail())
                .category(post.getCategory())
                .createdAt(post.getCreatedTime())
                .updatedAt(post.getUpdatedTime())
                .build();
    }
}