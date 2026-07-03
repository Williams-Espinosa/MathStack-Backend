package com.mathstack.social.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class GroupMember(
    val groupId: UUID,
    val userId: UUID,
    val role: String, // "CREATOR", "MEMBER"
    val joinedAt: LocalDateTime
)
