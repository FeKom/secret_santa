package com.github.fekom.secret_santa.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.github.fekom.secret_santa.apiResponse.SortResponse;
import com.github.fekom.secret_santa.entity.UserEntity;
import com.github.fekom.secret_santa.repository.GroupRepository;
import com.github.fekom.secret_santa.repository.RoleRepository;
import com.github.fekom.secret_santa.repository.UserRepository;

public class SortController {
//     @Autowired
//     private UserRepository userRepository;
//     @Autowired
//     private GroupRepository groupRepository;
//     @Autowired
//     private RoleRepository roleRepository;

// //     @GetMapping("/api/group/{groupid}/sort")
// //     public ResponseEntity<SortResponse>SortParticipants(@PathVariable long groupid){

// //     //     var group = groupRepository.findById(groupid).orElseThrow(()-> new RuntimeException("Group Not Found!"));
// //     //     List<UserEntity> participants = new ArrayList<>(group.getUser());

// //     //     if(participants.size() < 2){
// //     //         return ResponseEntity.badRequest().build();
// //     //     }

// //     //     List<UserEntity> participantsShuffled = new ArrayList<>(participants);
// //     //       Collections.shuffle(participantsShuffled);

// //     //     for (int i = 0; i < participantsShuffled.size(); i++) {
// //     //        if (participantsShuffled.get(i).getUserId().equals(participants.get(i).getUserId())) {
// //     //             Collections.swap(participantsShuffled, 0, 2);
// //     //        }
// //     //     }

// //     //     List<SortResponse> = participantsShuffled.stream().map

      
            
// //         return void
// //     }

}
