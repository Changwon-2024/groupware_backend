package com.groupware.project.domain.login.mapper;

import com.groupware.project.global.jwt.dto.JwtResponseDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {

    // 아이디, 비밀번호 일치 여부
    Boolean isLoginInfoExist(String email, String password);

    // 이메일로 유저 키 조회
    String getUserKey(String email);

    // 유저 정보 조회
    JwtResponseDTO getUserInfo(String userKey);

}
