package com.music.webservice.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.webservice.dto.response.UserDataDto;
import com.music.webservice.response.ResponseCode;
import io.jsonwebtoken.*;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@Slf4j
@NoArgsConstructor
public class JwtTokenProvider {

    @Autowired
    private Environment env;

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;

    @Value("${JWT_EXPIRATION}")
    private Long JWT_EXPIRATION;
//    = Long.parseLong(env.getProperty("JWT_EXPIRATION"));

    private ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public String generateToken(UserDataDto userData) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        String jsonObject = objectMapper.writeValueAsString(userData);
        return Jwts.builder().setSubject(jsonObject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    @SneakyThrows
    public UserDataDto getUserDataFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return objectMapper.readValue(claims.getSubject(), UserDataDto.class);
    }

    public boolean validateToken(String authToken) throws JwtException {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: " + ex);
            throw new JwtException(ResponseCode.ErrorCode.ERR_INVALID_TOKEN);
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token: " + ex);
            throw new JwtException(ResponseCode.ErrorCode.ERR_EXPIRED_TOKEN);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: " + ex);
            throw new JwtException(ResponseCode.ErrorCode.ERR_TOKEN_NOT_SUPPORT);
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: " + ex);
            throw new JwtException(ResponseCode.ErrorCode.ERR_TOKEN_EMPTY);
        }
        return true;
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public UserDataDto getUserDataFromRequest(HttpServletRequest request) {
        String token = getJwtFromRequest(request);
        return getUserDataFromJWT(token);
    }



}
