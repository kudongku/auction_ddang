package com.ip.ddangddangddang.domain.auction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.auction.dto.response.AuctionResponseDto;
import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.repository.AuctionRepository;
import com.ip.ddangddangddang.domain.file.entity.File;
import com.ip.ddangddangddang.domain.file.service.FileService;
import com.ip.ddangddangddang.domain.result.service.ResultService;
import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.domain.user.service.UserService;
import com.ip.ddangddangddang.global.exception.custom.CustomAuctionException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "AuctionService")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserService userService;
    private final FileService fileService;
    private final ResultService resultService;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void createAuction(AuctionRequestDto requestDto, Long userId) { // Todo fileId 곂칠때 duplicated error
        User user = userService.getUser(userId);
        File file = fileService.findFileOrElseThrow(requestDto.getFileId());

        if(!file.getUser().equals(user)){
            throw new IllegalArgumentException("파일에 대한 권한이 없습니다.");
        }

        Auction auction = auctionRepository.save(new Auction(requestDto, user, file));

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String redisKey = "auctionId:" + auction.getId();
        // redisKey = "auctionId:1";

        operations.set(redisKey, "1");
        redisTemplate.expire(redisKey, 60, TimeUnit.SECONDS);

        log.info("경매 등록, " + redisKey);
    }

    @Transactional
    public void deleteAuction(Long auctionId, Long userId) {
        Auction auction = findAuctionOrElseThrow(auctionId);

        if (!userId.equals(auction.getUser().getId())) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }

        fileService.delete(auction.getFile());
        auctionRepository.delete(auction);
    }

    @Transactional
    public void updateStatusToHold(String message) {

        if (message.startsWith("auctionId:")) {
            // message = "auctionId: 1", string
            // message.split(" ") = {"auctionId:", "1"}, Array<String>
            // message.split(" ")[1] = "1", string
            // Long.parseLong(message.split(" ")[1]) = 1L, Long
            Long auctionId = Long.parseLong(message.split(":")[1]);
            log.info("경매 기한 만료, " + message);
            Auction auction = findAuctionOrElseThrow(auctionId);
            auction.updateStatusToHold();
            resultService.createResult(auction);
        } else {
            throw new RuntimeException("redis 에러");
        }

    }

    @Transactional
    public void updateStatusToComplete(Long auctionId, Long userId) {
        Auction auction = findAuctionOrElseThrow(auctionId);

        if(!auction.getUser().getId().equals(userId)){
            throw new IllegalArgumentException("사용자가 불일치");
        }

        auction.updateStatusToComplete();
    }

    public List<AuctionResponseDto> getAuctionList(Long userId) {
        // TODO: 4/8/24 판매중인것만보이게
        User user = userService.getUser(userId);
        String townList = user.getTown().getNeighborIdList();

        ObjectMapper mapper = new ObjectMapper();
        List<Long> neighbor = new ArrayList<>();
        try {
            neighbor = mapper.readValue(townList, new TypeReference<List<Long>>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JsonProcessingException exceptio ");
        }

        List<AuctionResponseDto> response = new ArrayList<>();
        for (Long id : neighbor) {
            List<Auction> auctionList = auctionRepository.findAllByTownId(id);
            for (Auction auction : auctionList) {
                response.add(new AuctionResponseDto(auction));
            }
        }
        return response;
    }

    public Page<AuctionResponseDto> getAuctionsByTitle(String title, Pageable pageable) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("제목을 찾을 수 없습니다.");
        }
        Page<Auction> auctionList = auctionRepository.findAllByTitle(title, pageable);
        return auctionList.map(AuctionResponseDto::new);
    }

    // TODO: 4/8/24 자신이 입찰한 게시글리스트 보기 getList

    // TODO: 4/8/24 자신이 올린 옥션리스트 보기 getList

    public AuctionResponseDto getAuction(Long auctionId) {
        Auction auction = findAuctionOrElseThrow(auctionId);

        return new AuctionResponseDto(auction);
    }

    public Auction findAuctionOrElseThrow(Long auctionId) {
        return auctionRepository.findById(auctionId).orElseThrow(
            () -> new CustomAuctionException("게시글이 존재하지 않습니다.")
        );
    }

}
