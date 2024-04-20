package com.ip.ddangddangddang.domain.file.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ip.ddangddangddang.domain.file.dto.request.FileCreateRequestDto;
import com.ip.ddangddangddang.domain.file.dto.response.FileCreateResponseDto;
import com.ip.ddangddangddang.domain.file.dto.response.FileReadResponseDto;
import com.ip.ddangddangddang.domain.file.service.FileService;
import com.ip.ddangddangddang.domain.town.entity.Town;
import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.global.config.WebSecurityConfig;
import com.ip.ddangddangddang.global.security.UserDetailsImpl;
import com.ip.ddangddangddang.mvc.MockSpringSecurityFilter;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@WebMvcTest(
    controllers = FileController.class,
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebSecurityConfig.class
        )
    }
)
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FileService fileService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity(new MockSpringSecurityFilter()))
            .build();
        this.mockUserSetup();
    }

    private void mockUserSetup() {
        Town town = new Town("test", new ArrayList<>());
        User user = new User(0L, "test@test.com", "testNick", "testPw", town);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(user);
        mockPrincipal = new UsernamePasswordAuthenticationToken(
            testUserDetails,
            "",
            testUserDetails.getAuthorities()
        );
    }

    @Test
    @DisplayName("이미지 업로드 테스트")
    public void testUploadImage() throws Exception {

        FileCreateRequestDto requestDto = new FileCreateRequestDto("imageName");
        MockMultipartFile auctionImage = new MockMultipartFile(
            "auctionImage",
            "test.jpg",
            MediaType.IMAGE_PNG_VALUE,
            "test data".getBytes()
        );

        String requestDtoJson = objectMapper.writeValueAsString(requestDto);

        when(fileService.upload(auctionImage, requestDto.getImageName(), 0L)).thenReturn(
            new FileCreateResponseDto(0L));

        // when - then
        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/v1/auctions/files")
                    .file(auctionImage)
                    .file(new MockMultipartFile(
                        "requestDto",
                        "",
                        "application/json",
                        requestDtoJson.getBytes(StandardCharsets.UTF_8))
                    )
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .principal(mockPrincipal)
            )
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("preSignedUrl 테스트")
    public void testGetPresignedUrl() throws Exception {
        Long fileId = 0L;
        String expectedUrl = "http://example.com/signed-url";

        when(fileService.getPresignedURL(fileId, 0L)).thenReturn(
            new FileReadResponseDto(fileId, expectedUrl));

        mockMvc.perform(get("/v1/auctions/files/{fileId}", fileId)
                .principal(mockPrincipal)
            )
            .andExpect(status().isOk())
            .andDo(print());
    }
}
