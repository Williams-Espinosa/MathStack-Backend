package com.mathstack.admin.application

import com.mathstack.admin.domain.repository.AdminChallengeRepository
import java.util.UUID

class DeleteAdminChallengeUseCase(
    private val repository: AdminChallengeRepository
) {
    operator fun invoke(id: UUID): Boolean {
        if (repository.findById(id) == null) {
            return false
        }
        repository.delete(id)
        return true
    }
}
