package com.ip.ddangddangddang.domain.town.townList;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TownListRepository {

    private final TownListJpaRepository townJpaRepository;

    public void saveAll(List<TownList> townList) {
        townJpaRepository.saveAll(townList);
    }

}
