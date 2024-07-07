package com.groupware.project.global.jwt.mapper;

import com.groupware.project.global.jwt.dto.JwtResponseDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JwtGlobalMapper {

    // 이메일로 유저 키 조회
    String getUserKey(String email);

    // 유저 키로 리프레시 토큰 값 조회
    String getTokenValue(String userKey);

    // 유저 정보 조회
    JwtResponseDTO getUserInfo(String userKey);

}
