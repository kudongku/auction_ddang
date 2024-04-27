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
    private final CacheManager cacheManager;

    @CacheEvict(value = "auctions", allEntries = true ,cacheManager = "cacheManager")
    @Transactional
    public void createAuction(AuctionRequestDto requestDto, Long userId) { // Todo fileId 곂칠때 duplicated error
        User user = userService.getUserById(userId).orElseThrow(
            () -> new UserNotFoundException("회원이 존재하지 않습니다.")
        ); //  없는게 정상 로직일 수 있다 -> 없는게 정상로직일 때 옵셔널로 받아오고 있어야하는 로직은 orelsethrow를 써도 된다. 없을 게 정상로직으로 추가될 수 있으니 확장성 측면에서 옵셔널로 받는게 좋다
        File file = fileService.getFileById(
            requestDto.getFileId()).orElseThrow(() -> new FileNotFoundException(
            "없는 이미지 입니다."));

        if (!file.getUser().equals(user)) {
            throw new UserHasNotAuthorityToFileException("파일에 대한 권한이 없습니다.");
        }

        Auction auction = auctionRepository.save(new Auction(requestDto, user, file));
        cacheService.setAuctionExpiredKey(auction.getId());
    }

    @CacheEvict(value = "auctions", allEntries = true ,cacheManager = "cacheManager")
    @Transactional
    public void deleteAuction(Long auctionId, Long userId) {
        Auction auction = validatedAuction(
            auctionId);

        if (!userId.equals(auction.getUser().getId())) {
            throw new UserHasNotAuthorityToAuctionException("작성자가 아닙니다.");
        }

        fileService.delete(auction.getFile());
        auctionRepository.delete(auction);
    }

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

    @CacheEvict(value = "auction", allEntries = true , cacheManager = "cacheManager")
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

        return auctionRepository.findAllByFilters(townList, status,
                title).stream()
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

//    public Page<AuctionListResponseDto> getAuctionsByTitle(String title, Pageable pageable) {
//        if (title == null || title.isEmpty()) {
//            throw new IllegalArgumentException("제목을 찾을 수 없습니다.");
//        }
//
//        Long adjustedPageNumber = pageLimit(pageable);
//
//        return auctionRepository.findAllByTitle(title, pageable, adjustedPageNumber).map(
//            auction -> new AuctionListResponseDto(auction.getId(), auction.getTitle(),
//                auction.getStatusEnum(), auction.getFinishedAt())
//        );
//    }

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

    // TODO: 4/8/24 자신이 올린 옥션리스트 보기 getList
    public Slice<AuctionListResponseDto> getMyAuctions(Long userId, Pageable pageable) {
        return auctionRepository.findAuctionsByUserId(userId, pageable)
            .map( // page에는 .map이 내장되어있음
//                (Auction auction) -> {
//                    return new AuctionListResponseDto(auction.getId(), auction.getTitle(), // (의미적)한 문장이면 return 생략
//                        auction.getStatusEnum(), auction.getFinishedAt());
//                }
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

    // TODO: 4/8/24 자신이 입찰한(최고가를 부른 게시글) 게시글리스트 보기 getList
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

    // todo : OrElseThrow는 private - 다른 서비스에서 필요하지 않음 - 추가로 findAuctionOrElseThrow이게 아니라 validatedAuction이라고 합니다.
    // todo : 가져다 쓰는 건 getAuction에 검증로직은 해당 서비스에 다시 리팩토링 필요
    private Auction validatedAuction(Long auctionId) {
        return getAuctionById(auctionId).orElseThrow(
            () -> new AuctionNotFoundException("게시글이 존재하지 않습니다.")
        );
    }

    public Optional<Auction> getAuctionById(Long auctionId) {
        return auctionRepository.findById(auctionId);
    }

//    public Long pageLimit(Pageable pageable) {
//        long adjustedPageNumber = pageable.getPageNumber() - 1;
//        if (adjustedPageNumber < 0) {
//            throw new IllegalArgumentException("페이지의 넘버는 0보다 커야합니다.");
//        }
//        return adjustedPageNumber;
//    }
    //todo 삭제하기
}
