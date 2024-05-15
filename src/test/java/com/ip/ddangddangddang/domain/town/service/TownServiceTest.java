package com.ip.ddangddangddang.domain.town.service;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.ip.ddangddangddang.domain.town.repository.TownRepository;
import com.ip.ddangddangddang.domain.town.value.TownValue;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TownServiceTest implements TownValue {

    @InjectMocks
    private TownService townService;

    @Mock
    private TownRepository townRepository;

    @Nested
    @DisplayName("타운 이름으로 찾기")
    public class findTownByNameOrElseThrow_test {

        @Test
        @DisplayName("성공시")
        public void success() {
            given(townRepository.findByName(anyString()))
                .willReturn(Optional.of(TOWN_1));

            townService.findTownByNameOrElseThrow("서욽특별시 중구");
        }

        @Test
        @DisplayName("실패시")
        public void fail() {
            given(townRepository.findByName(anyString()))
                .willReturn(Optional.empty());

            assertThrows(
                NullPointerException.class,
                ()->townService.findTownByNameOrElseThrow("서욽특별시 중구"),
                "해당 동네가 없습니다."
            );
        }

    }

    @Nested
    @DisplayName("타운 아이디로 이름 찾기")
    public class findNameById_test {

        @Test
        @DisplayName("성공시")
        public void success() {
            given(townRepository.findById(anyLong()))
                .willReturn(Optional.of(TOWN_1));

            String townName = townService.findNameById(1L);

            assert townName.equals("서울특별시 중구");
        }

        @Test
        @DisplayName("실패시")
        public void fail() {
            given(townRepository.findById(anyLong()))
                .willReturn(Optional.empty());

            assertThrows(
                NullPointerException.class,
                ()->townService.findNameById(1L),
                "해당 동네가 없습니다."
            );
        }

    }
}