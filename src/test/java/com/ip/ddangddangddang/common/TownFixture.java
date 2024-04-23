package com.ip.ddangddangddang.common;

import com.ip.ddangddangddang.domain.town.entity.Town;
import java.util.List;

public interface TownFixture {

    Long TEST_TOWN1_ID = 1L;
    Long TEST_TOWN2_ID = 2L;
    Long TEST_ANOTHER_TOWN1_ID = 3L;
    Long TEST_ANOTHER_TOWN2_ID = 4L;
    String TEST_TOWN1_NAME = "testTownName1";
    String TEST_TOWN2_NAME = "testTownName2";
    String TEST_NON_EXISTENT_TOWN_NAME = "testNonExistentTownName";
    String TEST_ANOTHER_TOWN1_NAME = "testAnotherTownName1";
    String TEST_ANOTHER_TOWN2_NAME = "testAnotherTownName2";

    Town TEST_TOWN1 = new Town(TEST_TOWN1_ID, TEST_TOWN1_NAME,
        List.of(TEST_TOWN1_ID, TEST_TOWN2_ID));
    Town TEST_TOWN2 = new Town(TEST_TOWN2_ID, TEST_TOWN2_NAME,
        List.of(TEST_TOWN1_ID, TEST_TOWN2_ID));
    Town TEST_ANOTHER_TOWN1 = new Town(TEST_ANOTHER_TOWN1_ID, TEST_ANOTHER_TOWN1_NAME,
        List.of(TEST_ANOTHER_TOWN1_ID, TEST_ANOTHER_TOWN2_ID));
    Town TEST_ANOTHER_TOWN2 = new Town(TEST_ANOTHER_TOWN2_ID, TEST_ANOTHER_TOWN2_NAME,
        List.of(TEST_ANOTHER_TOWN1_ID, TEST_ANOTHER_TOWN2_ID));

}
