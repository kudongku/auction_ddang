package com.ip.ddangddangddang.domain.auction.repository;

import com.ip.ddangddangddang.domain.auction.entity.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuctionQueryRepository {

    Page<Auction> findAllByTitle(String title, Pageable pageable);
}
