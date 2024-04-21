package com.ip.ddangddangddang.domain.file.controller;

import static com.ip.ddangddangddang.domain.file.values.FileValues.FILE_CREATE_REQUEST_DTO;
import static com.ip.ddangddangddang.domain.file.values.FileValues.FILE_CREATE_RESPONSE_DTO;
import static com.ip.ddangddangddang.domain.file.values.FileValues.FILE_ID;
import static com.ip.ddangddangddang.domain.file.values.FileValues.FILE_READ_RESPONSE_DTO;
import static com.ip.ddangddangddang.domain.file.values.FileValues.MOCK_MULTIPART_FILE;
import static com.ip.ddangddangddang.domain.file.values.FileValues.OBJECT_NAME;
import static com.ip.ddangddangddang.domain.file.values.FileValues.USER_DETAILS;
import static com.ip.ddangddangddang.domain.file.values.FileValues.USER_ID;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ip.ddangddangddang.domain.file.service.FileService;
import com.ip.ddangddangddang.global.config.WebSecurityConfig;
import com.ip.ddangddangddang.mvc.MockSpringSecurityFilter;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
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
        mockPrincipal = new UsernamePasswordAuthenticationToken(
            USER_DETAILS,
            "",
            USER_DETAILS.getAuthorities()
        );
    }

    @Test
    @DisplayName("이미지 업로드 테스트")
    public void testUploadImage() throws Exception {
        String requestDtoJson = objectMapper.writeValueAsString(FILE_CREATE_REQUEST_DTO);

        when(fileService.upload(MOCK_MULTIPART_FILE, OBJECT_NAME, 0L))
            .thenReturn(FILE_CREATE_RESPONSE_DTO);

        // when - then
        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/v1/auctions/files")
                    .file(MOCK_MULTIPART_FILE)
                    .file(new MockMultipartFile(
                        OBJECT_NAME,
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
        when(fileService.getPresignedURL(FILE_ID, USER_ID))
            .thenReturn(FILE_READ_RESPONSE_DTO);

        mockMvc.perform(get("/v1/auctions/files/{fileId}", FILE_ID)
                .principal(mockPrincipal)
            )
            .andExpect(status().isOk())
            .andDo(print());
    }
}
