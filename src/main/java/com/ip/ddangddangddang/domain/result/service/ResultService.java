package com.ip.ddangddangddang.domain.result.service;

import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.bid.entity.Bid;
import com.ip.ddangddangddang.domain.bid.service.BidService;
import com.ip.ddangddangddang.domain.result.entity.Result;
import com.ip.ddangddangddang.domain.result.repository.ResultRepository;
import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.domain.user.service.UserService;
import com.ip.ddangddangddang.global.exception.custom.CustomResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ResultService {

    private final ResultRepository resultRepository;
    private final BidService bidService;
    private final UserService userService;

    public Result findResultOrElseThrow(Long auctionId) {
        return resultRepository.findById(auctionId).orElseThrow(
            () -> new CustomResultException("결과가 존재하지 않습니다.")
        );
    }

    @Transactional
    public void createResult(Auction auction) {
        Bid highestBid = bidService.getHighestBid(auction.getId());

        if(highestBid == null){
            resultRepository.save(new Result(0L, null, auction));
        }else{
            User buyer = userService.findUserOrElseThrow(highestBid.getUserId());
            Result result = new Result(highestBid.getPrice(), buyer, auction);
            resultRepository.save(result);
        }

    }
}
