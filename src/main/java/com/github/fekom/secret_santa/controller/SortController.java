package com.github.fekom.secret_santa.controller;


import com.github.fekom.secret_santa.entity.DrawEntity;
import com.github.fekom.secret_santa.entity.UserEntity;
import com.github.fekom.secret_santa.model.dto.draw.*;
import com.github.fekom.secret_santa.repository.DrawRepository;
import com.github.fekom.secret_santa.repository.GroupRepository;
import com.github.fekom.secret_santa.repository.RoleRepository;
import com.github.fekom.secret_santa.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping()
@Tag(name = "Sort", description = "Endpoints for Draw")
public class SortController {
     @Autowired
     private DrawRepository drawRepository;
     @Autowired
     private UserRepository userRepository;
     @Autowired
     private GroupRepository groupRepository;
     @Autowired
     private RoleRepository roleRepository;

      @GetMapping("/api/group/{groupId}/sort")
      @Operation(
              summary = "Sort Participants of a Group",
              description = "Sort the participants of a group in a random order and return the result"
      )
      @ApiResponses(value = {
              @ApiResponse(responseCode = "200", description = "Sorted Participants",
                      content = {@Content(
                              schema = @Schema(implementation = SortResponse.class),
                              mediaType = "application/json"
                      )}
              ),
              @ApiResponse(responseCode = "400", description = "Bad Request - Group has fewer than 2 participants!",
                      content = {@Content}
              ),
              @ApiResponse(responseCode = "404", description = "Group Not Found!",
                      content = {@Content(
                              schema = @Schema(defaultValue = "Group with the provided ID does not exist")
                      )}
              ),
              @ApiResponse(responseCode = "500", description = "Internal Server Error!",
                      content = {@Content}
              )
      })
      public ResponseEntity<SortResponse> SortParticipants(@PathVariable long groupId) {

          var group = groupRepository.findById(groupId)
                  .orElseThrow(() -> new RuntimeException("Group Not Found!"));

          List<UserEntity> participants = new ArrayList<>(group.getUser());

          if (participants.size() < 2) {
              return ResponseEntity.badRequest().build();
          }

          ArrayList<UserEntity> participantsShuffled = new ArrayList<>(participants);
          Collections.shuffle(participantsShuffled);

          List<ParticipantsDrawResponse> participantsResponse = new ArrayList<>();

          var drawId = 0;
          for (int i = 0; i < participantsShuffled.size(); i++) {
              UserEntity drawer = participants.get(i);
              UserEntity drawn = participantsShuffled.get(i);

              participantsResponse.add(new ParticipantsDrawResponse(
                      new UserDrawResponse(drawer.getUserId(), drawer.getName()),
                      new UserDrawResponse(drawn.getUserId(), drawn.getName())
              ));

              DrawEntity draw = new DrawEntity();
              draw.setDrawer(drawer);
              draw.setDrawn(drawn);
              draw.setGroup(group);

              draw = drawRepository.save(draw);
          }

          GroupDrawResponse groupDrawResponse = new GroupDrawResponse(
                  groupId,
                  group.getName(),
                  participantsResponse
          );

          DrawResponse drawResponse = new DrawResponse((long) drawId, groupDrawResponse);

          SortResponse sortResponse = new SortResponse(drawResponse);

          return ResponseEntity.ok(sortResponse);
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
