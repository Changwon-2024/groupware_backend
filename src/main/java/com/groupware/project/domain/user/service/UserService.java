package com.groupware.project.domain.user.service;

import com.groupware.project.domain.user.dto.UserInfoDTO;
import com.groupware.project.domain.user.dto.UserModifyDTO;
import com.groupware.project.domain.user.dto.UserSignDTO;
import com.groupware.project.domain.user.mapper.UserMapper;
import com.groupware.project.global.jwt.dto.JwtResponseDTO;
import com.groupware.project.global.response.CountDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;

    public void signUp(UserSignDTO userSignDTO) {

    }
    
    public List<UserInfoDTO> getUserList() {
        
        return null;
    }
    
    public CountDTO getUserListSize() {
        
        return new CountDTO();
    }
    
    public void manageUser(Map<String, Object> requestBody) {
        
    }
    
    public UserInfoDTO getMyPage(JwtResponseDTO jwtResponseDTO) {
        return null;
    }

    public void updateMyPage(UserModifyDTO userModifyDTO, JwtResponseDTO jwtResponseDTO) {
    }
}
