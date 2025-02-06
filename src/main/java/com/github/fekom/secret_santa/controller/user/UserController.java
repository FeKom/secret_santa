package com.github.fekom.secret_santa.controller.user;


import com.github.fekom.secret_santa.apiResponse.GetAllParticipantsByGroupResponse;
import com.github.fekom.secret_santa.apiResponse.RegisterResponse;
import com.github.fekom.secret_santa.model.dto.user.CreateUserDTO;
import com.github.fekom.secret_santa.entity.RoleEntity;
import com.github.fekom.secret_santa.entity.UserEntity;
import com.github.fekom.secret_santa.repository.GroupRepository;
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
import java.util.stream.Collectors;

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
    GroupRepository groupRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/api/register")
    @Transactional
    public ResponseEntity<RegisterResponse> newUser(@Valid @RequestBody CreateUserDTO dto) {

        var basicRole = roleRepository.findByRoleName(RoleEntity.Values.BASIC.name());
        var userFromDb = userRepository.findByEmail(dto.email());

        if(userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var userModel = new UserEntity();
        userModel.setEmail(dto.email());
        userModel.setPassword(passwordEncoder.encode((dto.password())));
        userModel.setRoles(of(basicRole));

        userRepository.save(userModel);

        RegisterResponse response = new RegisterResponse(userModel.getName(),userModel.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }



    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return  ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }



   @GetMapping("/api/group/{groupId}/participants")
   public ResponseEntity<List<GetAllParticipantsByGroupResponse>>getAllParticipantsByGroup(@PathVariable long groupId) {

        var group = groupRepository.findById(groupId).orElseThrow(()-> new RuntimeException("Group NOT FOUND"));

        List<UserEntity> participants = group.getUser();

        List<GetAllParticipantsByGroupResponse> participantsDto = participants.stream()
                .map(user -> new GetAllParticipantsByGroupResponse(group.getGroupId(), group.getName(), user.getUserId(), user.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(participantsDto);

   }
    
    

}
