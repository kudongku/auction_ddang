package com.ip.ddangddangddang.domain.comment.dto.response;

import com.ip.ddangddangddang.domain.comment.entity.Comment;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentReadResponseDto {

    private String nickname;
    private String content;
    private LocalDateTime createdAt;

    public CommentReadResponseDto(Comment comment) {
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }

}
