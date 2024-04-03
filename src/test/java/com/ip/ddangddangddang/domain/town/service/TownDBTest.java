package com.ip.ddangddangddang.domain.town.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class TownDBTest {

    @Autowired
    TownService townService;

    @Test
    @Rollback(false)
    @DisplayName("town DB에 저장")
    void saveTownListDB() throws JsonProcessingException {
        //given
        //when, then
        townService.createTown();
    }

}
