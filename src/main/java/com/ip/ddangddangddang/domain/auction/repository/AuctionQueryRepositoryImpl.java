package com.ip.ddangddangddang.domain.auction.repository;

import static com.ip.ddangddangddang.domain.auction.entity.QAuction.auction;

import com.ip.ddangddangddang.domain.auction.dto.response.AuctionListResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class AuctionQueryRepositoryImpl implements AuctionQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AuctionListResponseDto> findAllByTitle(String title, Pageable pageable) {

        // todo : 페이지 예외처리 service or repository?
        long adjustedPageNumber = pageable.getPageNumber() - 1;
        if (adjustedPageNumber < 0) {
            throw new IllegalArgumentException("페이지의 넘버는 0보다 커야합니다.");
        }

        // todo : 페이지 이상의 값이 나왔을 때의 예외처리가 필요한가요?

        List<AuctionListResponseDto> result = queryFactory.select(
                Projections.fields(AuctionListResponseDto.class, auction.id, auction.title,
                    auction.statusEnum, auction.finishedAt))
            .from(auction)
            .where(auction.title.contains(title).and(auction.finishedAt.isNotNull()))
            .orderBy(auction.createdAt.desc())
            .offset(pageable.getPageSize() * adjustedPageNumber)
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> count = queryFactory.select(auction.count())
            .from(auction);

        return PageableExecutionUtils.getPage(result, pageable, count::fetchOne);

    }

    @Override
    public Page<AuctionListResponseDto> findAuctionsByUserId(Long userId, Pageable pageable) {

        long adjustedPageNumber = pageable.getPageNumber() - 1;
        if (adjustedPageNumber < 0) {
            throw new IllegalArgumentException("페이지의 넘버는 0보다 커야합니다.");
        }

        List<AuctionListResponseDto> result = queryFactory.select(
                Projections.fields(AuctionListResponseDto.class, auction.id, auction.title,
                    auction.statusEnum, auction.finishedAt))
            .from(auction)
            .where(auction.user.id.eq(userId))
            .offset(pageable.getOffset() * adjustedPageNumber)
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> count = queryFactory.select(auction.count())
            .from(auction);

        return PageableExecutionUtils.getPage(result, pageable, count::fetchOne);
    }

}
