package com.ip.ddangddangddang.domain.auction.service;

import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.auction.dto.response.AuctionListResponseDto;
import com.ip.ddangddangddang.domain.auction.dto.response.AuctionResponseDto;
import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import com.ip.ddangddangddang.domain.auction.repository.AuctionRepository;
import com.ip.ddangddangddang.domain.file.entity.File;
import com.ip.ddangddangddang.domain.file.service.FileService;
import com.ip.ddangddangddang.domain.town.service.TownService;
import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.domain.user.service.UserService;
import com.ip.ddangddangddang.global.exception.customedExceptions.InvalidAuthorityException;
import com.ip.ddangddangddang.global.mail.MailService;
import com.ip.ddangddangddang.global.redis.CacheService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    private final TownService townService;
    private final MailService mailService;
    private final CacheService cacheService;

    @CacheEvict(value = "auctions", allEntries = true, cacheManager = "cacheManager")
    @Transactional
    public void createAuction(AuctionRequestDto auctionRequestDto, Long userId) {
        User user = userService.findUserById(userId);
        File file = fileService.findFileById(auctionRequestDto.getFileId());

        if (!file.getUser().equals(user)) {
            throw new InvalidAuthorityException("파일에 대한 권한이 없습니다.");
        }

        Auction auction = auctionRepository.save(new Auction(auctionRequestDto, user, file));
        cacheService.setAuctionExpiredKey(auction.getId());
    }

    @CacheEvict(value = "auctions", allEntries = true, cacheManager = "cacheManager")
    @Transactional
    public void deleteAuction(Long auctionId, Long userId) {
        Auction auction = findAuctionById(auctionId);

        if (!userId.equals(auction.getUser().getId())) {
            throw new InvalidAuthorityException("경매에 대한 권한이 없습니다.");
        }

        fileService.delete(auction.getFile());
        auctionRepository.delete(auction);
    }

    @Caching(evict = {
        @CacheEvict(value = "auction", key = "#auctionId", cacheManager = "cacheManager"),
        @CacheEvict(value = "auctions", allEntries = true, cacheManager = "cacheManager")
    })
    @Transactional
    public void updateStatusToHold(Long auctionId) {
        log.info("경매 기한 만료, auctionId : " + auctionId);
        Auction auction = findAuctionById(auctionId);
        auction.updateStatusToHold();

        mailService.sendMail(
            auction.getUser().getEmail(),
            auction.getUser().getNickname(),
            auction.getBuyerId(),
            auction.getTitle(),
            auction.getPrice()
        );
    }

    @Caching(evict = {
        @CacheEvict(value = "auction", key = "#auctionId", cacheManager = "cacheManager"),
        @CacheEvict(value = "auctions", allEntries = true, cacheManager = "cacheManager")
    })
    @Transactional
    public void updateStatusToComplete(Long auctionId, Long userId) {
        Auction auction = findAuctionById(auctionId);

        if (!auction.getUser().getId().equals(userId)) {
            throw new InvalidAuthorityException("경매에 대한 권한이 없습니다.");
        }

        auction.updateStatusToComplete();
    }

    @CacheEvict(value = "auction", key = "#auction.getId()", cacheManager = "cacheManager")
    @Transactional
    public void updateBid(Auction auction, Long price, Long buyerId) {
        auction.updateBid(price, buyerId);
    }

//    @Cacheable(value = "auctions", cacheManager = "cacheManager")
    public List<AuctionListResponseDto> getAuctions(
        Long userId,
        StatusEnum status,
        String title
    ) {
        User user = userService.findUserById(userId);
        List<Long> townList = user.getTown().getNeighborIdList();

        return auctionRepository.findAllByFilters(townList, status, title);
    }

    @Cacheable(value = "auction", key = "#auctionId", cacheManager = "cacheManager")
    public AuctionResponseDto getAuction(Long auctionId) {
        Auction auction = findAuctionById(auctionId);
        String townName = townService.findNameById(auction.getTownId());

        if (auction.getBuyerId() != null) {
            String buyerNickname = userService
                .findUserById(auction.getBuyerId())
                .getNickname();

            return new AuctionResponseDto(
                auction,
                townName,
                buyerNickname,
                auction.getFile().getFilePath()
            );
        }

        return new AuctionResponseDto(
            auction,
            townName,
            null,
            auction.getFile().getFilePath()
        );
    }

    public Slice<AuctionListResponseDto> getMyAuctions(Long userId, Pageable pageable) {
        return auctionRepository.findAuctionsByUserId(userId, pageable)
            .map(auction -> new AuctionListResponseDto(
                auction.getId(),
                auction.getTitle(),
                auction.getStatus(),
                auction.getUser().getNickname(),
                auction.getFinishedAt(),
                auction.getFile().getFilePath(),
                auction.getPrice()
            ));
    }

    public Slice<AuctionListResponseDto> getMyBids(Long userId, Pageable pageable) {
        return auctionRepository.findBidsByBuyerId(userId, pageable)
            .map(auction -> new AuctionListResponseDto(
                auction.getId(),
                auction.getTitle(),
                auction.getStatus(),
                auction.getUser().getNickname(),
                auction.getFinishedAt(),
                auction.getFile().getFilePath(),
                auction.getPrice()
            ));
    }

    public Auction findAuctionById(Long auctionId) {
        return auctionRepository.findById(auctionId).orElseThrow(
            () -> new NullPointerException("게시글이 존재하지 않습니다.")
        );
    }

}
