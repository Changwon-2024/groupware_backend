package com.groupware.project.domain.login.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {

    // 아이디, 비밀번호 일치 여부
    Boolean isLoginInfoExist(String email, String password);

    // 리프레시 토큰 갱신
    void updateTokenValue(String userKey, String refreshToken);

    // 로그아웃
    void logout(String email);

}
