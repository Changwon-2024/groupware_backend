package com.groupware.project.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDTO<T> {

    @Schema(example = "성공 시 1, 실패 시 0을 반환합니다.")
    private int result;

    @Schema(example = "응답 메시지를 반환합니다.")
    private String message;

    private T data;

}
