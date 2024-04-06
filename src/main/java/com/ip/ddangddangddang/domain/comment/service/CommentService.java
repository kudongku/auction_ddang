package com.ip.ddangddangddang.domain.comment.service;

import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.service.AuctionService;
import com.ip.ddangddangddang.domain.comment.dto.request.CommentRequestDto;
import com.ip.ddangddangddang.domain.comment.entity.Comment;
import com.ip.ddangddangddang.domain.comment.repository.CommentRepository;
import com.ip.ddangddangddang.domain.result.entity.Result;
import com.ip.ddangddangddang.domain.result.service.ResultService;
import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.domain.user.service.UserService;
import com.ip.ddangddangddang.global.exception.custom.CustomUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final AuctionService auctionService;
    private final ResultService resultService;

    @Transactional
    public void createComment(Long auctionId, CommentRequestDto requestDto, Long userId) {
        User user = userService.getUser(userId);
        Auction auction = auctionService.findAuctionOrElseThrow(auctionId);
        Result result = resultService.findResultOrElseThrow(auctionId);

        Long sellerId = auction.getUser().getId();
        Long buyerId = result.getUser().getId();

        validateUser(sellerId, buyerId, userId);
        Comment comment = new Comment(requestDto.getContent(), user, auction);
        commentRepository.save(comment);
    }

    private boolean isSellerOfAuction(Long sellerId, Long userId) {
        return sellerId.equals(userId);
    }

    private boolean isBuyerOfAuction(Long buyerId, Long userId) {
        return buyerId.equals(userId);
    }

    private void validateUser(Long sellerId, Long buyerId, Long userId) {
        if (!isSellerOfAuction(sellerId, userId)
            || !isBuyerOfAuction(buyerId, userId)) {
            throw new CustomUserException("댓글 작성 권한이 없습니다.");
        }
    }

}
