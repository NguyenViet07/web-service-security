package com.music.webservice.service;


import com.music.webservice.config.jwt.JwtTokenProvider;
import com.music.webservice.dto.request.AuthenticationRequest;
import com.music.webservice.dto.response.UserDataDto;
import com.music.webservice.model.Token;
import com.music.webservice.model.User;
import com.music.webservice.repositories.UserRepository;
import com.music.webservice.response.ResponseCode;
import com.music.webservice.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


@Service
@Slf4j
public class AuthService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Autowired
    private TokenService tokenService;

    @Autowired
    @Qualifier("jasyptStringEncryptor")
    private StringEncryptor stringEncryptor;

//    @Value("${JWT_EXPIRATION}")
//    private Long JWT_EXPIRATION;

    public ResponseResult login(AuthenticationRequest input) {

        User user = userRepository.findByUsername(input.getUserName());
        if (user == null) return ResponseResult.failed(ResponseCode.ErrorCode.ERR_LOGIN);

        if (stringEncryptor.decrypt(user.getPassword()).equals(input.getPassword())) {
            Token tokenObj = new Token();
            UserDataDto dataDto = new UserDataDto();
            dataDto.setUserId(user.getUserId());

            setInfoUserToken(dataDto, user);
            String token = jwtTokenProvider.generateToken(dataDto);
            tokenObj.setUserId(user.getUserId());
            tokenObj.setUsername(user.getUsername());
            tokenObj.setValue(token);
            tokenService.save(tokenObj);
            return ResponseResult.isSuccess(ResponseCode.SuccessCode.SUCCESS, dataDto);
        } else return ResponseResult.failed(ResponseCode.ErrorCode.ERR_LOGIN);

    }

    private void setInfoUserToken(UserDataDto userToken, User user) {
        if (userToken == null ) {
            return;
        }
        userToken.setUserName(user.getUsername());
        userToken.setFullName(user.getName());
    }

    public ResponseResult logout(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (org.springframework.util.StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            tokenService.deleteToken(bearerToken.substring(7));
            return ResponseResult.isSuccessSimple();
        }
        return ResponseResult.isSuccessSimple();
    }


}
