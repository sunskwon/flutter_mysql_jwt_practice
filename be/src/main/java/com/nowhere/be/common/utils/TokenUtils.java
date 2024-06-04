package com.nowhere.be.common.utils;

import com.nowhere.be.user.model.dto.LoginUserDTO;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtils {
    // token 관리를 위한 utils 모음
    private static String jwtSecretKey;
    private static Long tokenValidateTime;

    @Value("${jwt.key}")
    public void setJwtSecretKey(String jwtSecretKey) {
        TokenUtils.jwtSecretKey = jwtSecretKey;
    }

    @Value("{jwt.time}")
    public void setTokenValidateTime(Long tokenValidateTime) {
        TokenUtils.tokenValidateTime = tokenValidateTime;
    }

    // header의 token 분리
    public static String splitHeader(String header) {
        if (!header.equals("")) {
            return header.split(" ")[1];
        } else {
            return null;
        }
    }

    public static boolean isValidToken(String token) {

        try {
            Claims claims = getClaimsFromToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            return false;
        } catch (JwtException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static Claims getClaimsFromToken(String token) {

        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecretKey))
                .parseClaimsJws(token)
                .getBody();
    }

    public static String generateJwtToken(LoginUserDTO user) {

        Date expireTime = new Date(System.currentTimeMillis() + tokenValidateTime);

        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(user))
                .setSubject("token : " + user.getUserCode())
                .signWith(SignatureAlgorithm.HS256, createSignature())
                .setExpiration(expireTime);

        return builder.compact();
    }

    private static Map<String, Object> createHeader() {

        Map<String, Object> header = new HashMap<>();

        header.put("type", "jwt");
        header.put("alg", "HS256");
        header.put("date", System.currentTimeMillis());

        return header;
    }

    private static Map<String, Object> createClaims(LoginUserDTO user) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", user.getUserId());
        claims.put("userName", user.getUserName());
        claims.put("userRole", user.getUserRole());

        return claims;
    }

    private static Key createSignature() {

        byte[] secretBytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);

        return new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());
    }
}
