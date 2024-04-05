package com.ip.ddangddangddang.domain.auction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.auction.dto.response.AuctionResponseDto;
import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.model.AuctionModel;
import com.ip.ddangddangddang.domain.auction.repository.AuctionRepository;
import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.domain.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserService userService;

    @Transactional
    public void createAuction(AuctionRequestDto requestDto, User u) {
        User user = userService.getUser(u.getId());
        auctionRepository.save(new Auction(requestDto, user));
    }

    @Transactional
    public void deleteAuction(Long auctionId, User user) {
        auctionRepository.delete(auctionId, user.getId());
    }

    public List<AuctionResponseDto> getAuctionList(User u) {
        //User u = UserDetails의 User 객체이기 때문에 Id외의 값을 가지고 있지 않음
        User user = userService.getUser(u.getId());
        String townList = user.getTown().getNeighborIdList();

        ObjectMapper mapper = new ObjectMapper();
        List<Long> neighbor = new ArrayList<>();
        try {
            neighbor = mapper.readValue(townList, new TypeReference<List<Long>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<AuctionResponseDto> response = new ArrayList<>();
        for (Long id : neighbor) {
            List<AuctionModel> modelList = auctionRepository.findAllByTownId(id);
            for (AuctionModel auctionModel : modelList) {
                response.add(new AuctionResponseDto(auctionModel));
            }
        }
        return response;
    }

    public void isExistAuction(Long auctionId) {
        auctionRepository.findById(auctionId);
    }

}
