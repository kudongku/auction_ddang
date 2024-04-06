package com.ip.ddangddangddang.domain.auction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.auction.dto.response.AuctionResponseDto;
import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.entity.File;
import com.ip.ddangddangddang.domain.auction.repository.AuctionRepository;
import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.domain.user.service.UserService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private final UserService userService;

    @Transactional
    public void createAuction(MultipartFile auctionImage, AuctionRequestDto requestDto, Long userId)
        throws IOException {
        File file = fileService.upload(auctionImage, requestDto.getObjectName());
        User user = userService.getUser(userId);
        auctionRepository.save(new Auction(requestDto, user, file));
    }

    @Transactional
    public void deleteAuction(Long auctionId, Long userId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(
            () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        if (!userId.equals(auction.getUser().getId())) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }
        fileService.delete(auction.getFileKeyName());
        auctionRepository.delete(auction);
    }

    public List<AuctionResponseDto> getAuctionList(Long userId) {
        User user = userService.getUser(userId);
        String townList = user.getTown().getNeighborIdList();

        ObjectMapper mapper = new ObjectMapper();
        List<Long> neighbor = new ArrayList<>();
        try {
            neighbor = mapper.readValue(townList, new TypeReference<List<Long>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<AuctionResponseDto> response = new ArrayList<>();
        for (Long id : neighbor) {
            List<Auction> auctionList = auctionRepository.findAllByTownId(id);
            for (Auction auction : auctionList) {
                response.add(new AuctionResponseDto(
                    auction,
                    fileService.getPresignedURL(auction.getFileKeyName())));
            }
        }
        return response;
    }
    
    public AuctionResponseDto getAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(
            () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        String preSignedURL = fileService.getPresignedURL(auction.getFileKeyName());
        return new AuctionResponseDto(auction, preSignedURL);
    }

    public void isExistAuction(Long auctionId) {
        auctionRepository.findById(auctionId);
    }

}
