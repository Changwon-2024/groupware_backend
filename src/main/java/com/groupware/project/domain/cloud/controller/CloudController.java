package com.groupware.project.domain.cloud.controller;

import com.groupware.project.domain.cloud.dto.CloudElementDTO;
import com.groupware.project.domain.cloud.dto.CloudUploadDTO;
import com.groupware.project.domain.cloud.service.CloudService;
import com.groupware.project.global.exceptions.CustomRuntimeException;
import com.groupware.project.global.jwt.dto.JwtResponseDTO;
import com.groupware.project.global.jwt.service.JwtGlobalService;
import com.groupware.project.global.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Tag(name = "02. 클라우드 관련")
@RestController
@RequiredArgsConstructor
@RequestMapping("cloud")
public class CloudController {

    private final CloudService cloudService;

    private final JwtGlobalService jwtGlobalService;

    @Operation(summary = "1. 최상위 폴더 조회",
            description = "클라우드 서비스 접속 시 최상위 폴더 접근을 위한 api입니다.")
    @PostMapping("path/root")
    public ResponseEntity<ResponseDTO<List<CloudElementDTO>>> getRootFolderInfo(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken, 2);

        List<CloudElementDTO> elementList = cloudService.getRootFolderInfo();

        ResponseDTO<List<CloudElementDTO>> responseDTO =
                ResponseDTO.<List<CloudElementDTO>>builder()
                        .result(1)
                        .data(elementList)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "2. 하위 폴더 조회",
            description = "특정 폴더의 하위 정보를 조회하는 api입니다.")
    @PostMapping("path/sub")
    public ResponseEntity<?> getSubFolderInfo(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    schemaProperties = {
                            @SchemaProperty(name = "elementKey",
                                    schema = @Schema(type = "string", example = "폴더 요소 키"))
                    }
            )) @RequestBody Map<String, Object> requestBody) {

        jwtGlobalService.getTokenInfo(accessToken, 2);

        List<CloudElementDTO> elementList = cloudService.getSubFolderInfo(requestBody);

        ResponseDTO<List<CloudElementDTO>> responseDTO =
                ResponseDTO.<List<CloudElementDTO>>builder()
                        .result(1)
                        .data(elementList)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "3. 상위 폴더 조회",
            description = "특정 폴더의 상위 정보를 조회하는 api입니다.")
    @PostMapping("path/parent")
    public ResponseEntity<ResponseDTO<List<CloudElementDTO>>> getParentFolderInfo(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    schemaProperties = {
                            @SchemaProperty(name = "elementKey",
                                    schema = @Schema(type = "string", example = "폴더 요소 키"))
                    }
            )) @RequestBody Map<String, Object> requestBody) {

        jwtGlobalService.getTokenInfo(accessToken, 2);

        List<CloudElementDTO> elementList = cloudService.getParentFolderInfo(requestBody);

        ResponseDTO<List<CloudElementDTO>> responseDTO =
                ResponseDTO.<List<CloudElementDTO>>builder()
                        .result(1)
                        .data(elementList)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "4. 파일 업로드",
            description = "파일을 업로드하는 api입니다.")
    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken,
            @RequestPart(value = "json") @Valid CloudUploadDTO cloudUploadDTO,
            @RequestPart(value = "file") MultipartFile file) {

        JwtResponseDTO jwtResponseDTO = jwtGlobalService.getTokenInfo(accessToken, 1);

        cloudService.uploadFile(jwtResponseDTO, cloudUploadDTO, file);

        ResponseDTO<Object> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("업로드를 성공했어요.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "5. 파일 다운로드",
            description = "파일을 다운로드하는 api입니다.")
    @PostMapping("download")
    public ResponseEntity<?> downloadFile(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    schemaProperties = {
                            @SchemaProperty(name = "elementKey",
                                    schema = @Schema(type = "string", example = "파일 키 (폴더일 경우 다운로드 불가능)")),
                    }
            ))@RequestBody Map<String, Object> requestBody) {

        jwtGlobalService.getTokenInfo(accessToken, 2);

        Path filePath = cloudService.downloadFile(requestBody);

        try {
            File file = filePath.toFile();
            Resource resource = new FileSystemResource(file);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");

            return ResponseEntity.ok().headers(headers).contentLength(file.length()).body(resource);

        } catch (Exception e) {
            throw new CustomRuntimeException("파일 다운로드에 실패했어요. 다시 시도해주세요.");
        }
    }

    @Operation(summary = "6. 파일 이름 수정",
            description = "파일명을 수정하는 api입니다.")
    @PostMapping("rename")
    public ResponseEntity<?> renameFile(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken) {

        JwtResponseDTO jwtResponseDTO = jwtGlobalService.getTokenInfo(accessToken, 1);

        return null;
    }

    @Operation(summary = "7. 파일 삭제",
            description = "파일을 삭제하는 api입니다.")
    @PostMapping("delete")
    public ResponseEntity<?> deleteFile(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken) {

        JwtResponseDTO jwtResponseDTO = jwtGlobalService.getTokenInfo(accessToken, 1);

        return null;
    }

    @Operation(summary = "8. 폴더 생성",
            description = "폴더를 생성하는 api입니다.")
    @PostMapping("create/folder")
    public ResponseEntity<?> createFolder(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken) {

        JwtResponseDTO jwtResponseDTO = jwtGlobalService.getTokenInfo(accessToken, 1);

        return null;
    }

    @Operation(summary = "9. 폴더 및 하위 요소 일괄 삭제",
            description = "폴더와 해당 폴더의 모든 하위 요소를 삭제하는 api입니다.")
    @PostMapping("delete/all")
    public ResponseEntity<?> deleteAll(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken) {

        JwtResponseDTO jwtResponseDTO = jwtGlobalService.getTokenInfo(accessToken, 1);

        return null;
    }

}
