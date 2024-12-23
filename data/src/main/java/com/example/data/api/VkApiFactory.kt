package com.example.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object VkApiFactory {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.vk.com/method/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: VkApiService = retrofit.create()
}