package com.github.fekom.secret_santa.ApiResponse;

import java.util.UUID;

public record AddParticipantsResponse(Long groupId, UUID participantId, String m) {
}
