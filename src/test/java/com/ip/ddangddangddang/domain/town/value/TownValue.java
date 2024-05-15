package com.ip.ddangddangddang.domain.town.value;

import com.ip.ddangddangddang.domain.town.entity.Town;
import java.util.ArrayList;

public interface TownValue {
    Town TOWN_1 = new Town(
      1L,
      "서울특별시 중구",
        new ArrayList<>()
    );
}
