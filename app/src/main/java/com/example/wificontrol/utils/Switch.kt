package com.example.wificontrol.utils

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.compose.secondaryLight

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun Switch() {
    var isToggled by remember { mutableStateOf(false) }
    val togglePosition by animateDpAsState(targetValue = if (isToggled) 24.dp else 2.dp, label = "")
    Box(
        modifier = Modifier
            .size(50.dp, 30.dp)
            .background(
                color = if (isToggled) secondaryLight else Color.Gray,
                shape = RoundedCornerShape(60)
            )
            .pointerInput(Unit) {
                detectTapGestures(onTap = { isToggled = !isToggled })
            }
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .offset(
                    x = togglePosition,
                    y = (30.dp - 24.dp) / 2
                )
                .shadow(elevation = 4.dp, shape = CircleShape)
                .background(color = Color.White, shape = CircleShape)
        )
    }
}