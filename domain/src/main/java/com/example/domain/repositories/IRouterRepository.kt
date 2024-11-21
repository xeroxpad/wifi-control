package com.example.domain.repositories

interface IRouterRepository {
    suspend fun authenticate(): Result<Unit>
    suspend fun rebootRouter(): Result<Unit>
}