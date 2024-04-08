package com.ip.ddangddangddang.domain.comment.controller;

import com.ip.ddangddangddang.domain.comment.dto.request.CommentCreateRequestDto;
import com.ip.ddangddangddang.domain.comment.dto.response.CommentReadResponseDto;
import com.ip.ddangddangddang.domain.comment.service.CommentService;
import com.ip.ddangddangddang.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/auctions/{auctionId}/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public void createComment(
        @PathVariable Long auctionId,
        @RequestBody CommentCreateRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        commentService.createComment(auctionId, requestDto, userDetails.getUserId());
    }

    @GetMapping
    public List<CommentReadResponseDto> getComments(
        @PathVariable Long auctionId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return commentService.getComments(auctionId, userDetails.getUserId());
    }

}
