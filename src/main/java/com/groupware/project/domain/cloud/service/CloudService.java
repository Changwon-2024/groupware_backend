package com.groupware.project.domain.cloud.service;

import com.groupware.project.domain.cloud.dto.CloudElementDTO;
import com.groupware.project.domain.cloud.dto.CloudUploadDBDTO;
import com.groupware.project.domain.cloud.dto.CloudUploadDTO;
import com.groupware.project.domain.cloud.mapper.CloudMapper;
import com.groupware.project.domain.user.mapper.UserMapper;
import com.groupware.project.global.exceptions.CustomRuntimeException;
import com.groupware.project.global.jwt.dto.JwtResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CloudService {

    private final CloudMapper cloudMapper;
    private final UserMapper userMapper;

    @Value("${cloud.path.prefix}")
    private String prefixPath;

    @Value("${cloud.path.root}")
    private String rootPath;

    @Value("${cloud.key.root}")
    private String rootKey;

    /**
     * 최상위 폴더 내 정보 조회
     * @return 최상위 폴더 내 요소 리스트
     */
    public List<CloudElementDTO> getRootFolderInfo() {

        return Optional.ofNullable(cloudMapper.getElementsOfFolder(rootKey))
                .orElse(Collections.emptyList());
    }

    /**
     * 특정 폴더 내 정보 조회
     * @param requestBody elementKey
     * @return 폴더일 경우 조회 아닐 경우 오류
     */
    public List<CloudElementDTO> getSubFolderInfo(Map<String, Object> requestBody) {

        String elementKey = requestBody.get("elementKey").toString();

        CloudElementDTO elementDTO = cloudMapper.getElementInfo(elementKey);

        if (elementDTO == null)
            throw new CustomRuntimeException("존재하지 않거나 이미 삭제된 항목이에요.");

        if (elementDTO.getFileSize() != null)
            throw new CustomRuntimeException("하위 항목은 폴더만 조회할 수 있어요.");

        return Optional.ofNullable(cloudMapper.getElementsOfFolder(elementKey))
                .orElse(Collections.emptyList());
    }

    /**
     * 특정 폴더의 상위 폴더 조회
     * @param requestBody elementKey
     * @return 상위 폴더 요소 정보 조회
     */
    public List<CloudElementDTO> getParentFolderInfo(Map<String, Object> requestBody) {

        String elementKey = requestBody.get("elementKey").toString();

        CloudElementDTO elementDTO = cloudMapper.getElementInfo(elementKey);

        if (elementDTO == null)
            throw new CustomRuntimeException("존재하지 않거나 이미 삭제된 항목이에요.");

        if (elementDTO.getParentElementKey() == null)
            throw new CustomRuntimeException("이미 최상위 폴더를 조회하고 있어요.");

        return Optional.ofNullable(cloudMapper.getElementsOfFolder(elementDTO.getParentElementKey()))
                .orElse(Collections.emptyList());

    }

    /**
     * 파일 업로드
     * @param jwtResponseDTO 접속 유저 정보
     * @param cloudUploadDTO 업로드할 폴더 키
     * @param file 파일
     */
    @Transactional
    public void uploadFile(JwtResponseDTO jwtResponseDTO, CloudUploadDTO cloudUploadDTO, MultipartFile file) {

        // 파일 무결성 검사
        if (file == null || file.getSize() == 0)
            throw new CustomRuntimeException("업로드할 파일이 없어요. 다시 시도해주세요.");

        String fileName = file.getOriginalFilename();
        if (fileName == null)
            throw new CustomRuntimeException("파일명이 올바르지 않아요. 다시 시도해주세요.");
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

        if (!List.of("jpg", "jpeg", "png", "bmp", "txt").contains(fileExtension))
            throw new CustomRuntimeException("지원하지 않는 확장자에요. 다시 시도해주세요.");

        String folderKey = cloudUploadDTO.getFolderKey();

        // 폴더 존재 여부 확인
        CloudElementDTO folderDTO = cloudMapper.getElementInfo(folderKey);
        if (folderDTO == null || folderDTO.getFileSize() != null)
            throw new CustomRuntimeException("존재하지 않는 폴더에요. 다시 시도해주세요.");

        // 동일한 파일명 존재 확인
        if (cloudMapper.isFileNameExistedInFolder(fileName))
            throw new CustomRuntimeException("폴더 내에 이미 동일한 파일이 존재해요.");

        String fileRelativePath = folderDTO.getElementPath();
        String fileAbsolutePath = prefixPath + fileRelativePath;

        // 접두어 + 폴더 경로 + 파일명으로 저장
        // 폴더 없을 경우 생성
        File folder = new File(fileAbsolutePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 파일 저장
        try {
            file.transferTo(new File(fileAbsolutePath + "/" + fileName));
        } catch (IOException e) {
            throw new CustomRuntimeException("파일 저장에 실패했어요. 다시 시도해주세요.");
        }

        String userKey = userMapper.getUserKey(jwtResponseDTO.getEmail());

        // 접두어 제외 경로 DB 저장
        CloudUploadDBDTO uploadDTO =
                CloudUploadDBDTO.builder()
                        .parentElementKey(folderKey)
                        .name(fileName)
                        .elementPath(fileRelativePath + "/" + fileName)
                        .fileSize(file.getSize())
                        .regUserKey(userKey)
                        .build();
        cloudMapper.uploadFile(uploadDTO);
    }

    /**
     * 파일 다운로드
     * @param requestBody elementKey
     * @return Path (파일 경로)
     */
    public Path downloadFile(Map<String, Object> requestBody) {

        String elementKey = requestBody.get("elementKey").toString();

        CloudElementDTO elementDTO = cloudMapper.getElementInfo(elementKey);

        if (elementDTO == null)
            throw new CustomRuntimeException("파일을 찾을 수 없어요. 삭제되었거나 이동되었을 수 있어요.");

        if (elementDTO.getFileSize() == null)
            throw new CustomRuntimeException("폴더는 다운로드 받을 수 없어요.");

        String fileRelativePath = elementDTO.getElementPath();

        Path path = Paths.get(prefixPath + fileRelativePath);
        if (!Files.exists(path))
            throw new CustomRuntimeException("파일을 찾을 수 없어요. 삭제되었거나 이동되었을 수 있어요.");

        return path;
    }
}
