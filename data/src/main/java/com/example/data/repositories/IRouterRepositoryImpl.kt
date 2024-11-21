package com.example.data.repositories

import com.example.data.api.AuthRequest
import com.example.data.api.RouterApiService
import com.example.domain.repositories.IRouterRepository

class IRouterRepositoryImpl(
    private val apiService: RouterApiService,
    private val username: String,
    private val password: String
) : IRouterRepository {

    private var sessionToken: String? = null

    override suspend fun authenticate(): Result<Unit> {
        return try {
            val authRequest = AuthRequest(params = listOf(username, password))
            val response = apiService.authenticate(authRequest)
            if (response.isSuccessful && response.body()?.result != null) {
                sessionToken = response.body()!!.result
                Result.success(Unit)
            } else {
                Result.failure(Exception("Authentication failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun rebootRouter(): Result<Unit> {
        return sessionToken?.let { token ->
            try {
                val command = mapOf("method" to "reboot")
                val response = apiService.rebootRouter(command, "Bearer $token")
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to reboot router"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        } ?: Result.failure(Exception("No valid session"))
    }
}