package com.groupware.project.domain.user.dto;

import lombok.Data;

@Data
public class UserInfoDTO {

    private String userKey;

    private String email;

    private String name;

    private String phoneNumber;

    private String address;

    private String birthDate;

    private String genderFlag;

    private String roleLevel;

    // TODO 2024-10-10 이미지 조회 로직 추가 예정

}
