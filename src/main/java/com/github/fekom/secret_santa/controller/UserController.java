package com.github.fekom.secret_santa.controller;


import com.github.fekom.secret_santa.model.UserModel;
import com.github.fekom.secret_santa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return  ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

}
