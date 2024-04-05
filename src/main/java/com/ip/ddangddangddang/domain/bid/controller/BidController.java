package com.ip.ddangddangddang.domain.bid.controller;

import com.ip.ddangddangddang.domain.bid.dto.request.BidRequestDto;
import com.ip.ddangddangddang.domain.bid.service.BidService;
import com.ip.ddangddangddang.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/auctions/{auctionId}/bids")
@RestController
public class BidController {

    private final BidService bidService;

    @PostMapping
    public void createBid(
        @PathVariable Long auctionId,
        @Valid @RequestBody BidRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        bidService.createBid(auctionId, requestDto, userDetails.getUser());
    }

}
