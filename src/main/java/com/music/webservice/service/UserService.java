package com.music.webservice.service;


import com.music.webservice.model.User;
import com.music.webservice.repositpry.UserRepository;
import com.music.webservice.response.ResponseCode;
import com.music.webservice.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService  {

    @Autowired
    private UserRepository userRepository;

    public ResponseResult saveOrUpdate(User user) {

        user = userRepository.save(user);
        return ResponseResult.isSuccess(ResponseCode.SuccessCode.SUCCESS, user);
    }

    public ResponseResult findByUsername(String username) {

        User user = userRepository.findByUsername(username);
        return ResponseResult.isSuccess(ResponseCode.SuccessCode.SUCCESS, user);
    }


    public void save(User user) {
        userRepository.save(user);
    }


}
