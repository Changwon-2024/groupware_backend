package com.groupware.project.domain.user.service;

import com.groupware.project.domain.login.mapper.LoginMapper;
import com.groupware.project.domain.user.dto.UserInfoDTO;
import com.groupware.project.domain.user.dto.UserModifyDTO;
import com.groupware.project.domain.user.dto.UserSignDTO;
import com.groupware.project.domain.user.mapper.UserMapper;
import com.groupware.project.global.exceptions.CustomRuntimeException;
import com.groupware.project.global.jwt.dto.JwtResponseDTO;
import com.groupware.project.global.response.CountDTO;
import com.groupware.project.global.utils.CryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;

    private final LoginMapper loginMapper;

    /**
     * 회원가입
     * @param userSignDTO 신규 회원 정보 DTO
     */
    @Transactional
    public void signUp(UserSignDTO userSignDTO) {

        // 이메일 중복 확인
        if (userMapper.isEmailExist(userSignDTO.getEmail()))
            throw new CustomRuntimeException("이미 존재하는 이메일이에요.");

        // 비밀번호 확인 및 암호화
        if (!userSignDTO.getPassword().equals(userSignDTO.getPasswordConfirm()))
            throw new CustomRuntimeException("비밀번호와 비밀번호 확인이 일치하지 않아요.");
        userSignDTO.setPassword(CryptUtil.encrypt(userSignDTO.getPassword()));

        // 주소 암호화
        if (userSignDTO.getAddress() != null && !userSignDTO.getAddress().isEmpty())
            userSignDTO.setAddress(CryptUtil.encrypt(userSignDTO.getAddress()));

        // 전화번호 암호화
        if (userSignDTO.getPhoneNumber() != null && !userSignDTO.getPhoneNumber().isEmpty())
            userSignDTO.setPhoneNumber(CryptUtil.encrypt(userSignDTO.getPhoneNumber()));

        userMapper.signUp(userSignDTO);

    }

    /**
     * (관리자 전용) 유저 목록 조회
     * @return 유저 목록
     */
    public List<UserInfoDTO> getUserList() {

        List<UserInfoDTO> userList =
                Optional.ofNullable(userMapper.getUserList())
                        .orElse(Collections.emptyList());

        userList.forEach(dto -> {
            if (dto.getAddress() != null && !dto.getAddress().isEmpty())
                dto.setAddress(CryptUtil.decrypt(dto.getAddress()));

            if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isEmpty())
                dto.setPhoneNumber(CryptUtil.decrypt(dto.getPhoneNumber()));
        });

        return userList;
    }

    /**
     * (관리자 전용) 유저 목록 개수 조회
     * @return 유저 목록 크기
     */
    public CountDTO getUserListSize() {

        return new CountDTO(userMapper.getUserListSize());
    }

    /**
     * 유저 권한 변경
     * @param requestBody userKey, roleLevel
     */
    @Transactional
    public void manageUser(Map<String, Object> requestBody) {

        String userKey = requestBody.get("userKey").toString();
        String roleLevel = requestBody.get("roleLevel").toString();

        userMapper.manageUser(userKey, roleLevel);

    }

    /**
     * 마이페이지 조회
     * @param jwtResponseDTO 접속 유저 정보
     * @return 접속 유저의 회원 정보
     */
    public UserInfoDTO getMyPage(JwtResponseDTO jwtResponseDTO) {

        if (!userMapper.isEmailExist(jwtResponseDTO.getEmail()))
            throw new CustomRuntimeException("존재하지 않는 계정이에요.");

        UserInfoDTO userDTO = userMapper.getMyPage(jwtResponseDTO.getEmail());

        if (userDTO.getAddress() != null && !userDTO.getAddress().isEmpty())
            userDTO.setAddress(CryptUtil.decrypt(userDTO.getAddress()));

        if (userDTO.getPhoneNumber() != null && !userDTO.getPhoneNumber().isEmpty())
            userDTO.setPhoneNumber(CryptUtil.decrypt(userDTO.getPhoneNumber()));

        return userDTO;
    }

    /**
     * 마이페이지 수정
     * @param userModifyDTO 수정할 회원의 정보
     * @param jwtResponseDTO 접속 유저 정보
     */
    @Transactional
    public void updateMyPage(UserModifyDTO userModifyDTO, JwtResponseDTO jwtResponseDTO) {

        // 접속 유저와 변경하려는 회원 정보의 이메일이 동일한지 확인
        String userEmail = userMapper.getUserEmail(userModifyDTO.getUserKey());
        if (!jwtResponseDTO.getEmail().equals(userEmail))
            throw new CustomRuntimeException("본인 정보만 수정할 수 있어요.");

        if (!loginMapper.isLoginInfoExist(jwtResponseDTO.getEmail(), CryptUtil.encrypt(userModifyDTO.getOldPassword())))
            throw new CustomRuntimeException("기존 비밀번호가 일치하지 않아 정보를 수정할 수 없어요. 비밀번호를 확인해 주세요.");

        // 주소 암호화
        if (userModifyDTO.getAddress() != null && !userModifyDTO.getAddress().isEmpty())
            userModifyDTO.setAddress(CryptUtil.encrypt(userModifyDTO.getAddress()));

        // 전화번호 암호화
        if (userModifyDTO.getPhoneNumber() != null && !userModifyDTO.getPhoneNumber().isEmpty())
            userModifyDTO.setPhoneNumber(CryptUtil.encrypt(userModifyDTO.getPhoneNumber()));

        // 비밀번호 변경 시 로직 추가
        if (userModifyDTO.getPassword() == null || userModifyDTO.getPassword().isEmpty())
            userModifyDTO.setPassword(CryptUtil.encrypt(userModifyDTO.getOldPassword()));
        else {
            if (!userModifyDTO.getPassword().equals(userModifyDTO.getPasswordConfirm()))
                throw new CustomRuntimeException("비밀번호와 비밀번호 확인이 일치하지 않아요.");
            userModifyDTO.setPassword(CryptUtil.encrypt(userModifyDTO.getPassword()));
        }

        userMapper.updateMyPage(userModifyDTO);

    }
}
