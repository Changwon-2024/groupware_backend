package com.groupware.project.domain.cloud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CloudUploadDBDTO {

    private String parentElementKey;

    private String name;

    private String elementPath;

    private Long fileSize;

    private String regUserKey;

}
