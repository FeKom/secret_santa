package com.github.fekom.secret_santa.controller;


import com.github.fekom.secret_santa.dtos.CreateUserDTO;
import com.github.fekom.secret_santa.model.Role;
import com.github.fekom.secret_santa.model.UserModel;
import com.github.fekom.secret_santa.repository.RoleRepository;
import com.github.fekom.secret_santa.repository.UserRepository;
import com.github.fekom.secret_santa.service.UserService;
import jakarta.persistence.SecondaryTable;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

import static java.util.List.*;

@RestController
@RequestMapping()
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<UserModel> newUser(@Valid @RequestBody CreateUserDTO dto) {

        var basicRole = roleRepository.findByRoleName(Role.Values.BASIC.name());
        var userFromDb = userRepository.findByEmail(dto.email());

        if(userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var UserModel = new UserModel();
        UserModel.setEmail(dto.email());
        UserModel.setPassword(passwordEncoder.encode((dto.password())));
        UserModel.setRoles(of(basicRole));

        userRepository.save(UserModel);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/users")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return  ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

}
