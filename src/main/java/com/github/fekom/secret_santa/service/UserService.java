package com.github.fekom.secret_santa.service;

import com.github.fekom.secret_santa.apiResponse.GetAllParticipantsByGroupResponse;
import com.github.fekom.secret_santa.apiResponse.RegisterResponse;
import com.github.fekom.secret_santa.entity.RoleEntity;
import com.github.fekom.secret_santa.entity.UserEntity;
import com.github.fekom.secret_santa.model.dto.user.CreateUserDTO;
import com.github.fekom.secret_santa.repository.GroupRepository;
import com.github.fekom.secret_santa.repository.RoleRepository;
import com.github.fekom.secret_santa.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.List.of;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       GroupRepository groupRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponse newUser(@Valid @RequestBody CreateUserDTO dto) {

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

        return new  RegisterResponse(userModel.getName(),userModel.getUserId());

    }

    public List<GetAllParticipantsByGroupResponse> getAllParticipantsByGroup(Long groupId) {

        var group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group NOT FOUND"));
        List<UserEntity> participants = group.getUser();

        return participants.stream()
                .map(user -> new GetAllParticipantsByGroupResponse(
                        group.getGroupId(),
                        group.getName(),
                        user.getUserId(),
                        user.getName()))
                .collect(Collectors.toList());
    }


    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

}
