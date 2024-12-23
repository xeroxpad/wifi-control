package com.example.data.entities

import com.google.gson.annotations.SerializedName

data class VKProfileInfoDto(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name")val lastName: String,
    @SerializedName("photo_200") val avatarProfile: String,
)
