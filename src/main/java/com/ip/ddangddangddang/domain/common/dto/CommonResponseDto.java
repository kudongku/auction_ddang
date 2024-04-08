package com.ip.ddangddangddang.domain.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponseDto<T> {

    private HttpStatus status;
    private String message;
    private T data;

    public static <T> ResponseEntity<CommonResponseDto<T>> of(
        HttpStatus status, String message, T data
    ) {
        return ResponseEntity.status(status).body(new CommonResponseDto<>(status, message, data));
    }
    // TODO: 4/8/24 제거?

}
