package com.ip.ddangddangddang.domain.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> { //기본적으로 제공하는 ResponseEntity 안 쓰고 dto로 반환

    private T data;

    public static <T> Response<T> ok(T data) {
        return Response.<T>builder()
            .data(data)
            .build();
    }

}
