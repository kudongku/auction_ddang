package com.ip.ddangddangddang.domain.bid.service;

import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.bid.dto.request.BidRequestDto;
import com.ip.ddangddangddang.domain.bid.entity.Bid;
import com.ip.ddangddangddang.domain.bid.repository.BidRepository;
import com.ip.ddangddangddang.global.aop.DistributedLock;
import com.ip.ddangddangddang.global.exception.custom.CustomBidException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BidService {

    private final BidRepository bidRepository;
    private final AuctionValidator auctionValidator;

    @Transactional
    @DistributedLock(value = "bidLock", waitTime = 50, leaseTime = 50, timeUnit = TimeUnit.MINUTES)
    public void createBid(Long auctionId, BidRequestDto requestDto, Long userId) {
        Auction auction = auctionValidator.findAuctionOrElseThrow(auctionId);

        Long seller = auction.getUser().getId();
        validateBidBySeller(seller, userId);

        bidRepository.save(new Bid(auctionId, userId, requestDto.getPrice()));
    }

    private void validateBidBySeller(Long sellerId, Long userId) {
        if (sellerId.equals(userId)) {
            throw new CustomBidException("본인의 게시글은 입찰을 할 수 없습니다.");
        }
    }

    public Bid getHighestBid(Long auctionId) {
        List<Bid> bids = bidRepository.findAllByAuctionId(auctionId);
        Long highestPrice = Long.MIN_VALUE;
        Bid highestBid = null;

        for (Bid bid : bids) {

            if (bid.getPrice() > highestPrice) {
                highestPrice = bid.getPrice();
                highestBid = bid;
            }

        }

        return highestBid;

    }
}
