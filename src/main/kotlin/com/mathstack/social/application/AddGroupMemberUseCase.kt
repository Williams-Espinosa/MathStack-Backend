package com.mathstack.social.application

import com.mathstack.social.domain.model.GroupMember
import com.mathstack.social.domain.repository.GroupRepository
import com.mathstack.users.domain.repository.UserRepository
import com.mathstack.shared.domain.exception.NotFoundException
import java.time.LocalDateTime
import java.util.UUID

data class AddGroupMemberCommand(
    val groupId: UUID,
    val identifier: String // Can be email or username
)

class AddGroupMemberUseCase(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(command: AddGroupMemberCommand): GroupMember {
        val group = groupRepository.getGroupById(command.groupId)
            ?: throw NotFoundException("Group not found")
            
        // Find user by email or username
        val user = if (command.identifier.contains("@")) {
            userRepository.findUserByEmail(command.identifier.trim().lowercase())
        } else {
            userRepository.findUserByUsername(command.identifier.trim())
        }
        
        if (user == null) {
            throw NotFoundException("User not found with the provided identifier")
        }
        
        if (groupRepository.isMember(group.id, user.id)) {
            throw IllegalArgumentException("User is already a member of this group")
        }
        
        if (groupRepository.getMemberCount(group.id) >= group.maxMembers) {
            throw IllegalArgumentException("Group is full")
        }
        
        val member = GroupMember(
            groupId = group.id,
            userId = user.id,
            role = "MEMBER",
            joinedAt = LocalDateTime.now()
        )
        
        return groupRepository.addMember(member)
    }
}
