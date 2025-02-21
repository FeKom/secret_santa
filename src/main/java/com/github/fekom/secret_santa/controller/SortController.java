package com.github.fekom.secret_santa.controller;


import com.github.fekom.secret_santa.entity.UserEntity;
import com.github.fekom.secret_santa.repository.GroupRepository;
import com.github.fekom.secret_santa.repository.RoleRepository;
import com.github.fekom.secret_santa.repository.UserRepository;
import com.github.fekom.secret_santa.utils.SortResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

public class SortController {
     @Autowired
     private UserRepository userRepository;
     @Autowired
     private GroupRepository groupRepository;
     @Autowired
     private RoleRepository roleRepository;

      @GetMapping("/api/group/{groupId}/sort")
      public ResponseEntity<SortResponse> SortParticipants(@PathVariable long groupId) {

          var group = groupRepository.findById(groupId)
                  .orElseThrow(() -> new RuntimeException("Group Not Found!"));

          List<UserEntity> participants = new ArrayList<>(group.getUser());

          if (participants.size() < 2) {
              return ResponseEntity.badRequest().build();
          }

          ArrayList<UserEntity> participantsShuffled = new ArrayList<>(participants);
          Collections.shuffle(participantsShuffled);

          HashMap<String, String> draw = new HashMap<>();
          for (int i = 0; i < participantsShuffled.size(); i++) {
              draw.put(participants.get(i).getName(), participantsShuffled.get(i).getName());
              System.out.println(draw);
          }

          SortResponse response = new SortResponse(draw);

          return ResponseEntity.ok(response);
      }

//    public static void lista() {
//        int[] g = {0, 1, 2, 3, 4, 5, 6};
//        shuffle(g);
//        for(int i = 0; i < g.length; i++) {
//            System.out.println(g[i] + "" );
//        }
//    }
//
//    public static void shuffle(int[] a){
//        Random rnd = new Random();
//        for(int i = a.length - 1; i > 0; i--) {
//            int randomNum = rnd.nextInt(i + 1);
//            swap(a, i, randomNum);
//        }
//    }
//    public static void swap(int [] a, int i, int j) {
//        int temp = a[i];
//        a[i] = a[j];
//        a[j] = temp;
//    }

}
