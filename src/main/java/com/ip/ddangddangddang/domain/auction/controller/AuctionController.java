package com.ip.ddangddangddang.domain.auction.controller;

import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.auction.dto.response.AuctionListResponseDto;
import com.ip.ddangddangddang.domain.auction.dto.response.AuctionResponseDto;
import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import com.ip.ddangddangddang.domain.auction.service.AuctionService;
import com.ip.ddangddangddang.domain.common.dto.Response;
import com.ip.ddangddangddang.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public Response<List<AuctionListResponseDto>> getAuctions(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(required = false) StatusEnum status,
        @RequestParam(required = false) String title
    ) {
        List<AuctionListResponseDto> auctions = auctionService.getAuctions(userDetails.getUserId(),
            status, title);
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
    public Response<Slice<AuctionListResponseDto>> getMyAuctions(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        Pageable pageable
    ) {
        return Response.ok(auctionService.getMyAuctions(userDetails.getUserId(), pageable));
    }

    @GetMapping("/mybids")
    public Response<Slice<AuctionListResponseDto>> getMyBids(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        Pageable pageable
    ) {
        return Response.ok(auctionService.getMyBids(userDetails.getUserId(), pageable));
    }

}
