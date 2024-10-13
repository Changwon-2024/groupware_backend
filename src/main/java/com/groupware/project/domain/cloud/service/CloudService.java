package com.groupware.project.domain.cloud.service;

import com.groupware.project.domain.cloud.dto.CloudElementDTO;
import com.groupware.project.domain.cloud.mapper.CloudMapper;
import com.groupware.project.global.exceptions.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CloudService {

    private final CloudMapper cloudMapper;

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
}
