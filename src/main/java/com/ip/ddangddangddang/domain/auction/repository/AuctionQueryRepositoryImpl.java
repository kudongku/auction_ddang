package com.ip.ddangddangddang.domain.auction.repository;

import static com.ip.ddangddangddang.domain.auction.entity.QAuction.auction;

import com.ip.ddangddangddang.domain.auction.dto.response.AuctionListResponseDto;
import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class AuctionQueryRepositoryImpl implements AuctionQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AuctionListResponseDto> findAllByFilters(
        List<Long> neighbor,
        StatusEnum status,
        String title
    ) {
        return queryFactory.select(Projections.fields(AuctionListResponseDto.class,
                auction.id.as("auctionId"),
                auction.title,
                auction.status,
                auction.user.nickname,
                auction.finishedAt,
                auction.file.filePath,
                auction.price
            ))
            .from(auction)
            .where(auction.townId.in(neighbor),
                eqStatusAndContainsTitle(status, title))
            .orderBy(auction.createdAt.desc())
            .fetch();
    }

    @Override
    public Slice<Auction> findAuctionsByUserId(Long userId, Pageable pageable) {

        List<Auction> result = queryFactory.selectFrom(auction)
            .where(auction.user.id.eq(userId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(auction.createdAt.desc())
            .fetch();

        return new SliceImpl<>(result, pageable, hasNextPage(result, pageable.getPageSize()));
    }

    @Override
    public Slice<Auction> findBidsByBuyerId(Long userId, Pageable pageable) {

        List<Auction> result = queryFactory.selectFrom(auction)
            .where(auction.buyerId.isNotNull()
                .and(auction.buyerId.eq(userId)))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new SliceImpl<>(result, pageable, hasNextPage(result, pageable.getPageSize()));
    }

    private boolean hasNextPage(List<Auction> result, int pageSize) {
        return result.size() > pageSize;
    }

    private BooleanExpression eqStatus(StatusEnum status) {
        if (status == null) {
            return null;
        }
        return auction.status.eq(status);
    }

    private BooleanExpression containsTitle(String title) {
        if (title == null) {
            return null;
        }
        return auction.title.contains(title);
    }

    private BooleanExpression eqStatusAndContainsTitle(StatusEnum status, String title) {
        BooleanExpression resultStatus = eqStatus(status);
        BooleanExpression resultContainsTitle = containsTitle(title);
        if (resultStatus != null && resultContainsTitle != null) {
            return resultStatus.and(resultContainsTitle);
        }
        if (resultStatus != null) {
            return resultStatus;
        }
        return resultContainsTitle;
    }

}
