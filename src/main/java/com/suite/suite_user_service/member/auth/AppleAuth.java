package com.suite.suite_user_service.member.auth;

import com.suite.suite_user_service.member.auth.appleDto.KeyInfo;
import com.suite.suite_user_service.member.auth.appleDto.Keys;
import com.suite.suite_user_service.member.dto.ReqSignInMemberDto;
import com.suite.suite_user_service.member.handler.CustomException;
import com.suite.suite_user_service.member.handler.StatusCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class AppleAuth {
    public static final String APPLE_KEY = "https://appleid.apple.com/auth/keys";
    public static final int HEADER = 0;
    public static String KID = "kid";
    public static String ALG = "alg";
    public static String ALGORITHM = "RSA";

    public static String EMAIL = "email";
    public static String SUB = "sub";

    public ReqSignInMemberDto getAppleMemberInfo(String identityToken) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Keys keys = restTemplate.getForEntity(APPLE_KEY, Keys.class).getBody();

            Map<String, String> headerKey = getTokenHeaderInfo(identityToken);

            KeyInfo keyInfo = keys.getKeys().stream().filter(
                    key -> key.validateKey(headerKey.get(KID), headerKey.get(ALG))).findFirst().orElseThrow( ()-> new CustomException(StatusCode.FORBIDDEN));

            PublicKey publicKey = getPublicKey(keyInfo);
            Claims memberInfo = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(identityToken).getBody();
            JSONObject claims = claimsToJSONObject(memberInfo);

            return ReqSignInMemberDto.builder()
                    .email(claims.get(EMAIL).toString())
                    .password(claims.get(SUB).toString())
                    .isOauth(true).build();
        } catch (ParseException e) {
            throw new CustomException(StatusCode.NOT_FOUND);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CustomException(StatusCode.FAILED_SIGNUP);
        }
    }

    private Map<String, String> getTokenHeaderInfo(String identityToken) throws ParseException {
        String[] decodeToken = identityToken.split("\\.");
        String headerInfo = new String(Base64.getDecoder().decode(decodeToken[HEADER]));
        JSONParser parser = new JSONParser();
        JSONObject keyObject = (JSONObject) parser.parse(headerInfo);

        Map<String, String> map = new HashMap<>();
        map.put(KID, keyObject.get(KID).toString());
        map.put(ALG, keyObject.get(ALG).toString());

        return map;
    }

    private PublicKey getPublicKey(KeyInfo keyInfo) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] nBytes = Base64.getUrlDecoder().decode(keyInfo.getN());
        byte[] eBytes = Base64.getUrlDecoder().decode(keyInfo.getE());
        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(publicKeySpec);
    }

    private JSONObject claimsToJSONObject(Claims claims) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(claims);
        return jsonObject;
    }

}
