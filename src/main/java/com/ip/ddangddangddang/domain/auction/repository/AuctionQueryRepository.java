package com.ip.ddangddangddang.domain.auction.repository;

import com.ip.ddangddangddang.domain.auction.dto.response.AuctionListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuctionQueryRepository {

    Page<AuctionListResponseDto> findAllByTitle(String title, Pageable pageable);

    Page<AuctionListResponseDto> findAuctionsByUserId(Long userId, Pageable pageable);
}
