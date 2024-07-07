package com.groupware.project.domain.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank(message = "아이디를 입력해 주세요.")
    @Schema(example = "아이디")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Schema(example = "비밀번호")
    private String password;

}
