package pers.liam.util;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * @author liam
 * @date 2021/4/7 12:59
 */
public class JwtUtil {
    /**
     * Token过期时间
     */
    public static final int TTL_MILLIS = 1000*60*60*24*7;
    /**
     * 生成签名密钥
     */
    private static final String SECURITY = "AED34B9F60EE115DFA7918B742336277";

    /**
     * jwt解密，需要密钥和token，如果解密失败，说明token无效
     * @param jsonWebToken token
     * @return 解密后信息
     */
    public static Claims parseJwt(String jsonWebToken) {
        if (jsonWebToken == null || jsonWebToken.equals("")) {
            return null;
        }
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECURITY))
                    .parseClaimsJws(jsonWebToken)
                    .getBody();
            return claims;
        } catch (JwtException ex) {
            return null;
        }
    }

    /**
     * 创建token
     *
     * @param map 主题，也差不多是个人的一些信息，为了好的移植，采用了map放个人信息，而没有采用JSON
     * @return
     */
    public static String createJwt(Map map) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成签名密钥 就是一个base64加密后的字符串？
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECURITY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());


        // 添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .setIssuedAt(now)
                .setAudience(map.get("username").toString())
                .setClaims(map)
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, signingKey);

        // 添加Token过期时间
        if (TTL_MILLIS >= 0) {
            // 过期时间
            long expMillis = nowMillis + TTL_MILLIS;
            // 现在是什么时间
            Date exp = new Date(expMillis);
            // 系统时间之前的token都是不可以被承认的
            builder.setExpiration(exp).setNotBefore(now);
        }
        //生成JWT
        return builder.compact();
    }
}
