package com.github.fekom.secret_santa.controller;

import com.github.fekom.secret_santa.dtos.CreateGroupDTO;
import com.github.fekom.secret_santa.dtos.ParticiapantDto;
import com.github.fekom.secret_santa.model.GroupModel;
import com.github.fekom.secret_santa.model.Role;
import com.github.fekom.secret_santa.repository.GroupRepository;
import com.github.fekom.secret_santa.repository.RoleRepository;
import com.github.fekom.secret_santa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;


@RestController
public class GroupController {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/api/group")
    public ResponseEntity<Void> createGroup(@RequestBody CreateGroupDTO dto, JwtAuthenticationToken token) {

        var basicRole = roleRepository.findByRoleName(Role.Values.OWNER.name());

        var user = userRepository.findById(UUID.fromString(token.getName())).orElseThrow(() -> new RuntimeException("User not Found!"));

        var groupModel = new GroupModel();
        groupModel.setUser(Collections.singletonList(user));
        groupModel.setRoles(Collections.singletonList(basicRole));
        groupModel.setName(dto.name());
        groupModel.setDescription(dto.Description());
        groupModel.setPreferences(dto.preferences());
    

        groupRepository.save(groupModel);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    
    @PostMapping("/{groupId}/participants")
    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    public ResponseEntity<Void> addParticipants(@PathVariable Long groupId, @RequestBody ParticiapantDto dto, JwtAuthenticationToken token) {

        var user = userRepository.findById(UUID.fromString(token.getName())).orElseThrow(() -> new RuntimeException("User not Found!"));

        var group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not Found"));

        var participants = userRepository.findById(dto.userId()).orElseThrow(() -> new RuntimeException("User not found!"));

        if (group.getUser().contains(participants)) {
            throw new RuntimeException("Participant is already in this group!");
            
        }

        group.getUser().add(participants);
        groupRepository.save(group);

        return ResponseEntity.status(HttpStatus.OK).build();


    }
}
