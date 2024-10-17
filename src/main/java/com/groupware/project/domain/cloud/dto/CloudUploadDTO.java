package com.groupware.project.domain.cloud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CloudUploadDTO {

    @NotEmpty(message = "업로드할 폴더를 선택해 주세요.")
    @Schema(example = "폴더 키")
    private String folderKey;

}
