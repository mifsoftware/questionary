package com.example.testingProject.util;

import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@UtilityClass
@Slf4j
public class StringToKeyConverter {

    public static SecretKey convertStringToSecretKey(String encodedKey) {
        try {
            return Keys.hmacShaKeyFor(encodedKey.getBytes(StandardCharsets.UTF_8));
        }
        catch (InvalidKeyException ex){
            log.error("invalid signing key", ex);
            throw ex;
        }
    }
}
