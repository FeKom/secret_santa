package com.github.fekom.secret_santa.apiResponse;

import java.util.UUID;

public record GetAllParticipantsByGroupResponse(Long groupId, String groupName,  UUID particiapantId, String userName) {
}
