package com.mathstack.admin.domain.repository

import com.mathstack.admin.domain.model.AdminChallenge
import java.util.UUID

interface AdminChallengeRepository {
    fun create(challenge: AdminChallenge): AdminChallenge
    fun listAll(): List<AdminChallenge>
    fun findById(id: UUID): AdminChallenge?
    fun update(challenge: AdminChallenge): AdminChallenge
}
