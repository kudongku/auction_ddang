package com.ip.ddangddangddang.domain.auction.repository;

import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface AuctionQueryRepository {

    Slice<Auction> findAllByFilters(List<Long> neighbor, StatusEnum status,
        String title, Pageable pageable);

    Slice<Auction> findAuctionsByUserId(Long userId, Pageable pageable);

    Slice<Auction> findBidsByUserId(Long userId, Pageable pageable);
}
