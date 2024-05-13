package com.ip.ddangddangddang.domain.town.service;

import com.ip.ddangddangddang.domain.town.entity.Town;
import com.ip.ddangddangddang.domain.town.repository.TownRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TownService {

    private final TownRepository townRepository;

    @Transactional
    public Town findTownByNameOrElseThrow(String address) {
        return townRepository.findByName(address).orElseThrow(
            () -> new IllegalArgumentException("해당 동네가 없습니다.")
        );
    }

    public Optional<Town> findTownByName(String address) {
        return townRepository.findByName(address);
    }

    public String findNameByIdOrElseThrow(Long townId) {
        Town town = townRepository.findById(townId).orElseThrow(
            () -> new NoSuchElementException("해당 동네가 없습니다.")
        );

        return town.getName();
    }
}
