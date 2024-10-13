package com.groupware.project.global.jwt.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JwtGlobalMapper {

    // 리프레시 토큰 갱신
    void updateTokenValue(String userKey, String refreshToken, String clientIp);

    // 로그아웃
    void logout(String userKey, String clientIp);

    // 허용된 토큰이 4개 이상일 경우 최근 3개를 제외한 토큰 비활성화
    void forbidOldTokens(String userKey);

    // 유저 키로 리프레시 토큰 값 조회
    String getTokenValue(String userKey, String loginIp);

    // 접속 유저 및 IP 허용 여부
    Boolean isCurrentIpPermitted(String userKey, String loginIp);

}
