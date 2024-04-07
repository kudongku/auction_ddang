package com.ip.ddangddangddang.domain.auction.repository;

import static com.ip.ddangddangddang.domain.auction.entity.QAuction.auction;

import com.ip.ddangddangddang.domain.auction.entity.Auction;
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
    public Page<Auction> findAllByTitle(String title, Pageable pageable) {

        List<Auction> result = queryFactory.selectFrom(auction)
            .where(auction.title.contains(title).and(auction.finishedAt.isNull()))
            .orderBy(auction.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

//        List<Auction> auctionList = result.fetch();

        JPAQuery<Long> count = queryFactory.select(auction.count())
            .from(auction);

        return PageableExecutionUtils.getPage(result, pageable, count::fetchOne);

    }

}
