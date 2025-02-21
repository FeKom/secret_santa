package com.github.fekom.secret_santa.service;


import com.github.fekom.secret_santa.utils.AddParticipantsResponse;
import com.github.fekom.secret_santa.utils.CreateGroupResponse;
import com.github.fekom.secret_santa.entity.GroupEntity;
import com.github.fekom.secret_santa.entity.RoleEntity;
import com.github.fekom.secret_santa.model.dto.group.CreateGroupDTO;
import com.github.fekom.secret_santa.model.dto.user.ParticipantDto;
import com.github.fekom.secret_santa.repository.GroupRepository;
import com.github.fekom.secret_santa.repository.RoleRepository;
import com.github.fekom.secret_santa.repository.UserRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.UUID;

@Service
public class GroupService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public GroupService(RoleRepository roleRepository,
                        UserRepository userRepository,
                        GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

    }

    public CreateGroupResponse createGroup(CreateGroupDTO dto, JwtAuthenticationToken token) {

        var basicRole = roleRepository.findByRoleName(RoleEntity.Values.OWNER.name());

        var user = userRepository.findById(UUID.fromString(token.getName())).orElseThrow(() -> new RuntimeException("User not Found!"));
        user.setRoles(Collections.singletonList(basicRole));

        var groupModel = new GroupEntity();
        groupModel.setUser(Collections.singletonList(user));
        groupModel.setName(dto.name());
        groupModel.setDescription(dto.Description());
        groupModel.setPreferences(dto.preferences());


        groupRepository.save(groupModel);

         return new CreateGroupResponse(groupModel.getGroupId(), "Group created successfully!");
    }

    public AddParticipantsResponse addParticipants(@PathVariable Long groupId, ParticipantDto dto) {

        var group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not Found"));

        var participants = userRepository.findById(dto.userId()).orElseThrow(() -> new RuntimeException("User not found!"));

        if (group.getUser().contains(participants)) {
            throw new RuntimeException("Participant is already in this group!");

        }

        group.getUser().add(participants);
        groupRepository.save(group);

        return new AddParticipantsResponse(groupId, dto.userId(), "Participant added successfully!");

    }
}
