package com.ip.ddangddangddang.domain.auction.handler;

import com.ip.ddangddangddang.domain.auction.dto.request.AuctionKeyNotificationRequestDto;
import com.ip.ddangddangddang.domain.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionEventHandler {

    private final AuctionService auctionService;

    @EventListener
    public void AuctionKeyExpiredEvent(AuctionKeyNotificationRequestDto auctionKeyNotificationRequestDto){
        auctionService.updateStatusToHold(auctionKeyNotificationRequestDto.getMessage());
    }

}
