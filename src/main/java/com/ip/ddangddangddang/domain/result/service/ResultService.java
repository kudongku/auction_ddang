package com.ip.ddangddangddang.domain.result.service;

import com.ip.ddangddangddang.domain.result.entity.Result;
import com.ip.ddangddangddang.domain.result.repository.ResultRepository;
import com.ip.ddangddangddang.global.exception.custom.CustomResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ResultService {

    private final ResultRepository resultRepository;

    public Result findResultOrElseThrow(Long auctionId) {
        return resultRepository.findById(auctionId).orElseThrow(
            () -> new CustomResultException("결과가 존재하지 않습니다.")
        );
    }

}
