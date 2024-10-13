package com.groupware.project.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserInfoDTO {

    @Schema(example = "유저 고유 키")
    private String userKey;

    @Schema(example = "이메일")
    private String email;

    @Schema(example = "이름")
    private String name;

    @Schema(example = "전화번호")
    private String phoneNumber;

    @Schema(example = "주소")
    private String address;

    @Schema(example = "생일 (yyyy-mm-dd)")
    private String birthDate;

    @Schema(example = "성별 플래그 (M, F)")
    private String genderFlag;

    @Schema(example = "권한 레벨 (0 ~ 3)")
    private String roleLevel;

    // TODO 2024-10-10 이미지 조회 로직 추가 예정

}
