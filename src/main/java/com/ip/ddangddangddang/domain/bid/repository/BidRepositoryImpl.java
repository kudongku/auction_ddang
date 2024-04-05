package com.ip.ddangddangddang.domain.bid.repository;

import com.ip.ddangddangddang.domain.bid.entity.Bid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BidRepositoryImpl implements BidRepository {

    private final BidJpaRepository bidJpaRepository;

    @Override
    public void save(Bid bid) {
        bidJpaRepository.save(bid);
    }

}
