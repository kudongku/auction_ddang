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
import com.ip.ddangddangddang.global.exception.customedExceptions.InvalidBidException;
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
    public void createBid(
        Long auctionId,
        BidRequestDto requestDto,
        Long userId
    ) throws JsonProcessingException {

        Auction auction = auctionService.findAuctionById(auctionId);

        if (!auction.getStatus().equals(StatusEnum.ON_SALE)) {
            throw new InvalidBidException("입찰 기간이 종료되었습니다.");
        }

        if (auction.getUser().getId().equals(userId)) {
            throw new InvalidBidException("본인의 게시글은 입찰을 할 수 없습니다.");
        }

        if (auction.getPrice() >= requestDto.getPrice()) {
            throw new InvalidBidException("현재 가격보다 높은 가격을 입력해주세요.");
        }

        auctionService.updateBid(auction, requestDto.getPrice(), userId);

        Bid bid = new Bid(auctionId, userId, requestDto.getPrice());
        bidRepository.save(bid);

        BidEvent bidEvent = new BidEvent(
            bid.getId(),
            auctionId,
            userId,
            requestDto.getPrice()
        );
        bidEventPublisher.publishEvent(auctionId, objectMapper.writeValueAsString(bidEvent));
    }

}
