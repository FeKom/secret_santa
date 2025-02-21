package com.github.fekom.secret_santa.utils;

import java.util.UUID;

public record AddParticipantsResponse(Long groupId, UUID participantId, String m) {
}
