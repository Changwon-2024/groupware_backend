package com.groupware.project.domain.cloud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CloudElementDTO {

    @Schema(example = "요소 키")
    private String elementKey;

    @Schema(example = "부모 요소 키")
    private String ParentElementKey;

    @Schema(example = "이름")
    private String name;

    @Schema(example = "요소 경로")
    private String elementPath;

    @Schema(example = "파일 크기 (폴더일 경우 null)")
    private String fileSize;

    @Schema(example = "작성자 이메일")
    private String regUserEmail;

    @Schema(example = "작성 시각")
    private String regDateTime;

    @Schema(example = "최근 수정 유저 이메일")
    private String modUserEmail;

    @Schema(example = "최근 수정 시각")
    private String modDateTime;

}
