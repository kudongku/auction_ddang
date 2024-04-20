package com.ip.ddangddangddang.domain.bid.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import com.ip.ddangddangddang.domain.auction.service.AuctionService;
import com.ip.ddangddangddang.domain.bid.dto.event.BidEvent;
import com.ip.ddangddangddang.domain.bid.dto.request.BidRequestDto;
import com.ip.ddangddangddang.domain.bid.entity.Bid;
import com.ip.ddangddangddang.domain.bid.repository.BidRepository;
import com.ip.ddangddangddang.global.aop.DistributedLock;
import com.ip.ddangddangddang.global.exception.custom.CustomBidException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BidService {

    private final BidRepository bidRepository;
    private final AuctionService auctionService;
    private final BidEventPublisher bidEventPublisher;
    private final ObjectMapper objectMapper;

    @DistributedLock(value = "bidLock", waitTime = 50, leaseTime = 50, timeUnit = TimeUnit.MINUTES)
    public void createBid(Long auctionId, BidRequestDto requestDto, Long userId)
        throws JsonProcessingException {
        Optional<Auction> foundAuction = auctionService.getAuctionById(auctionId);
        Auction auction = validatedAuction(foundAuction);
        validateAuctionStatus(auction);

        Long seller = auction.getUser().getId();
        validateBidBySeller(seller, userId);

        validatePrice(auction.getPrice(), requestDto.getPrice());
        auctionService.updateBid(auctionId, requestDto.getPrice(), userId);

        Bid savedBid = bidRepository.save(new Bid(auctionId, userId, requestDto.getPrice()));

        BidEvent bidEvent = new BidEvent(
            savedBid.getId(),
            savedBid.getAuctionId(),
            savedBid.getUserId(),
            savedBid.getPrice());
        bidEventPublisher.publishEvent(auctionId, objectMapper.writeValueAsString(bidEvent));
    }

    private void validateAuctionStatus(Auction auction) {
        if (!auction.getStatusEnum().equals(StatusEnum.ON_SALE)) {
            throw new CustomBidException("입찰 기간이 종료되었습니다.");
        }
    }

    private void validateBidBySeller(Long sellerId, Long userId) {
        if (sellerId.equals(userId)) {
            throw new CustomBidException("본인의 게시글은 입찰을 할 수 없습니다.");
        }
    }

    private void validatePrice(Long auctionPrice, Long bidPrice) {
        if (auctionPrice >= bidPrice) {
            throw new CustomBidException("현재 가격보다 높은 가격을 입력해주세요.");
        }
    }

    private Auction validatedAuction(Optional<Auction> auction) {
        return auction.orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
    }

}
