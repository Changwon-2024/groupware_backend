package com.groupware.project.domain.cloud.controller;

import com.groupware.project.domain.cloud.service.CloudService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "02. 클라우드 관련")
@RestController
@RequiredArgsConstructor
@RequestMapping("cloud")
public class CloudController {

    private final CloudService cloudService;

    // 최상위 폴더 조회

    // 하위 폴더 조회

    // 상위 폴더 조회

    // 파일 다운로드

    // 파일 이름 수정

    // 파일 삭제

    // 폴더 생성

    // 폴더 및 하위 데이터 삭제

    // 파일 업로드

}
