package com.groupware.project.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserSignDTO {

    @Email(message = "올바른 이메일 형식이 아니에요.")
    @NotBlank(message = "가입할 이메일을 입력해 주세요.")
    @Schema(example = "이메일")
    private String email;

    @NotBlank(message = "가입할 비밀번호를 입력해 주세요.")
    @Schema(example = "비밀번호")
    private String password;

    @NotBlank(message = "비밀번호 확인란에 가입할 비밀번호를 입력해 주세요.")
    @Schema(example = "비밀번호 확인")
    private String passwordConfirm;

    @NotBlank(message = "이름을 입력해 주세요.")
    @Schema(example = "이름")
    private String name;

    @Schema(example = "전화번호")
    private String phoneNumber;

    @Schema(example = "주소")
    private String address;

    @Schema(example = "생일 (YYYY-MM-DD)")
    private String birthDate;

    @Schema(example = "성별 플래그 (F, M)")
    private String genderFlag;

}
