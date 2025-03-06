package com.github.fekom.secret_santa.model.dto.draw;

import java.util.List;

public record GroupDrawResponse(Long groupId, String name, List<ParticipantsDrawResponse> participants) {
}
