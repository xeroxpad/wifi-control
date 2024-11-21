package com.example.domain.usecases

import com.example.domain.repositories.IRouterRepository

class RebootRouterUseCase(private val repository: IRouterRepository) {
    suspend operator fun invoke(): Result<Unit> {
        val authResult = repository.authenticate()
        return if (authResult.isSuccess) {
            repository.rebootRouter()
        } else {
            Result.failure(authResult.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}