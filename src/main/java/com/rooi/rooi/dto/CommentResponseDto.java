package com.rooi.rooi.dto;

import com.rooi.rooi.entity.Comment;
import com.rooi.rooi.entity.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto extends Timestamped {
    private Long id;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getComment_id();
        this.username = comment.getUser().getUsername();
        this.content = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
