package com.groupware.project.domain.cloud.service;

import com.groupware.project.domain.cloud.mapper.CloudMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CloudService {

    private final CloudMapper cloudMapper;

}
