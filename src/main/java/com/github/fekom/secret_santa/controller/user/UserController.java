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
import org.apache.catalina.User;
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

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/register")
    @Transactional
    public ResponseEntity<RegisterResponse> newUser(@Valid @RequestBody CreateUserDTO dto) {

        var response = userService.newUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }



    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return  ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }



   @GetMapping("/api/group/{groupId}/participants")
   public ResponseEntity<List<GetAllParticipantsByGroupResponse>>getAllParticipantsByGroup(@PathVariable long groupId) {
        var participantsDto = userService.getAllParticipantsByGroup(groupId);
        return ResponseEntity.status(HttpStatus.OK).body(participantsDto);

   }


}
