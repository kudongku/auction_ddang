package com.ip.ddangddangddang.domain.town.repository;

import com.ip.ddangddangddang.domain.town.entity.Town;

public interface TownRepository {

    void save(Town town);

    Town findById(Long townId);

}
