package com.mathstack.social.application

import com.mathstack.social.domain.model.Group
import com.mathstack.social.domain.repository.GroupRepository
import com.mathstack.users.domain.repository.UserRepository
import java.util.UUID

data class GroupListDto(
    val id: UUID,
    val name: String,
    val subject: String,
    val members: Int,
    val maxMembers: Int,
    val activeChallenges: Int,
    val color: String
)

class ListGroupsUseCase(
    private val groupRepository: GroupRepository
) {
    operator fun invoke(userId: UUID): List<GroupListDto> {
        val groups = groupRepository.getGroupsByUserId(userId)
        
        return groups.map { group ->
            val memberCount = groupRepository.getMemberCount(group.id)
            GroupListDto(
                id = group.id,
                name = group.name,
                subject = group.subject,
                members = memberCount,
                maxMembers = group.maxMembers,
                activeChallenges = group.activeChallenges,
                color = group.color
            )
        }
    }
}
