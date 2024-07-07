package com.groupware.project.global.jwt.service;

import com.groupware.project.global.exceptions.CustomRuntimeException;
import com.groupware.project.global.exceptions.CustomTokenException;
import com.groupware.project.global.jwt.dto.JwtResponseDTO;
import com.groupware.project.global.jwt.mapper.JwtGlobalMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtGlobalService {

    private final JwtGlobalMapper jwtGlobalMapper;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Jwts.SIG.HS256.key().build();
    }

    public JwtResponseDTO getTokenInfo(String accessToken) {

        if (accessToken == null || accessToken.isEmpty())
            throw new CustomTokenException("로그인 정보가 만료되었어요. 계속하려면 다시 로그인해주세요.");

        if (accessToken.startsWith("Bearer "))
            accessToken = accessToken.substring(7);

        Date tokenDate = getExpiration(accessToken);
        if (tokenDate.before(new Date()))
            throw new CustomTokenException("로그인 정보가 만료되었어요. 계속하려면 다시 로그인해주세요.");

        String userKey = jwtGlobalMapper.getUserKey(getEmail(accessToken));
        JwtResponseDTO jwtResponseDTO = jwtGlobalMapper.getUserInfo(userKey);

        if (jwtResponseDTO == null)
            throw new CustomRuntimeException("일치하는 회원정보가 없어요. 다시 시도해 주세요.");

        return jwtResponseDTO;
    }

    /**
     * 로그인 정보 생성
     * 리프레시 토큰을 기준으로 만료일자와 DB내 토큰값 비교
     * 모든 조건이 일치하는 경우 엑세스 토큰 발급
     * @param refreshToken 리프레시 토큰
     * @return 엑세스 토큰
     */
    public String silent(String refreshToken) {

        if (refreshToken == null)
            throw new CustomTokenException("로그인 정보가 만료되었어요. 계속하려면 다시 로그인해주세요.");

        if (refreshToken.startsWith("Bearer "))
            refreshToken = refreshToken.substring(7);

        // 리프레시 토큰 만기 일자 비교
        Date expireDate = getExpiration(refreshToken);
        if (expireDate.before(new Date()))
            throw new CustomTokenException("로그인 정보가 만료되었어요. 계속하려면 다시 로그인해주세요.");

        String email = getEmail(refreshToken);
        String userKey = jwtGlobalMapper.getUserKey(email);

        // 리프레시 토큰값 일치 여부 비교
        String originalRefreshToken = jwtGlobalMapper.getTokenValue(userKey);
        if (!originalRefreshToken.equals(refreshToken))
            throw new CustomTokenException("로그인 정보가 변경되었어요. 계속하려면 다시 로그인해주세요.");

        return createAccessToken(email);
    }

    // 엑세스 토큰 검증
    public JwtResponseDTO valid(String accessToken) {

        if (accessToken == null)
            throw new CustomTokenException("로그인 정보가 만료되었어요. 계속하려면 다시 로그인해주세요.");

        if (accessToken.startsWith("Bearer "))
            accessToken = accessToken.substring(7);

        Date expireDate = getExpiration(accessToken);
        if (expireDate.before(new Date()))
            throw new CustomTokenException("로그인 정보가 만료되었어요. 계속하려면 다시 로그인해주세요.");

        String email = getEmail(accessToken);
        String userKey = jwtGlobalMapper.getUserKey(email);

        return jwtGlobalMapper.getUserInfo(userKey);
    }


    // 리프레시 토큰 생성
    public String createRefreshToken(String email) {
        // 1 개월
        long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 31;
        Date now = new Date();

        return Jwts.builder()
                .header()
                .and()
                .subject(email)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(secretKey)
                .compact();
    }

    // 액세스 토큰 생성
    public String createAccessToken(String email) {
        // 두 시간
        long tokenValidTime = 1000L * 60 * 60 * 2;
        Date now = new Date();

        return Jwts.builder()
                .header()
                .and()
                .subject(email)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + tokenValidTime))
                .signWith(secretKey)
                .compact();
    }

    // 사원 아이디 얻기
    public String getEmail(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch(ExpiredJwtException e) {
            throw new CustomTokenException("로그인 정보가 만료되었어요. 계속하려면 다시 로그인해주세요.");
        }
    }

    // 만료날짜 얻기
    public Date getExpiration(String token) {
        if(token == null || token.isBlank()) {
            throw new CustomTokenException("로그인 정보가 만료되었어요. 계속하려면 다시 로그인해주세요.");
        }
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
        } catch(Exception e) {
            throw new CustomTokenException("로그인 정보가 만료되었어요. 계속하려면 다시 로그인해주세요.");
        }
    }

}
