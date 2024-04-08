package com.ip.ddangddangddang.domain.bid.repository;

import com.ip.ddangddangddang.domain.bid.entity.Bid;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findAllByAuctionId(Long auctionId);
}
