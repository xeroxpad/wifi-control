package com.example.data.entities

import com.google.gson.annotations.SerializedName

data class VKProfileInfoResponseDto(
    @SerializedName("response") val response: VKProfileInfoDto
)
