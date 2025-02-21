package com.github.fekom.secret_santa.service;

import com.github.fekom.secret_santa.utils.GetAllParticipantsByGroupResponse;
import com.github.fekom.secret_santa.utils.ParticipantGroupDto;
import com.github.fekom.secret_santa.utils.RegisterResponse;
import com.github.fekom.secret_santa.entity.RoleEntity;
import com.github.fekom.secret_santa.entity.UserEntity;
import com.github.fekom.secret_santa.model.dto.user.CreateUserDTO;
import com.github.fekom.secret_santa.repository.GroupRepository;
import com.github.fekom.secret_santa.repository.RoleRepository;
import com.github.fekom.secret_santa.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.List.of;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;
    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       GroupRepository groupRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public RegisterResponse registerUser(@Valid @RequestBody CreateUserDTO dto) {

        var basicRole = roleRepository.findByRoleName(RoleEntity.Values.BASIC.name());
        var userFromDb = userRepository.findByEmail(dto.email());

        if(userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var userEntity = new UserEntity();
        userEntity.setName(dto.name());
        userEntity.setEmail(dto.email());
        userEntity.setPassword(passwordEncoder.encode((dto.password())));
        userEntity.setRoles(of(basicRole));

        userRepository.save(userEntity);

        var now = Instant.now();
        var accessTokenExpiresIn = 300L;
        var refreshTokenExpiresIn = 604800L;

        var scopes = userEntity.getRoles()
                .stream()
                .map(RoleEntity::getRoleName)
                .collect(Collectors.joining(" "));

        var accessTokenClaims = JwtClaimsSet.builder()
                .issuer("secret-santa")
                .subject(userEntity.getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(accessTokenExpiresIn))
                .claim("scope", scopes)
                .build();

        var accessToken = jwtEncoder.encode(JwtEncoderParameters.from(accessTokenClaims)).getTokenValue();

        var refreshTokenClaims = JwtClaimsSet.builder()
                .issuer("secret-santa")
                .subject(userEntity.getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(refreshTokenExpiresIn))
                .build();

        var refreshToken = jwtEncoder.encode(JwtEncoderParameters.from(refreshTokenClaims)).getTokenValue();



        return new  RegisterResponse(userEntity.getName(),userEntity.getUserId(), accessToken, refreshToken);

    }

    public  ResponseEntity<GetAllParticipantsByGroupResponse> getAllParticipantsByGroup(Long groupId) {

        var group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group NOT FOUND"));
        List<UserEntity> participants = group.getUser();

	if(participants.isEmpty()){
		return ResponseEntity.noContent().build();
	}
      
        List<ParticipantGroupDto> parsedParticipants =  participants.stream()
                .map(user -> new ParticipantGroupDto(
                         user.getUserId().toString(),
                         user.getName()))
                .collect(Collectors.toList());



        GetAllParticipantsByGroupResponse response = new GetAllParticipantsByGroupResponse(
		group.getGroupId(),
		group.getName(),
		parsedParticipants
	    );
	return ResponseEntity.ok(response);
    }


    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

}
