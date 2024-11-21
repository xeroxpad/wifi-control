package com.example.data

import com.example.data.api.SpeedApi
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://fast.com/"

private fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor { chain ->
            val response = chain.proceed(chain.request())
            val contentType = response.body?.contentType()
            if (contentType?.subtype != "json") {
                throw IOException("Unexpected content type: ${contentType?.subtype}")
            }
            response
        }
        .build()
}

fun provideRetrofit(): Retrofit {
    val gson = GsonBuilder()
        .setLenient()
        .create()

    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(provideOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

fun provideSpeedApi(retrofit: Retrofit): SpeedApi {
    return retrofit.create(SpeedApi::class.java)
}