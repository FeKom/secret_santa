package com.github.fekom.secret_santa.controller.group;

import com.github.fekom.secret_santa.apiResponse.AddParticipantsResponse;
import com.github.fekom.secret_santa.apiResponse.CreateGroupResponse;
import com.github.fekom.secret_santa.model.dto.group.CreateGroupDTO;
import com.github.fekom.secret_santa.model.dto.user.ParticiapantDto;
import com.github.fekom.secret_santa.service.GroupService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/api/group")
    public ResponseEntity<CreateGroupResponse> createGroup(@RequestBody CreateGroupDTO dto, JwtAuthenticationToken token) {
        var response =  groupService.createGroup(dto, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    
    @PostMapping("/api/group/{groupId}/add-participants")
    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    public ResponseEntity<AddParticipantsResponse> addParticipants(@PathVariable Long groupId, @RequestBody ParticiapantDto dto, JwtAuthenticationToken token) {
        var response = groupService.addParticipants(groupId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);


    }
}
