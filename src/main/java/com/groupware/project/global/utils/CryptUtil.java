package com.groupware.project.global.utils;

import com.groupware.project.global.exceptions.CustomRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.stream.Collectors;

@Component
public class CryptUtil {

    // 암호화 설정
    private static String alg;
    @Value("${groupware.security.alg}")
    private void setAlg(String alg) {
        CryptUtil.alg = alg;
    }

    // 키
    private static String key;
    @Value("${groupware.security.key}")
    private void setKey(String key) {
        CryptUtil.key = key;
    }

    // 벡터
    private static String iv;
    @Value("${groupware.security.iv}")
    private void setIv(String iv) {
        CryptUtil.iv = iv;
    }

    /**
     * 암호화
     * @param plain 평문
     * @return 암호문
     */
    public static String encrypt(String plain) {

        try {
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] encrypted = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new CustomRuntimeException(e.getMessage());
        }

    }

    /**
     * 복호화
     * @param encrypted 암호문
     * @return 평문
     */
    public static String decrypt(String encrypted) {

        try {
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] decodedBytes = Base64.getDecoder().decode(encrypted);
            byte[] decrypted = cipher.doFinal(decodedBytes);

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CustomRuntimeException(e.getMessage());
        }

    }

    public static String getTokenValue() {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom secureRandom = new SecureRandom();

        return secureRandom.ints(16, 0, characters.length())
                .mapToObj(characters::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
