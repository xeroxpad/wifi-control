package com.example.wificontrol.screens.authorization

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class AuthTokenStorage(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_auth_tokens",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(idToken: String) {
        sharedPreferences.edit()
            .putString("id_token", idToken)
            .apply()
    }

    fun getToken(): String? = sharedPreferences.getString("id_token", null)

    fun clearTokens() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }
}