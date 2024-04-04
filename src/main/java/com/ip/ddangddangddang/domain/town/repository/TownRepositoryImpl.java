package com.ip.ddangddangddang.domain.town.repository;

import com.ip.ddangddangddang.domain.town.entity.Town;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TownRepositoryImpl implements TownRepository {

    private final TownJpaRepository townJpaRepository;

    @Override
    public void save(Town town) {
        townJpaRepository.save(town);
    }

    @Override
    public Town findById(Long townId) {
        return townJpaRepository.findById(townId).orElseThrow(
            () -> new IllegalArgumentException("해당 동네가 없습니다.")
        );
    }

}
