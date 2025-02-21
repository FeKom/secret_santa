package com.github.fekom.secret_santa.controller.user;


import com.github.fekom.secret_santa.model.dto.user.ForgotPasswordDto;
import com.github.fekom.secret_santa.utils.GetAllParticipantsByGroupResponse;
import com.github.fekom.secret_santa.utils.RegisterResponse;
import com.github.fekom.secret_santa.model.dto.user.CreateUserDTO;
import com.github.fekom.secret_santa.entity.UserEntity;
import com.github.fekom.secret_santa.service.UserService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
@Tag(name = "Users", description = "Endpoints for managing Users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/register")
    @Transactional
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Return a user Created",
            content = {@Content(
                schema = @Schema(implementation = RegisterResponse.class),
                mediaType = "application/json"
            )} 
        ),
        @ApiResponse(responseCode = "422", description = "already registered user!",
                content = {@Content (
                        schema = @Schema(defaultValue = "already registered user, do you want reset your password? ")
                )}
        ),
    })
    public ResponseEntity<RegisterResponse> newUser(@Valid @RequestBody CreateUserDTO dto) {
        var response = userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }


   @GetMapping("/api/group/{groupId}/participants")
   @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Get all Participants by id Group!",
            content = {@Content(
                schema = @Schema(implementation = GetAllParticipantsByGroupResponse.class),
                mediaType = "application/json"
            )} 
        ),
        @ApiResponse(responseCode = "401", description = "You are not owner of this Group!,",
                content = {@Content (
                        schema = @Schema(defaultValue = "You are not Authorized to compelte this action")
                )}
        ),
        @ApiResponse(responseCode = "200", description = "No participants were found!",
            content = {@Content}
        ),
    })
   public ResponseEntity<List<GetAllParticipantsByGroupResponse>>getAllParticipantsByGroup(@PathVariable long groupId) {
        var participantsDto = userService.getAllParticipantsByGroup(groupId);
        return ResponseEntity.status(HttpStatus.CREATED).body((List<GetAllParticipantsByGroupResponse>) participantsDto);

   }

   @PostMapping("/api/forgot-password")
   public void forgotPassword(@RequestBody ForgotPasswordDto dto) {
   }

   @GetMapping("/users")
   public ResponseEntity<List<UserEntity>> getAllUsers() {
       return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
   }

}
