package com.ip.ddangddangddang.domain.result.repository;

import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.result.entity.Result;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<Result, Long> {

    Optional<Result> findByAuction(Auction auction);
}
