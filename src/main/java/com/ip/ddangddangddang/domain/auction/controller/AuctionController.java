package com.ip.ddangddangddang.domain.auction.controller;


import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.auction.dto.response.AuctionResponseDto;
import com.ip.ddangddangddang.domain.auction.service.AuctionService;
import com.ip.ddangddangddang.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        auctionService.createAuction(requestDto, userDetails.getUserId());
    }

    @DeleteMapping("/{auctionId}")
    public void deleteAuction(
        @PathVariable Long auctionId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        auctionService.deleteAuction(auctionId, userDetails.getUserId());
    }

    @GetMapping
    public List<AuctionResponseDto> getAuctionList(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return auctionService.getAuctionList(userDetails.getUserId());
    }

    @GetMapping("/{auctionId}")
    public AuctionResponseDto getAuction(
        @PathVariable Long auctionId
    ) {
        return auctionService.getAuction(auctionId);
    }

    @PutMapping("/{auctionId}")
    public void completeAuction(
        @PathVariable Long auctionId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        auctionService.updateStatusToComplete(auctionId, userDetails.getUserId());
    }

    @GetMapping("/search")
    public Page<AuctionResponseDto> searchAuctions(
        @RequestParam String title,
        Pageable pageable
    ) {
        return auctionService.getAuctionsByTitle(title, pageable);
    }

}
