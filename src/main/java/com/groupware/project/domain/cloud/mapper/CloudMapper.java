package com.groupware.project.domain.cloud.mapper;

import com.groupware.project.domain.cloud.dto.CloudElementDTO;
import com.groupware.project.domain.cloud.dto.CloudUploadDBDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CloudMapper {

    // 요소 키로 폴더 내 하위 정보 조회
    List<CloudElementDTO> getElementsOfFolder(String elementKey);

    // 요소 세부 정보 조회
    CloudElementDTO getElementInfo(String elementKey);

    // 동일한 파일명 존재 여부 확인
    Boolean isFileNameExistedInFolder(String fileName);

    // 파일 메타데이터 업로드
    void uploadFile(CloudUploadDBDTO uploadDTO);

}
