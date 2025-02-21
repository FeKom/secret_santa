package com.github.fekom.secret_santa.utils;

public record GetAllParticipantsByGroupResponse(Long groupId, String groupName, java.util.List<ParticipantGroupDto> participants) {
}


// {
//     "groupId": 6,
//     "groupName": "Corinthians",
//     "participants":[
//         {
//             "particiapantId": "b5ab80a9-21c1-4cd5-a18b-9161f050d4e2",
//             "userName": "memphys"
//         },
//         {
//             "particiapantId": "b151b105-51f0-4cd1-a82f-35802245a5c2",
//             "userName": "hariel"
//         }
//     ]
    
// }

  
//