package com.mathstack.social.infrastructure.rest.dto

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CreateGroupRequest(
    val name: String,
    val description: String?,
    val subject: String,
    val maxMembers: Int
)

@Serializable
data class AddGroupMemberRequest(
    val identifier: String
)

@Serializable
data class GroupListResponse(
    val id: String,
    val name: String,
    val subject: String,
    val members: Int,
    val maxMembers: Int,
    val activeChallenges: Int,
    val color: String
)

@Serializable
data class GroupMemberResponse(
    val userId: String,
    val username: String,
    val role: String,
    val level: Int,
    val streak: Int,
    val xp: Int
)

@Serializable
data class GroupDetailsResponse(
    val id: String,
    val name: String,
    val description: String?,
    val subject: String,
    val maxMembers: Int,
    val activeChallenges: Int,
    val totalXp: Int,
    val color: String,
    val members: List<GroupMemberResponse>
)

@Serializable
data class GroupResponse(
    val id: String,
    val name: String,
    val description: String?,
    val subject: String,
    val maxMembers: Int,
    val creatorId: String,
    val activeChallenges: Int,
    val totalXp: Int,
    val color: String
)

fun com.mathstack.social.application.GroupListDto.toResponse() = GroupListResponse(
    id = id.toString(),
    name = name,
    subject = subject,
    members = members,
    maxMembers = maxMembers,
    activeChallenges = activeChallenges,
    color = color
)

fun com.mathstack.social.application.GroupMemberDto.toResponse() = GroupMemberResponse(
    userId = userId.toString(),
    username = username,
    role = role,
    level = level,
    streak = streak,
    xp = xp
)

fun com.mathstack.social.application.GroupDetailsDto.toResponse() = GroupDetailsResponse(
    id = id.toString(),
    name = name,
    description = description,
    subject = subject,
    maxMembers = maxMembers,
    activeChallenges = activeChallenges,
    totalXp = totalXp,
    color = color,
    members = members.map { it.toResponse() }
)

fun com.mathstack.social.domain.model.Group.toResponse() = GroupResponse(
    id = id.toString(),
    name = name,
    description = description,
    subject = subject,
    maxMembers = maxMembers,
    creatorId = creatorId.toString(),
    activeChallenges = activeChallenges,
    totalXp = totalXp,
    color = color
)
