package com.example.wificontrol.preferences

import android.content.Context

class PermissionPreferences(context: Context) {
    private val preferences = context.getSharedPreferences("permission_prefs", Context.MODE_PRIVATE)

    fun hasLocationPermission(): Boolean {
        return preferences.getBoolean("location_permission_granted", false)
    }

    fun setLocationPermissionGranted(granted: Boolean) {
        preferences.edit().putBoolean("location_permission_granted", granted).apply()
    }
}