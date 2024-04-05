package com.ip.ddangddangddang.domain.auction.service;

import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.auction.dto.response.AuctionResponseDto;
import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.entity.File;
import com.ip.ddangddangddang.domain.auction.repository.AuctionRepository;
import com.ip.ddangddangddang.domain.user.entity.User;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuctionService {

    private final FileService fileService;
    private final AuctionRepository auctionRepository;

    @Transactional
    public void createAuction(MultipartFile auctionImage, AuctionRequestDto requestDto, User user)
        throws IOException {
        File file = fileService.upload(auctionImage, requestDto.getObjectName());
        auctionRepository.save(new Auction(requestDto, user, file));
    }

    @Transactional
    public void deleteAuction(Long auctionId, User user) {
        Auction auction = auctionRepository.findById(auctionId);
        fileService.delete(auction.getFileKeyName());
        auctionRepository.delete(auctionId, user.getId());
    }

    @Transactional(readOnly = true)
    public AuctionResponseDto getAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId);
        String preSignedURL = fileService.getPresignedURL(auction.getFileKeyName());
        return new AuctionResponseDto(auction, preSignedURL);
    }
}
