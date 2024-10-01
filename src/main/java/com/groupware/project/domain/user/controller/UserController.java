package com.groupware.project.domain.user.controller;

import com.groupware.project.domain.user.dto.UserSignDTO;
import com.groupware.project.domain.user.service.UserService;
import com.groupware.project.global.jwt.service.JwtGlobalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저 관련")
@RequiredArgsConstructor
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final JwtGlobalService jwtGlobalService;

    @Operation(summary = "1. 회원가입",
            description = "회원가입을 위한 api입니다. 가입 시 승인 대기 상태로 가입됩니다.")
    @PostMapping("login")
    public ResponseEntity<?> signUp(@RequestBody UserSignDTO userSignDTO) {

        return null;
    }

    @Operation(summary = "2. 유저조회 (어드민 전용)",
            description = "권한 변경 및 유저관리를 위한 어드민용 조회 api입니다.")
    @PostMapping("list")
    public ResponseEntity<?> getUserList() {

        return null;
    }

    @Operation(summary = "2-1. 유저조회 (어드민 전용)",
            description = "권한 변경 및 유저관리를 위한 어드민용 개수 조회 api입니다.")
    @PostMapping("list/count")
    public ResponseEntity<?> getUserCount() {

        return null;
    }

    @Operation(summary = "3. 권한 변경 (어드민 전용)",
            description = "권한 변경 및 유저관리를 위한 어드민용 관리 api입니다.")
    @PostMapping("manage")
    public ResponseEntity<?> manageUser() {

        return null;
    }

    @Operation(summary = "4. 마이페이지 조회",
            description = "개인 정보 조회를 위한 api입니다.")
    @PostMapping("my-page")
    public ResponseEntity<?> getMyPage() {

        return null;
    }

    @Operation(summary = "5. 마이페이지 수정",
            description = "개인 정보 수정을 위한 api입니다.")
    @PostMapping("my-page/update")
    public ResponseEntity<?> updateMyPage() {

        return null;
    }

}
