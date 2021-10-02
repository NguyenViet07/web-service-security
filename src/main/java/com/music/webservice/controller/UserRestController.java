package com.music.webservice.controller;


import com.music.webservice.dto.request.UserDto;
import com.music.webservice.model.User;
import com.music.webservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity createUser(@RequestBody User user){
        return new ResponseEntity(userService.saveOrUpdate(user), HttpStatus.CREATED);
    }

    @PostMapping("/find-by-user-name")
    public ResponseEntity findByUsername(@RequestBody UserDto userDto){
        return new ResponseEntity(userService.findByUsername(userDto.getUsername()), HttpStatus.CREATED);
    }


}
