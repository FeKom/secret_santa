package com.github.fekom.secret_santa.controller;


import com.github.fekom.secret_santa.ApiResponse.RegisterResponse;
import com.github.fekom.secret_santa.dtos.CreateUserDTO;
import com.github.fekom.secret_santa.model.Role;
import com.github.fekom.secret_santa.model.UserModel;
import com.github.fekom.secret_santa.repository.RoleRepository;
import com.github.fekom.secret_santa.repository.UserRepository;
import com.github.fekom.secret_santa.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static java.util.List.*;
import org.springframework.web.bind.annotation.GetMapping;


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

    @PostMapping("/api/register")
    @Transactional
    public ResponseEntity<RegisterResponse> newUser(@Valid @RequestBody CreateUserDTO dto) {

        var basicRole = roleRepository.findByRoleName(Role.Values.BASIC.name());
        var userFromDb = userRepository.findByEmail(dto.email());

        if(userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var userModel = new UserModel();
        userModel.setEmail(dto.email());
        userModel.setPassword(passwordEncoder.encode((dto.password())));
        userModel.setRoles(of(basicRole));

        userRepository.save(userModel);

        RegisterResponse response = new RegisterResponse(userModel.getName(),userModel.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }



    @GetMapping("/users")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return  ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

//    @GetMapping("/{groupId}/participants")
//    public ResponseEntity<List<GroupModel>>getAllParticipantsGroup(@PathVariable long groupId) {
//
//    }
    
    

}
