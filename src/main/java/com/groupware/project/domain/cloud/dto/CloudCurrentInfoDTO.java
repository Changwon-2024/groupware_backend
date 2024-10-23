package com.groupware.project.domain.cloud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CloudCurrentInfoDTO {

    @Schema(description = "현재 요소 정보")
    private CloudElementDTO you;

    @Schema(description = "자식 요소 정보 (현재 요소가 폴더일 경우)")
    private List<CloudElementDTO> children;

}
