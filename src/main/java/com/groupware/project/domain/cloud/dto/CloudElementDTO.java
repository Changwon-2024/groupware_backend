package com.groupware.project.domain.cloud.dto;

import lombok.Data;

@Data
public class CloudElementDTO {

    private String elementKey;

    private String ParentElementKey;

    private String name;

    private String fileSize;

    private String regUserEmail;

    private String regDateTime;

    private String modUserEmail;

    private String modDateTime;

}
