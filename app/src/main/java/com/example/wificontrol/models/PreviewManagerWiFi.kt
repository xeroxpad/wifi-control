package com.example.wificontrol.models

import androidx.compose.runtime.Immutable
import com.example.wificontrol.R

@Immutable
data class CategoryCollection(
    val id: Long,
    val name: String,
    val categories: List<PreviewManagerWiFi>
)

@Immutable
data class PreviewManagerWiFi(
    val speedMps: String,
    val downloadOrUpload: Int,
    val chart: Int
)