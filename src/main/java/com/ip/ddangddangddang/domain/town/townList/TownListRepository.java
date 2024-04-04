package com.ip.ddangddangddang.domain.town.townList;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TownListRepository {

    private final TownListJpaRepository townListJpaRepository;

    public void saveAll(List<TownList> townList) {
        townListJpaRepository.saveAll(townList);
    }

    public List<TownList> findAll() {
        return townListJpaRepository.findAll().stream().toList();
    }

}
