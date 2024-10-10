package com.groupware.project.domain.user.dto;

import lombok.Data;

@Data
public class UserModifyDTO {

    private String userKey;

    private String email;

    // 기존 비밀번호 일치 시 회원정보 수정 가능
    private String oldPassword;

    private String password;

    private String passwordConfirm;

    private String address;

    private String birthDate;

    private String genderFlag;

}
