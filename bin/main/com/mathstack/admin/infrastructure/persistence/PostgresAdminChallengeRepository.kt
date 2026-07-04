package com.mathstack.admin.infrastructure.persistence

import com.mathstack.admin.domain.model.AdminChallenge
import com.mathstack.admin.domain.repository.AdminChallengeRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class PostgresAdminChallengeRepository : AdminChallengeRepository {
    override fun create(challenge: AdminChallenge): AdminChallenge = transaction {
        AdminChallengesTable.insert {
            it[id] = challenge.id
            it[title] = challenge.title
            it[description] = challenge.description
            it[subjectId] = challenge.subjectId
            it[difficulty] = challenge.difficulty
            it[startDate] = challenge.startDate
            it[endDate] = challenge.endDate
            it[rewardCoins] = challenge.rewardCoins
            it[rewardXp] = challenge.rewardXp
            it[targetScore] = challenge.targetScore
            it[status] = challenge.status
            it[createdAt] = challenge.createdAt
        }
        challenge
    }

    override fun listAll(): List<AdminChallenge> = transaction {
        AdminChallengesTable.selectAll().map { it.toAdminChallenge() }
    }

    override fun findById(id: UUID): AdminChallenge? = transaction {
        AdminChallengesTable.selectAll().where { AdminChallengesTable.id eq id }.singleOrNull()?.toAdminChallenge()
    }

    private fun ResultRow.toAdminChallenge() = AdminChallenge(
        id = this[AdminChallengesTable.id],
        title = this[AdminChallengesTable.title],
        description = this[AdminChallengesTable.description],
        subjectId = this[AdminChallengesTable.subjectId],
        difficulty = this[AdminChallengesTable.difficulty],
        startDate = this[AdminChallengesTable.startDate],
        endDate = this[AdminChallengesTable.endDate],
        rewardCoins = this[AdminChallengesTable.rewardCoins],
        rewardXp = this[AdminChallengesTable.rewardXp],
        targetScore = this[AdminChallengesTable.targetScore],
        status = this[AdminChallengesTable.status],
        createdAt = this[AdminChallengesTable.createdAt]
    )
}
