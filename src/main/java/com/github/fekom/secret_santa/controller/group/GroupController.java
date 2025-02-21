package com.github.fekom.secret_santa.controller.group;

import com.github.fekom.secret_santa.utils.AddParticipantsResponse;
import com.github.fekom.secret_santa.utils.CreateGroupResponse;
import com.github.fekom.secret_santa.model.dto.group.CreateGroupDTO;
import com.github.fekom.secret_santa.model.dto.user.ParticipantDto;
import com.github.fekom.secret_santa.service.GroupService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "Groups", description = "Endpoints for managing Groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

   
    @PostMapping("/api/group")
    @Operation(summary = "Create a Group", description =  "Create a group based on id" )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Return a Created Group",
            content = {@Content(
                schema = @Schema(implementation = CreateGroupResponse.class),
                mediaType = "application/json"
            )} 
        ),
        @ApiResponse(responseCode = "401", description = "Not Authorized to create a group!",
                content = {@Content (
                        schema = @Schema(defaultValue = "You are not Authorized to compelte this action")
                )}
        ),
        @ApiResponse(responseCode = "500", description = "Internal Server Error!",
            content = {@Content}
        ),
    })
    public ResponseEntity<CreateGroupResponse> createGroup(@RequestBody CreateGroupDTO dto, JwtAuthenticationToken token) {
        var response =  groupService.createGroup(dto, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    
    
    @PostMapping("/api/group/{groupId}/add-participants")
    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Participant add with successfully",
            content = {@Content(
                schema = @Schema(implementation = AddParticipantsResponse.class),
                mediaType = "application/json"
            )} 
        ),
        @ApiResponse(responseCode = "401", description = "You are not owner of this Group!,",
                content = {@Content (
                        schema = @Schema(defaultValue = "You are not Authorized to compelte this action")
                )}
        ),
        @ApiResponse(responseCode = "404", description = "No participants were found!",
            content = {@Content}
        ),
    })
    public ResponseEntity<AddParticipantsResponse> addParticipants(@PathVariable Long groupId, @RequestBody ParticipantDto dto, JwtAuthenticationToken token) {
        var response = groupService.addParticipants(groupId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);


    }
}
