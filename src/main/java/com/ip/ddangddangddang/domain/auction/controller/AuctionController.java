package com.ip.ddangddangddang.domain.auction.controller;


import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.auction.dto.response.AuctionListResponseDto;
import com.ip.ddangddangddang.domain.auction.dto.response.AuctionResponseDto;
import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import com.ip.ddangddangddang.domain.auction.service.AuctionService;
import com.ip.ddangddangddang.domain.common.dto.Response;
import com.ip.ddangddangddang.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public Response<Slice<AuctionListResponseDto>> getAuctions(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) StatusEnum status,
            @RequestParam(required = false) String title
    ) {
        Slice<AuctionListResponseDto> auctions = auctionService.getAuctions(userDetails.getUserId(), status, title, PageRequest.of(page, size));
        return Response.ok(auctions);
    }

    @GetMapping("/{auctionId}")
    public AuctionResponseDto getAuction(
        @PathVariable Long auctionId
    ) {
        return auctionService.getAuction(auctionId);
    }

    @PatchMapping("/{auctionId}")
    public void completeAuction(
        @PathVariable Long auctionId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        auctionService.updateStatusToComplete(auctionId, userDetails.getUserId());
    }

    @GetMapping("/myauctions")
    public Slice<AuctionListResponseDto> getMyAuctions(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        Pageable pageable
    ) {
        return auctionService.getMyAuctions(userDetails.getUserId(), pageable);
    }

    @GetMapping("/mybids")
    public Slice<AuctionListResponseDto> getMyBids(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        Pageable pageable
    ) {
        return auctionService.getMyBids(userDetails.getUserId(), pageable);
    }

}
