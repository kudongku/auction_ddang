package com.ip.ddangddangddang.domain.auction.service;

import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.auction.dto.response.AuctionListResponseDto;
import com.ip.ddangddangddang.domain.auction.dto.response.AuctionResponseDto;
import com.ip.ddangddangddang.domain.auction.dto.response.AuctionUpdateResponseDto;
import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import com.ip.ddangddangddang.domain.auction.repository.AuctionRepository;
import com.ip.ddangddangddang.domain.file.entity.File;
import com.ip.ddangddangddang.domain.file.service.FileService;
import com.ip.ddangddangddang.domain.town.service.TownService;
import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.domain.user.service.UserService;
import com.ip.ddangddangddang.global.exception.custom.AuctionNotFoundException;
import com.ip.ddangddangddang.global.exception.custom.FileNotFoundException;
import com.ip.ddangddangddang.global.exception.custom.UserHasNotAuthorityToAuctionException;
import com.ip.ddangddangddang.global.exception.custom.UserHasNotAuthorityToFileException;
import com.ip.ddangddangddang.global.exception.custom.UserNotFoundException;
import com.ip.ddangddangddang.global.mail.MailService;
import com.ip.ddangddangddang.global.redis.CacheService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
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

    @CacheEvict(value = "auctions", allEntries = true ,cacheManager = "cacheManager")
    @Transactional
    public void createAuction(AuctionRequestDto requestDto, Long userId) {
        User user = userService.getUserById(userId).orElseThrow(
            () -> new UserNotFoundException("회원이 존재하지 않습니다.")
        );
        File file = fileService.getFileById(
            requestDto.getFileId()).orElseThrow(
                () -> new FileNotFoundException("없는 이미지 입니다.")
        );

        if (!file.getUser().equals(user)) {
            throw new UserHasNotAuthorityToFileException("파일에 대한 권한이 없습니다.");
        }

        Auction auction = auctionRepository.save(new Auction(requestDto, user, file));
        cacheService.setAuctionExpiredKey(auction.getId());
    }

    @CacheEvict(value = "auctions", allEntries = true ,cacheManager = "cacheManager")
    @Transactional
    public void deleteAuction(Long auctionId, Long userId) {
        Auction auction = validatedAuction(auctionId);

        if (!userId.equals(auction.getUser().getId())) {
            throw new UserHasNotAuthorityToAuctionException("작성자가 아닙니다.");
        }

        fileService.delete(auction.getFile());
        auctionRepository.delete(auction);
    }

    @Caching(evict = {
        @CacheEvict(value = "auction", key = "#auctionId", cacheManager = "cacheManager"),
        @CacheEvict(value = "auctions", allEntries = true, cacheManager = "cacheManager")
    })
    @Transactional
    public AuctionUpdateResponseDto updateStatusToHold(Long auctionId) {
        log.info("경매 기한 만료, auctionId : " + auctionId);
        Auction auction = validatedAuction(auctionId);
        auction.updateStatusToHold();

        mailService.sendMail(
            auction.getUser().getEmail(),
            auction.getUser().getNickname(),
            auction.getBuyerId(),
            auction.getTitle(),
            auction.getPrice()
        );

        return new AuctionUpdateResponseDto(
            auction.getId(),
            auction.getTownId(),
            auction.getTitle(),
            auction.getContent(),
            auction.getPrice(),
            auction.getBuyerId(),
            auction.getStatusEnum(),
            auction.getFinishedAt()
        );
    }

    @Caching(evict = {
        @CacheEvict(value = "auction", key = "#auctionId", cacheManager = "cacheManager"),
        @CacheEvict(value = "auctions", allEntries = true, cacheManager = "cacheManager")
    })
    @Transactional
    public AuctionUpdateResponseDto updateStatusToComplete(Long auctionId, Long userId) {
        Auction auction = validatedAuction(auctionId);

        if (!auction.getUser().getId().equals(userId)) {
            throw new UserHasNotAuthorityToAuctionException("사용자가 불일치");
        }

        auction.updateStatusToComplete();

        return new AuctionUpdateResponseDto(
            auction.getId(),
            auction.getTownId(),
            auction.getTitle(),
            auction.getContent(),
            auction.getPrice(),
            auction.getBuyerId(),
            auction.getStatusEnum(),
            auction.getFinishedAt()
        );
    }

    @CacheEvict(value = "auction", key = "#auctionId", cacheManager = "cacheManager")
    @Transactional
    public AuctionUpdateResponseDto updateBid(Long auctionId, Long price, Long buyerId) {
        Auction auction = validatedAuction(auctionId);
        auction.updateBid(price, buyerId);
        return new AuctionUpdateResponseDto(
            auction.getId(),
            auction.getTownId(),
            auction.getTitle(),
            auction.getContent(),
            auction.getPrice(),
            auction.getBuyerId(),
            auction.getStatusEnum(),
            auction.getFinishedAt()
        );
    }

    @Cacheable(value = "auctions", cacheManager = "cacheManager")
    public List<AuctionListResponseDto> getAuctions(
        Long userId,
        StatusEnum status,
        String title
    ) {
        User user = userService.getUserById(userId).orElseThrow(
            () -> new UserNotFoundException("회원이 존재하지 않습니다.")
        );

        List<Long> townList = user.getTown().getNeighborIdList();

        return auctionRepository.findAllByFilters(townList, status, title).stream()
            .map(auction ->
                new AuctionListResponseDto(
                    auction.getId(),
                    auction.getTitle(),
                    auction.getStatusEnum(),
                    auction.getUser().getNickname(),
                    auction.getFinishedAt(),
                    auction.getFile().getFilePath(),
                    auction.getPrice()
                )
            ).collect(Collectors.toList());
    }

    @Cacheable(value = "auction", key = "#auctionId", cacheManager = "cacheManager")
    public AuctionResponseDto getAuction(Long auctionId) {
        Auction auction = validatedAuction(auctionId);

        String townName = townService.findNameByIdOrElseThrow(auction.getTownId());

        String buyerNickname = "";

        if (auction.getBuyerId() != null) {
            buyerNickname = userService.getUserByIdOrElseThrow(auction.getBuyerId()).getNickname();
        }

        return new AuctionResponseDto(auction, townName, buyerNickname,
            auction.getFile().getFilePath());
    }

    public Slice<AuctionListResponseDto> getMyAuctions(Long userId, Pageable pageable) {
        return auctionRepository.findAuctionsByUserId(userId, pageable)
            .map(
                auction -> new AuctionListResponseDto(
                    auction.getId(),
                    auction.getTitle(),
                    auction.getStatusEnum(),
                    auction.getUser().getNickname(),
                    auction.getFinishedAt(),
                    auction.getFile().getFilePath(),
                    auction.getPrice()
                )
            );
    }

    public Slice<AuctionListResponseDto> getMyBids(Long userId, Pageable pageable) {
        return auctionRepository.findBidsByBuyerId(userId, pageable)
            .map(
                auction -> new AuctionListResponseDto(
                  auction.getId(),
                  auction.getTitle(),
                  auction.getStatusEnum(),
                  auction.getUser().getNickname(),
                  auction.getFinishedAt(),
                  auction.getFile().getFilePath(),
                  auction.getPrice()
                )
            );
    }

    private Auction validatedAuction(Long auctionId) {
        return getAuctionById(auctionId).orElseThrow(
            () -> new AuctionNotFoundException("게시글이 존재하지 않습니다.")
        );
    }

    public Optional<Auction> getAuctionById(Long auctionId) {
        return auctionRepository.findById(auctionId);
    }
}
