package com.ip.ddangddangddang.domain.bid.service;

import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.repository.AuctionRepository;
import com.ip.ddangddangddang.global.exception.custom.CustomAuctionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuctionValidator {

    private final AuctionRepository auctionRepository;

    public Auction findAuctionOrElseThrow(Long auctionId) {
        return auctionRepository.findById(auctionId).orElseThrow(
            () -> new CustomAuctionException("게시글이 존재하지 않습니다.")
        );
    }
}
