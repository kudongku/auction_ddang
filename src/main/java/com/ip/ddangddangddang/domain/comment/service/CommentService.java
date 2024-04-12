package com.ip.ddangddangddang.domain.comment.service;

import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.service.AuctionService;
import com.ip.ddangddangddang.domain.comment.dto.request.CommentCreateRequestDto;
import com.ip.ddangddangddang.domain.comment.dto.response.CommentReadResponseDto;
import com.ip.ddangddangddang.domain.comment.entity.Comment;
import com.ip.ddangddangddang.domain.comment.repository.CommentRepository;
import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.domain.user.service.UserService;
import com.ip.ddangddangddang.global.exception.custom.CustomUserException;
import java.util.List;
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

    @Transactional
    public void createComment(Long auctionId, CommentCreateRequestDto requestDto, Long userId) {
        User user = userService.findUserOrElseThrow(userId);
        Auction auction = auctionService.findAuctionOrElseThrow(auctionId);

        Long sellerId = auction.getUser().getId();
        Long buyerId = auction.getBuyerId();

        validateUser(sellerId, buyerId, userId);

        commentRepository.save(new Comment(requestDto.getContent(), user, auction));
    }

    public List<CommentReadResponseDto> getComments(Long auctionId, Long userId) {
        Auction auction = auctionService.findAuctionOrElseThrow(auctionId);

        Long sellerId = auction.getUser().getId();
        Long buyerId = auction.getBuyerId();

        validateUser(sellerId, buyerId, userId);

        return commentRepository.findAllByAuctionId(auctionId).stream()
            .map(CommentReadResponseDto::new)
            .toList();
    }

    private boolean isSellerOfAuction(Long sellerId, Long userId) {
        return userId.equals(sellerId);
    }

    private boolean isBuyerOfAuction(Long buyerId, Long userId) {
        return userId.equals(buyerId);
    }

    private void validateUser(Long sellerId, Long buyerId, Long userId) {
        if (!isSellerOfAuction(sellerId, userId)
            && !isBuyerOfAuction(buyerId, userId)) {
            throw new CustomUserException("댓글 권한이 없습니다.");
        }
    }

}
