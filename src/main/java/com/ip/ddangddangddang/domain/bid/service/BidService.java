package com.ip.ddangddangddang.domain.bid.service;

import com.ip.ddangddangddang.domain.auction.service.AuctionService;
import com.ip.ddangddangddang.domain.bid.dto.request.BidRequestDto;
import com.ip.ddangddangddang.domain.bid.entity.Bid;
import com.ip.ddangddangddang.domain.bid.repository.BidRepository;
import com.ip.ddangddangddang.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BidService {

    private final BidRepository bidRepository;
    private final AuctionService auctionService;

    @Transactional
    public void createBid(Long auctionId, BidRequestDto requestDto, User user) {
        auctionService.isExistAuction(auctionId);

        bidRepository.save(new Bid(auctionId, user.getId(), requestDto.getPrice()));
    }

}
