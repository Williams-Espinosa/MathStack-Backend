package com.mathstack.social.application

import com.mathstack.social.domain.model.Group
import com.mathstack.social.domain.model.GroupMember
import com.mathstack.social.domain.repository.GroupRepository
import java.time.LocalDateTime
import java.util.UUID

data class CreateGroupCommand(
    val creatorId: UUID,
    val name: String,
    val description: String?,
    val subject: String,
    val maxMembers: Int
)

class CreateGroupUseCase(
    private val groupRepository: GroupRepository
) {
    operator fun invoke(command: CreateGroupCommand): Group {
        val group = Group(
            id = UUID.randomUUID(),
            name = command.name,
            description = command.description,
            subject = command.subject,
            maxMembers = command.maxMembers,
            creatorId = command.creatorId,
            createdAt = LocalDateTime.now()
        )
        
        val createdGroup = groupRepository.createGroup(group)
        
        // Add creator as member
        groupRepository.addMember(
            GroupMember(
                groupId = createdGroup.id,
                userId = command.creatorId,
                role = "CREATOR",
                joinedAt = LocalDateTime.now()
            )
        )
        
        return createdGroup
    }
}
