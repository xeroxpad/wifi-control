package com.example.wificontrol.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.farimarwat.speedmeter.SpeedMeter
import kotlin.random.Random

@Composable
fun CustomSpeedometer(modifier: Modifier = Modifier) {
    var progress by remember {
        mutableFloatStateOf(0f)
    }
    SpeedMeter(
        modifier = modifier
            .fillMaxWidth()
            .height(370.dp)
            .clickable(onClick = {
                progress = Random
                    .nextInt(100)
                    .toFloat()
            },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }),
        backgroundColor = Color.Unspecified,
        progressWidth = 50f,
        progress = progress,
        needleColors = listOf(Color.Black, Color.White),
        needleKnobColors = listOf(Color.Black, Color.Gray),
        needleKnobSize = 20f,
        progressColors = listOf(Color.Red, Color.Yellow),
        labelColor = Color.White,
        unitText = "MB",
    )
}