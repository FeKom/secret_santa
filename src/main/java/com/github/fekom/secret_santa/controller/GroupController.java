package com.github.fekom.secret_santa.controller;

import com.github.fekom.secret_santa.dtos.CreateGroupDTO;
import com.github.fekom.secret_santa.model.GroupModel;
import com.github.fekom.secret_santa.model.UserModel;
import com.github.fekom.secret_santa.repository.GroupRepository;
import com.github.fekom.secret_santa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
public class GroupController {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/api/group")
    public ResponseEntity<Void> createGroup(@RequestBody CreateGroupDTO dto, JwtAuthenticationToken token) {

        var user = userRepository.findById(UUID.fromString(token.getName()));

        var groupModel = new GroupModel();

        groupModel.setUser(Collections.singletonList(user.get()));
        groupModel.setName(dto.name());
        groupModel.setDescription(dto.Description());
        groupModel.setPreferences(dto.preferences());

        groupRepository.save(groupModel);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
