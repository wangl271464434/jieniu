package com.jieniuwuliu.jieniu.util;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;

import com.jieniuwuliu.jieniu.LoginActivity;

import java.util.Map;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

public class JwtUtil {
    public static String JWTParse(Context context, String jwt){
        try{
            String key = Base64.encodeToString("jieniuwuliu".getBytes(), 0);
            //Key key = MacProvider.generateKey(SignatureAlgorithm.HS256);
            //在解析的时候一定要传key进去，否则无法通过key的认证
            Jwt parse = Jwts.parser().setSigningKey(key).parse(jwt);
            Header header = parse.getHeader();
            Map<String, Object> map = (Map<String, Object>) parse.getBody();
            String param = String.valueOf(map.get("uid"));
            return param;
        }catch(ExpiredJwtException e){
            e.printStackTrace();
            Intent intent = new Intent();
            intent.setClass(context,LoginActivity.class);
            context.startActivity(intent);
        }
        return "";
    }
}
