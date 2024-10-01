package com.groupware.project.global.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class JwtResponseDTO {

    @Schema(example = "유저 이메일")
    private String email;

    @Schema(example = "유저명")
    private String name;

    @Schema(example = "유저 권한")
    private Integer roleLevel;

}
