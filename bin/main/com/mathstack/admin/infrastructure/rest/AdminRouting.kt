package com.mathstack.admin.infrastructure.rest

import com.mathstack.admin.application.GenerateAvatarUseCase
import com.mathstack.admin.application.ListAllUsersUseCase
import com.mathstack.admin.application.UpdateUserCoinsUseCase
import com.mathstack.admin.infrastructure.rest.dto.DashboardStatsResponse
import com.mathstack.admin.infrastructure.rest.dto.GenerateAvatarRequest
import com.mathstack.admin.infrastructure.rest.dto.GenerateAvatarResponse
import com.mathstack.admin.infrastructure.rest.dto.OverviewStatsResponse
import com.mathstack.admin.infrastructure.rest.dto.UpdateCoinsRequest
import com.mathstack.shared.infrastructure.plugins.authorize
import com.mathstack.users.infrastructure.rest.dto.toResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import java.util.UUID
import com.mathstack.admin.application.ListAllPracticeSessionsUseCase
import com.mathstack.practice.infrastructure.rest.dto.toResponse
import org.koin.ktor.ext.inject

fun Route.adminRouting() {
    val listAllUsersUseCase by inject<ListAllUsersUseCase>()
    val generateAvatarUseCase by inject<GenerateAvatarUseCase>()
    val updateUserCoinsUseCase by inject<UpdateUserCoinsUseCase>()
    val getUserProfileUseCase by inject<com.mathstack.admin.application.GetUserProfileUseCase>()
    val listAllDiagnosticsUseCase by inject<com.mathstack.admin.application.ListAllDiagnosticsUseCase>()
    val listAllPracticeSessionsUseCase by inject<com.mathstack.admin.application.ListAllPracticeSessionsUseCase>()
    val getDashboardStatsUseCase by inject<com.mathstack.admin.application.GetDashboardStatsUseCase>()
    val listAllLessonsUseCase by inject<com.mathstack.admin.application.ListAllLessonsUseCase>()
    val listAllExercisesUseCase by inject<com.mathstack.admin.application.ListAllExercisesUseCase>()
    val listAllChallengesUseCase by inject<com.mathstack.admin.application.ListAllChallengesUseCase>()
    val createAdminChallengeUseCase by inject<com.mathstack.admin.application.CreateAdminChallengeUseCase>()

    authenticate("auth-jwt") {
        authorize("ADMIN") {
            route("/api/v1/admin") {
                get("/dashboard/stats") {
                    call.respond(getDashboardStatsUseCase())
                }
                
                get("/lessons") {
                    call.respond(listAllLessonsUseCase())
                }

                get("/exercises") {
                    call.respond(listAllExercisesUseCase())
                }

                get("/challenges") {
                    call.respond(listAllChallengesUseCase())
                }
                
                post("/challenges") {
                    val request = call.receive<com.mathstack.admin.infrastructure.rest.dto.CreateAdminChallengeRequest>()
                    
                    val command = com.mathstack.admin.application.CreateAdminChallengeCommand(
                        title = request.title,
                        description = request.description,
                        subjectId = request.subjectId,
                        difficulty = request.difficulty,
                        startDate = request.startDate?.let { java.time.LocalDateTime.parse(it) },
                        endDate = request.endDate?.let { java.time.LocalDateTime.parse(it) },
                        rewardCoins = request.rewardCoins,
                        rewardXp = request.rewardXp,
                        targetScore = request.targetScore
                    )
                    
                    val challenge = createAdminChallengeUseCase(command)
                    
                    call.respond(HttpStatusCode.Created, com.mathstack.admin.infrastructure.rest.dto.ChallengeResponse(
                        id = challenge.id.toString(),
                        creatorId = "admin",
                        status = challenge.status,
                        createdAt = challenge.createdAt.toString(),
                        title = challenge.title,
                        description = challenge.description,
                        subjectId = challenge.subjectId,
                        difficulty = challenge.difficulty,
                        startDate = challenge.startDate?.toString(),
                        endDate = challenge.endDate?.toString(),
                        rewardCoins = challenge.rewardCoins,
                        rewardXP = challenge.rewardXp,
                        targetScore = challenge.targetScore
                    ))
                }

                get("/users") {
                    val users = listAllUsersUseCase()
                    call.respond(users.map { it.toResponse() })
                }

                get("/users/{id}") {
                    val userId = UUID.fromString(call.parameters["id"] ?: throw IllegalArgumentException("Missing id"))
                    val profile = getUserProfileUseCase(userId)
                    call.respond(profile.toResponse())
                }

                get("/diagnostics") {
                    val diagnostics = listAllDiagnosticsUseCase()
                    call.respond(diagnostics.map { it.toResponse() })
                }

                get("/practice-sessions") {
                    val sessions = listAllPracticeSessionsUseCase()
                    call.respond(sessions.map { it.toResponse() })
                }

                put("/users/{id}/coins") {
                    val userId = UUID.fromString(call.parameters["id"] ?: throw IllegalArgumentException("Missing id"))
                    val request = call.receive<UpdateCoinsRequest>()
                    
                    updateUserCoinsUseCase(userId, request.coins)
                    call.respond(HttpStatusCode.OK)
                }

                get("/stats/overview") {
                    call.respond(OverviewStatsResponse(cpuUsagePercent = 24.5, memoryUsageMb = 512L))
                }

                post("/avatars/generate") {
                    val request = call.receive<GenerateAvatarRequest>()
                    val url = generateAvatarUseCase(request.seed, request.style)
                    call.respond(GenerateAvatarResponse(url))
                }
            }
        }
    }
}
