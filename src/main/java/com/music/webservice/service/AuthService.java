package com.music.webservice.service;


import com.music.webservice.dto.request.AuthenticationRequest;
import com.music.webservice.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class AuthService {


//    @Value("${JWT_EXPIRATION}")
//    private Long JWT_EXPIRATION;

    public ResponseResult login(AuthenticationRequest input) {

        if (input.getPassword().equals("admin") || input.getUserName().equals("admin")) {
//            log.info(callId + " - AuthService.login: thieu thong tin dau vao");
            return ResponseResult.isSuccessSimple();
        } else return ResponseResult.failed("Sai mật khẩu hoặc tên đăng nhập");
    }


}
