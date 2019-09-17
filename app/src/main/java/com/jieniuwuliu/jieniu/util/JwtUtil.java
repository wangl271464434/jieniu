package com.jieniuwuliu.jieniu.util;

import android.util.Base64;

import java.util.Map;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

public class JwtUtil {
    public static String JWTParse(String jwt){
        String key = Base64.encodeToString("jieniuwuliu".getBytes(), 0);
        //Key key = MacProvider.generateKey(SignatureAlgorithm.HS256);
        //在解析的时候一定要传key进去，否则无法通过key的认证
        Jwt parse = Jwts.parser().setSigningKey(key).parse(jwt);
        Header header = parse.getHeader();
        Map<String, Object> map = (Map<String, Object>) parse.getBody();
        String param = String.valueOf(map.get("uid"));
        return param;
    }
}
