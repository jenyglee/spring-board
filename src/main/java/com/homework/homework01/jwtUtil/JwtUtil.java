package com.homework.homework01.jwtUtil;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization"; // 헤더에 들어가는 키값
    public static final String AUTHORIZATION_KEY = "auth"; // 사용자 권한 키값. 사용자 권한도 토큰안에 넣어주기 때문에 그때 사용하는 키값
    private static final String BEARER_PREFIX = "Bearer "; // Token 식별자
    private static final long TOKEN_TIME = 60 * 60 * 1000L; // 토큰 만료시간. (60 * 1000L 이 1분)

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key; // 토큰을 만들 때 넣어줄 키값
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; // 이 알고리즘을 사용해서 키 객체를 암호화할 것이다.

    @PostConstruct // ✨클래스가 service(로직을 탈 때)를 수행하기 전에 발생한다. 이 메서드는 다른 리소스에서 호출되지 않는다해도 실행된다.
    public void init(){
        //✨secretKey는 Base64로 인코딩되어 있기 때문에, 값을 가지고와서 디코드 하는 과정이다.(getDecoder().decode()) 반환값 byte[]이다.
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        //✨key 객체에 넣어줄 때는 hmacShaKeyFor() 메서드를 사용해야한다.
        key = Keys.hmacShaKeyFor(bytes);
    }

    // header 토큰을 가져오기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken= request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 생성
    public String createToken(String username){
        Date date = new Date();
        return  BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // subject는 어떠한 공간이고 그 공간에 username을 넣어준다.
                        // .claim(AUTHORIZATION_KEY) // claim 공간에는 사용자의 권한을 넣어준다.
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 토큰을 언제까지 유효하게 가져갈건지 지정한다.
                        .setIssuedAt(date) // 이 토큰이 언제 만들어졌는지 넣어주는 부분
                        .signWith(key, signatureAlgorithm) // 토큰을 만들때 넣어주는 key 객체(디코딩됨)와 암호화 할 알고리즘을 넣어준다.
                        .compact(); // String 형식의 jwt 토큰으로 반환이 되어진다.
    }


    // // 토큰 검증
    // public boolean validateToken(String token) {
    //     try {
    //         Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    //         return true;
    //     } catch (SecurityException | MalformedJwtException e) {
    //
    //         log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
    //     } catch (ExpiredJwtException e) {
    //         log.info("Expired JWT token, 만료된 JWT token 입니다.");
    //     } catch (UnsupportedJwtException e) {
    //         log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
    //     } catch (IllegalArgumentException e) {
    //         log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
    //     }
    //     return false;
    // }
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (SecurityException | MalformedJwtException e){
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        }catch (ExpiredJwtException e){
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        }catch (UnsupportedJwtException e){
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        }catch (IllegalArgumentException e){
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }


    //
    // // 토큰에서 사용자 정보 가져오기
    // public Claims getUserInfoFromToken(String token) {
    //     return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    // }
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
