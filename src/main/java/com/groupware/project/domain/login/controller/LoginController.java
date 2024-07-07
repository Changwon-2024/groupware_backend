package com.groupware.project.domain.login.controller;

import com.groupware.project.domain.login.dto.LoginDTO;
import com.groupware.project.domain.login.service.LoginService;
import com.groupware.project.global.jwt.dto.JwtResponseDTO;
import com.groupware.project.global.jwt.service.JwtGlobalService;
import com.groupware.project.global.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@Tag(name = "00. 로그인 관련")
@RestController
@RequiredArgsConstructor
@RequestMapping("login")
public class LoginController {

    private final LoginService loginService;
    private final JwtGlobalService jwtGlobalService;

    @Operation(summary = "1. 로그인",
            description = "로그인 후 refresh 토큰을 발급하기 위한 api입니다.")
    @PostMapping("sign")
    public ResponseEntity<ResponseDTO<String>> login(
            @RequestBody @Valid LoginDTO loginDTO,
            HttpServletResponse response) {

        String refreshToken = loginService.login(loginDTO);

        ResponseCookie cookie =
                ResponseCookie.from("myGroupware", refreshToken)
                        .maxAge(7 * 24 * 60 * 60)
                        .path("/")
                        .httpOnly(true)
                        .build();
        response.addHeader("Set-Cookie", cookie.toString());

        ResponseDTO<String> responseDTO =
                ResponseDTO.<String>builder()
                        .message("로그인 되었습니다.")
                        .data(refreshToken)
                        .result(1)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "2. 액세스 토큰 재발급",
            description = "refresh 토큰으로 access 토큰을 발급받습니다.")
    @PostMapping("/silent")
    public ResponseEntity<ResponseDTO<String>> silent(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        String refreshToken = Stream.of(cookies)
                .filter(cookie -> "myGroupware".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse("");

        String accessToken = jwtGlobalService.silent(refreshToken);

        ResponseDTO<String> responseDTO = ResponseDTO.<String>builder()
                .data(accessToken)
                .result(1)
                .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "3. 액세스 토큰 검증",
            description = "access 토큰이 유효한지 및 해당 토큰의 사원정보를 조회합니다.")
    @PostMapping("/valid")
    public ResponseEntity<ResponseDTO<JwtResponseDTO>> valid(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken) {

        JwtResponseDTO userInfo = jwtGlobalService.valid(accessToken);

        ResponseDTO<JwtResponseDTO> responseDTO =
                ResponseDTO.<JwtResponseDTO>builder()
                        .data(userInfo)
                        .result(1)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "4. 로그아웃",
            description = "쿠키를 만료시킵니다.")
    @PostMapping("/logout")
    public ResponseEntity<ResponseDTO<String>> logout(HttpServletRequest request,
                                                      HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        String refreshToken = Stream.of(cookies)
                .filter(cookie -> "myGroupware".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse("");

        String email = jwtGlobalService.silent(refreshToken);
        loginService.logout(email);

        ResponseCookie responseCookie =
                ResponseCookie.from("wimirGroupware", "")
                        .maxAge(0)
                        .path("/")
                        .httpOnly(true)
                        .build();
        response.addHeader("Set-Cookie", responseCookie.toString());

        ResponseDTO<String> responseDTO =
                ResponseDTO.<String>builder()
                        .data("로그아웃 되었습니다")
                        .result(1)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
}
