package com.ip.ddangddangddang.domain.auction.repository;

import com.ip.ddangddangddang.domain.auction.entity.Auction;

public interface AuctionRepository {

    void save(Auction auction);

    void delete(Long auctionId, Long id);

}
