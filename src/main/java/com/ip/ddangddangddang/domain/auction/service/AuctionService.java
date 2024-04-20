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
import com.ip.ddangddangddang.global.exception.custom.CustomAuctionException;
import com.ip.ddangddangddang.global.mail.MailService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    private final TownService townService;
    private final MailService mailService;
    private final RedisTemplate<String, String> redisTemplate;
    private final CacheManager cacheManager;

    @Transactional
    public void createAuction(AuctionRequestDto requestDto,
        Long userId) { // Todo fileId 곂칠때 duplicated error
        User user = userService.findUserOrElseThrow(userId);
        File file = fileService.findFileOrElseThrow(requestDto.getFileId());

        if (!file.getUser().equals(user)) {
            throw new IllegalArgumentException("파일에 대한 권한이 없습니다.");
        }

        Auction auction = auctionRepository.save(new Auction(requestDto, user, file));

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String redisKey = "auctionId:" + auction.getId();
        // redisKey = "auctionId:1";

        operations.set(redisKey, "1");
        redisTemplate.expire(redisKey, 5, TimeUnit.HOURS);

        log.info("경매 등록, " + redisKey);
    }

    @Transactional
    public void deleteAuction(Long auctionId, Long userId) {
        Auction auction = validatedAuction(
            auctionId);

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
            Auction auction = validatedAuction(
                auctionId);
            auction.updateStatusToHold();

            Objects.requireNonNull(cacheManager.getCache("auction")).evict(auctionId);

            mailService.sendMail(
                auction.getUser().getEmail(),
                auction.getUser().getNickname(),
                auction.getBuyerId(),
                auction.getTitle(),
                auction.getPrice()
            );

        } else {
            throw new RuntimeException("redis 에러");
        }

    }

    @CacheEvict(value = "auction", key = "#auctionId", cacheManager = "cacheManager")
    @Transactional
    public AuctionUpdateResponseDto updateStatusToComplete(Long auctionId, Long userId) {
        Auction auction = validatedAuction(auctionId);

        if (!auction.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("사용자가 불일치");
        }

        auction.updateStatusToComplete();
        return new AuctionUpdateResponseDto(auction.getId(), auction.getTownId(),
            auction.getTitle(), auction.getContent(), auction.getPrice(), auction.getBuyerId(),
            auction.getStatusEnum(), auction.getFinishedAt());
    }

    @CacheEvict(value = "auction", key = "#auctionId", cacheManager = "cacheManager")
    @Transactional
    public AuctionUpdateResponseDto updateBid(Long auctionId, Long price, Long buyerId) {
        Auction auction = validatedAuction(auctionId);
        auction.updateBid(price, buyerId);
        return new AuctionUpdateResponseDto(auction.getId(), auction.getTownId(),
            auction.getTitle(), auction.getContent(), auction.getPrice(), auction.getBuyerId(),
            auction.getStatusEnum(), auction.getFinishedAt());
    }

    @Cacheable(value = "auctions", cacheManager = "cacheManager")
    public List<AuctionListResponseDto> getAuctions(
        Long userId,
        StatusEnum status,
        String title
    ) {
        User user = userService.findUserOrElseThrow(userId);
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
            buyerNickname = userService.findUserOrElseThrow(auction.getBuyerId()).getNickname();
        }

        return new AuctionResponseDto(auction, townName, buyerNickname,
            auction.getFile().getFilePath());
    }

    // TODO: 4/8/24 자신이 올린 옥션리스트 보기 getList
    public Slice<AuctionListResponseDto> getMyAuctions(Long userId, Pageable pageable) {
        return auctionRepository.
            findAuctionsByUserId(userId, pageable)
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
        return auctionRepository.findBidsByUserId(userId, pageable).map(
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
    private Auction validatedAuction(
        Long auctionId) {
        return auctionRepository.findById(auctionId).orElseThrow(
            () -> new CustomAuctionException("게시글이 존재하지 않습니다.")
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
