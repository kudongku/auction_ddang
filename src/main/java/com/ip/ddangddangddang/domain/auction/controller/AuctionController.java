package com.ip.ddangddangddang.domain.auction.controller;


import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.auction.service.AuctionService;
import com.ip.ddangddangddang.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/auctions")
@RestController
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping
    public void createAuction(
        @Valid @RequestBody AuctionRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        auctionService.createAuction(requestDto, userDetails.getUser());
    }

    @DeleteMapping("/{auctionId}")
    public void deleteAuction(
        @PathVariable Long auctionId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        auctionService.deleteAuction(auctionId, userDetails.getUser());
    }

}
