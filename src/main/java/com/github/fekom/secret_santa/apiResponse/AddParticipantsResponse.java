package com.github.fekom.secret_santa.apiResponse;

import java.util.UUID;

public record AddParticipantsResponse(Long groupId, UUID participantId, String m) {
}
