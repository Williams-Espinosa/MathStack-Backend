package com.mathstack.admin.infrastructure.persistence

import com.mathstack.academic.infrastructure.persistence.SubjectTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object AdminChallengesTable : Table("admin_challenges") {
    val id = uuid("id")
    val title = varchar("title", 255)
    val description = text("description")
    val subjectId = integer("subject_id").references(SubjectTable.id).nullable()
    val difficulty = varchar("difficulty", 50)
    val startDate = datetime("start_date").nullable()
    val endDate = datetime("end_date").nullable()
    val rewardCoins = integer("reward_coins").default(0)
    val rewardXp = integer("reward_xp").default(0)
    val targetScore = integer("target_score").default(0)
    val status = varchar("status", 50)
    val createdAt = datetime("created_at")

    override val primaryKey = PrimaryKey(id)
}
