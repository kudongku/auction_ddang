package com.ip.ddangddangddang.domain.auction.repository;

import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.model.AuctionModel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AuctionRepositoryImpl implements AuctionRepository {

    private final AuctionJpaRepository jpaRepository;

    @Override
    public void save(Auction auction) {
        jpaRepository.save(auction);
    }

    @Override
    public void delete(Long auctionId, Long id) {
        Auction auction = findById(auctionId);
        if (!id.equals(auction.getUser().getId())) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }
        jpaRepository.delete(auction);
    }

    @Override
    public List<AuctionModel> findAllByTownId(Long id) {
        return jpaRepository.findAllByTownId(id).stream().map(Auction::toModel)
            .toList();
    }

    @Override
    public Auction findById(Long auctionId) {
        return jpaRepository.findById(auctionId).orElseThrow(
            () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
    }

}
