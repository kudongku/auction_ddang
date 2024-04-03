package com.ip.ddangddangddang.domain.town.townList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class TownListDBTest {

    @Autowired
    TownListRepository townListRepository;

    @Test
    @Rollback(false)
    @DisplayName("townList 엑셀 파일 DB에 저장")
    void saveTownListDB() {
        //given
        ExcelReader excelReader = new ExcelReader(townListRepository);

        //when,then
        excelReader.createTownList();
    }

}
