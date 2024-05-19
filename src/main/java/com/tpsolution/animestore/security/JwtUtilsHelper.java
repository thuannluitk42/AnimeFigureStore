package com.tpsolution.animestore.security;

import com.tpsolution.animestore.constant.StringConstant;
import com.tpsolution.animestore.dto.UserResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.tpsolution.animestore.utils.CommonUtils.extractUsernameFromEmail;

@Slf4j
@Component
public class JwtUtilsHelper {
    @Value("${jwt.privateKey}")
    private String privateKey;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";

    /*=====================================generateToken oauth start =====================================*/
    //Method to create AccessToken and RefreshToken with Authentication
    public String generateTokenOauth(Authentication authentication){
        return generateTokenOauth(authentication.getName(), authentication.getAuthorities());
    }

    //Method to create AccessToken and RefreshToken with name and authorities
    public String generateTokenOauth(String name, Collection<? extends GrantedAuthority> inputAuthorities) {
        //Get permission
        String authorities = inputAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();

        //Generate AccessToken
        String accessToken = Jwts.builder()
                .setSubject(name)
                .claim(AUTHORITIES_KEY, authorities)
                .claim("type", TYPE_ACCESS)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + StringConstant.ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

        //Generate RefreshToken
        String refreshToken = Jwts.builder()
                .claim("type", TYPE_REFRESH)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + StringConstant.REFRESH_TOKEN_EXPIRE_TIME)) //토큰 만료 시간 설정
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

        return accessToken;
    }
    /*===================================== generateToken oauth end =====================================*/

    /*===================================== generateToken normal start =====================================*/
    public String generateToken(String userName){
        extractUsernameFromEmail(userName);
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                //Token issuance time information
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //Set token expiration time
                .setExpiration(new Date(System.currentTimeMillis() + StringConstant.ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    /*===================================== generateToken normal end =====================================*/

    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(privateKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

