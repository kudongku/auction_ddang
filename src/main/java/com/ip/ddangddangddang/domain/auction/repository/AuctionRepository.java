package com.ip.ddangddangddang.domain.auction.repository;

import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.model.AuctionModel;
import java.util.List;

public interface AuctionRepository {

    void save(Auction auction);

    void delete(Long auctionId, Long id);

    Auction findById(Long auctionId);

    List<AuctionModel> findAllByTownId(Long id);

}
