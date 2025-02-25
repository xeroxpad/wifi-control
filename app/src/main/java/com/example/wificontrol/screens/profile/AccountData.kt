package com.example.wificontrol.screens.profile

import com.example.wificontrol.screens.support.ChatUserData


//@Serializable
data class AccountData(
    val userId: String = "",
    val email: String? = "",
    val username: String? = "",
    val ppurl: String? = "",
    val bio: String = "",
)

fun AccountData.toChatUserData() = ChatUserData(
    userId = this.userId,
    email = this.email?: "",
    username = this.username,
    ppurl = this.ppurl?: "",
    bio = this.bio,
    typing = false
)