package com.groupware.project.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserModifyDTO {

    @NotBlank(message = "유효한 유저가 아니에요. 다시 확인해 주세요.")
    @Schema(example = "유저 고유 키")
    private String userKey;

    // 기존 비밀번호 일치 시 회원정보 수정 가능
    @NotBlank(message = "회원정보를 수정하기 위해서는 기존 비밀번호가 필요해요.")
    @Schema(example = "기존 비밀번호")
    private String oldPassword;

    @Schema(example = "신규 비밀번호")
    private String password;

    @Schema(example = "비밀번호 확인")
    private String passwordConfirm;

    @Schema(example = "전화번호")
    private String phoneNumber;

    @Schema(example = "주소")
    private String address;

    @Schema(example = "생일 (yyyy-mm-dd)")
    private String birthDate;

    @NotBlank(message = "유효한 성별이 아니에요. 다시 확인해 주세요.")
    @Schema(example = "성별 플래그 (M, F)")
    private String genderFlag;

}
