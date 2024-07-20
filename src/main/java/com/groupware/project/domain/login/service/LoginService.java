package com.groupware.project.domain.login.service;

import com.groupware.project.domain.login.dto.LoginDTO;
import com.groupware.project.domain.login.mapper.LoginMapper;
import com.groupware.project.global.exceptions.CustomRuntimeException;
import com.groupware.project.global.jwt.mapper.JwtGlobalMapper;
import com.groupware.project.global.jwt.service.JwtGlobalService;
import com.groupware.project.global.utils.CryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService {

    private final JwtGlobalService jwtGlobalService;

    private final LoginMapper loginMapper;
    private final JwtGlobalMapper jwtGlobalMapper;

    // 로그인
    @Transactional
    public String login(LoginDTO loginDTO, String loginIp) {

        String email = loginDTO.getEmail();
        String userKey = loginMapper.getUserKey(email);
        String password = loginDTO.getPassword();

        String encryptedPassword = CryptUtil.encrypt(password);
        boolean exist = loginMapper.isLoginInfoExist(email, encryptedPassword);
        if (!exist) throw new CustomRuntimeException("일치하는 회원정보가 없어요. 다시 시도해 주세요.");

        String refreshToken = jwtGlobalService.createRefreshToken(email);
        jwtGlobalMapper.updateTokenValue(userKey, refreshToken, loginIp);

        // 접속 IP가 3개 이상인 경우 가장 오래전 접속한 토큰 비활성화
        jwtGlobalMapper.forbidOldTokens(userKey);

        return refreshToken;
    }

    // 로그아웃
    @Transactional
    public void logout(String email, String loginIp) {

        String userKey = loginMapper.getUserKey(email);
        jwtGlobalMapper.logout(userKey, loginIp);
    }

}
