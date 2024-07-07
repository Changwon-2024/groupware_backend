package com.groupware.project.global.exceptions;

import com.groupware.project.global.response.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // ---------- 사용자 지정 커스텀 Exception ---------- //
    // 모든 에러는 200으로 변환 후 result 에 0으로 전송
    // Runtime Error
    @ExceptionHandler({CustomRuntimeException.class})
    public ResponseEntity<?> customRuntimeException(final CustomRuntimeException customException) {
        log.warn("error", customException);
        ResponseDTO<String> responseDTO =
                ResponseDTO.<String>builder()
                        .result(0)
                        .message(customException.getMessage())
                        .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    // Token Error
    @ExceptionHandler({CustomTokenException.class})
    public ResponseEntity<?> customTokenException(final CustomTokenException customException) {
        log.warn("error", customException);
        ResponseDTO<String> responseDTO =
                ResponseDTO.<String>builder()
                        .result(-1)
                        .message(customException.getMessage())
                        .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    // AccessDeny Error
    @ExceptionHandler({CustomAccessDenyException.class})
    public ResponseEntity<?> customAccessDenyException() {
        String message = "권한이 부족합니다. 다시 시도해 주세요.";
        ResponseDTO<String> responseDTO =
                ResponseDTO.<String>builder()
                        .result(0)
                        .message(message)
                        .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    /*
     * 실 서비스 시 백엔드 정보 보안을 위해 data(e.getMessage()) 부분 삭제 필요
     */
    // ---------- 기존 내장 Exception ---------- //
    // NPE Error
    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<?> nullPointException(final NullPointerException e) {
        log.warn("error", e);
        String message = "요청에 필요한 필수 정보가 부족합니다. 잠시 후 다시 시도하거나 관리자에게 문의하세요.";
        ResponseDTO<String> responseDTO =
                ResponseDTO.<String>builder()
                        .result(0)
                        .message(message)
                        .data(e.getMessage())
                        .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    // 400 Error
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> runtimeException(final RuntimeException e) {
        log.warn("error", e);
        String message = "지금은 처리할 수 없습니다. 잠시 후 다시 시도하거나 관리자에게 문의하세요.";
        ResponseDTO<String> responseDTO =
                ResponseDTO.<String>builder()
                        .result(0)
                        .message(message)
                        .data(e.getMessage())
                        .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    // 401 Error
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> unauthorizedException(final AccessDeniedException e) {
        log.warn("error", e);
        String message = "권한이 없습니다.";
        ResponseDTO<String> responseDTO =
                ResponseDTO.<String>builder()
                        .result(0)
                        .message(message)
                        .data(e.getMessage())
                        .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    // 404 Error
    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity<?> pageNotFoundException(final NoHandlerFoundException e) {
        log.warn("error", e);
        String message = "요청한 페이지는 존재하지 않습니다.";
        ResponseDTO<String> responseDTO =
                ResponseDTO.<String>builder()
                        .result(0)
                        .message(message)
                        .data(e.getMessage())
                        .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    // 500 Error
    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> unknownException(final Exception e) {
        log.warn("error", e);
        String message = "지금은 처리할 수 없습니다. 잠시 후 다시 시도하거나 관리자에게 문의하세요.";
        ResponseDTO<String> responseDTO =
                ResponseDTO.<String>builder()
                        .result(0)
                        .message(message)
                        .data(e.getMessage())
                        .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    // Validation Error
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> methodArgumentNotValidException(final MethodArgumentNotValidException e) {
        ResponseDTO<String> responseDTO =
                ResponseDTO.<String>builder()
                        .result(0)
                        .message(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                        .build();
        return ResponseEntity.ok().body(responseDTO);
    }

}
