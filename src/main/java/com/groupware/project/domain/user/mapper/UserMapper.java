package com.groupware.project.domain.user.mapper;

import com.groupware.project.domain.user.dto.UserInfoDTO;
import com.groupware.project.domain.user.dto.UserModifyDTO;
import com.groupware.project.domain.user.dto.UserSignDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    // 이메일 중복 검사
    Boolean isEmailExist(String email);

    // 회원가입
    void signUp(UserSignDTO userSignDTO);

    // 유저 목록 조회
    List<UserInfoDTO> getUserList();

    // 유저 목록 개수 조회
    Integer getUserListSize();

    // 유저 권한 변경
    void manageUser(String userKey, String roleLevel);

    // 마이페이지 유저 조회
    UserInfoDTO getMyPage(String email);

    // 마이페이지 유저 정보 수정
    void updateMyPage(UserModifyDTO userModifyDTO);

    // 유저 이메일 조회
    String getUserEmail(String userKey);

    // 유저 키 조회
    String getUserKey(String email);

}
