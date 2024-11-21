package com.example.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RouterApiService {
    @POST("cgi-bin/luci/api/auth")
    suspend fun authenticate(
        @Body credentials: AuthRequest
    ): Response<AuthResponse>

    @POST("cgi-bin/luci/rpc/sys")
    suspend fun rebootRouter(
        @Body command: Map<String, String>,
        @Header("Authorization") authToken: String
    ): Response<Unit>
}

data class AuthResponse(
    val result: String
)

data class AuthRequest(
    val method: String = "login",
    val params: List<String>
)