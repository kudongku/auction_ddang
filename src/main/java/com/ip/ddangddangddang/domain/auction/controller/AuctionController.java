package com.ip.ddangddangddang.domain.auction.controller;


import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.auction.dto.response.AuctionResponseDto;
import com.ip.ddangddangddang.domain.auction.service.AuctionService;
import com.ip.ddangddangddang.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/v1/auctions")
@RestController
public class AuctionController {

    private final AuctionService auctionService;

    @GetMapping("/{auctionId}")
    public AuctionResponseDto getAuction(
        @PathVariable Long auctionId
    ) {
        return auctionService.getAuction(auctionId);
    }

    @PostMapping
    public void createAuction(
        @RequestPart("auctionImage") MultipartFile auctionImage,
        @Valid @RequestPart("requestDto") AuctionRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        auctionService.createAuction(auctionImage, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/{auctionId}")
    public void deleteAuction(
        @PathVariable Long auctionId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        auctionService.deleteAuction(auctionId, userDetails.getUser());
    }

}
