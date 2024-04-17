package com.ip.ddangddangddang.domain.auction.repository;

import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface AuctionQueryRepository {

    List<Auction> findAllByFilters(List<Long> neighbor, StatusEnum status,
        String title);

    Slice<Auction> findAuctionsByUserId(Long userId, Pageable pageable);

    Slice<Auction> findBidsByUserId(Long userId, Pageable pageable);

}
