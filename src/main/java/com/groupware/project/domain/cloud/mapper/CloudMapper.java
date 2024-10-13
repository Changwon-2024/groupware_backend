package com.groupware.project.domain.cloud.mapper;

import com.groupware.project.domain.cloud.dto.CloudElementDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CloudMapper {

    // 요소 키로 폴더 내 하위 정보 조회
    List<CloudElementDTO> getElementsOfFolder(String elementKey);

    // 요소 세부 정보 조회
    CloudElementDTO getElementInfo(String elementKey);
}
