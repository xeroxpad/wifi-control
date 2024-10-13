package com.example.wificontrol.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun IconRouter(
    modifier: Modifier = Modifier,
    model: Int,
    contentDescription: String?,
    contentScale: ContentScale = ContentScale.Crop
) {
    Box(
        modifier =
        modifier
            .size(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Unspecified)
    ) {
        AsyncImage(
            modifier = Modifier,
            model = model,
            contentDescription = contentDescription,
            contentScale = contentScale,
        )
    }
}