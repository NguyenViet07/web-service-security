package com.music.webservice.service;


import com.music.webservice.model.User;
import com.music.webservice.repositories.UserRepository;
import com.music.webservice.response.ResponseCode;
import com.music.webservice.response.ResponseResult;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserService  {

    @Autowired
    @Qualifier("jasyptStringEncryptor")
    private StringEncryptor stringEncryptor;

    @Autowired
    private UserRepository userRepository;

    public ResponseResult saveOrUpdate(User user) {

        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            return ResponseResult.isSuccess(ResponseCode.SuccessCode.ERR_INPUT, user);
        }
        String passDecode = stringEncryptor.encrypt(user.getPassword());
        user.setPassword(passDecode);
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
