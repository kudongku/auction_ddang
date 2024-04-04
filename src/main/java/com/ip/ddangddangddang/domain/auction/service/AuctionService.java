package com.ip.ddangddangddang.domain.auction.service;

import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.repository.AuctionRepository;
import com.ip.ddangddangddang.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;

    @Transactional
    public void createAuction(AuctionRequestDto requestDto, User user) {
        auctionRepository.save(new Auction(requestDto, user));
    }

    @Transactional
    public void deleteAuction(Long auctionId, User user) {

        auctionRepository.delete(auctionId, user.getId());
    }

}
