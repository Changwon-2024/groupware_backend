package com.groupware.project.domain.user.controller;

import com.groupware.project.domain.user.dto.UserInfoDTO;
import com.groupware.project.domain.user.dto.UserModifyDTO;
import com.groupware.project.domain.user.dto.UserSignDTO;
import com.groupware.project.domain.user.service.UserService;
import com.groupware.project.global.jwt.dto.JwtResponseDTO;
import com.groupware.project.global.jwt.service.JwtGlobalService;
import com.groupware.project.global.response.CountDTO;
import com.groupware.project.global.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperties;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "01. 유저 관련")
@RequiredArgsConstructor
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final JwtGlobalService jwtGlobalService;

    @Operation(summary = "1. 회원가입",
            description = "회원가입을 위한 api입니다. 가입 시 승인 대기 상태로 가입됩니다.")
    @PostMapping("sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid UserSignDTO userSignDTO) {

        userService.signUp(userSignDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("가입되었어요. 관리자의 승인을 기다리세요.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "2. 유저조회 (어드민 전용)",
            description = "권한 변경 및 유저관리를 위한 어드민용 조회 api입니다.")
    @PostMapping("list")
    public ResponseEntity<ResponseDTO<List<UserInfoDTO>>> getUserList(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken, 0);

        List<UserInfoDTO> userList = userService.getUserList();

        ResponseDTO<List<UserInfoDTO>> responseDTO =
                ResponseDTO.<List<UserInfoDTO>>builder()
                        .result(1)
                        .data(userList)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "2-1. 유저조회 (어드민 전용)",
            description = "권한 변경 및 유저관리를 위한 어드민용 개수 조회 api입니다.")
    @PostMapping("list/count")
    public ResponseEntity<ResponseDTO<CountDTO>> getUserListSize(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken, 0);

        CountDTO userListCount = userService.getUserListSize();

        ResponseDTO<CountDTO> responseDTO =
                ResponseDTO.<CountDTO>builder()
                        .result(1)
                        .data(userListCount)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "3. 권한 변경 (어드민 전용)",
            description = "권한 변경 및 유저관리를 위한 어드민용 관리 api입니다.")
    @PostMapping("manage")
    public ResponseEntity<?> manageUser(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    schemaProperties = {
                            @SchemaProperty(name = "userKey",
                                    schema = @Schema(type = "string", example = "권한을 바꿀 유저 키")),
                            @SchemaProperty(name = "roleLevel",
                                    schema = @Schema(type = "string", example = "권한 레벨 (0~3)")),
                    }
            ))@RequestBody Map<String, Object> requestBody) {

        jwtGlobalService.getTokenInfo(accessToken, 0);

        userService.manageUser(requestBody);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상적으로 처리되었어요.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "4. 마이페이지 조회",
            description = "개인 정보 조회를 위한 api입니다.")
    @PostMapping("my-page")
    public ResponseEntity<ResponseDTO<UserInfoDTO>> getMyPage(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken) {

        JwtResponseDTO jwtResponseDTO = jwtGlobalService.getTokenInfo(accessToken, 2);

        UserInfoDTO userInfoDTO = userService.getMyPage(jwtResponseDTO);

        ResponseDTO<UserInfoDTO> responseDTO =
                ResponseDTO.<UserInfoDTO>builder()
                        .result(1)
                        .data(userInfoDTO)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "5. 마이페이지 수정",
            description = "개인 정보 수정을 위한 api입니다.")
    @PostMapping("my-page/update")
    public ResponseEntity<?> updateMyPage(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid UserModifyDTO userModifyDTO) {

        JwtResponseDTO jwtResponseDTO = jwtGlobalService.getTokenInfo(accessToken, 2);

        userService.updateMyPage(userModifyDTO, jwtResponseDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상적으로 수정되었어요.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

}
