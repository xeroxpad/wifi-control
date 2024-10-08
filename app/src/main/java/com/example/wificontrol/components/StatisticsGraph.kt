package com.example.wificontrol.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomGraphStatistics(
    barValue: List<Float>,
    xAxisScale: List<String>,
    totalAmount: Int = 100,
) {
    val context = LocalContext.current
    val barGraphHeight by remember { mutableStateOf(200.dp) }
    val barGraphWidth by remember { mutableStateOf(20.dp) }
    val scaleYAxisWidth by remember { mutableStateOf(50.dp) }
    val scaleLineWidth by remember { mutableStateOf(2.dp) }
    val usageStatistics = listOf("100 Gb", "80 Gb", "60 Gb", "40 Gb", "20 Gb", "1 Gb")
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(barGraphHeight),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(scaleYAxisWidth),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    usageStatistics.forEach { gb ->
                        Text(text = gb, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(scaleLineWidth)
                    .background(Color.Transparent)
            )
            barValue.forEach { value ->
                val realValue = (value * totalAmount).toInt()
                Box(
                    modifier = Modifier
                        .padding(start = barGraphWidth, bottom = 5.dp)
                        .clip(CircleShape)
                        .width(barGraphWidth)
                        .fillMaxHeight(value)
                        .background(Color.Gray)
                        .clickable {
                            Toast
                                .makeText(context, realValue.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                )
            }

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(scaleLineWidth)
                .background(Color.Transparent)
        )
        Row(
            modifier = Modifier
                .padding(start = scaleYAxisWidth + barGraphWidth + scaleLineWidth)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(barGraphWidth)
        ) {

            xAxisScale.forEach {
                Text(
                    modifier = Modifier.width(barGraphWidth),
                    text = it,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 8.sp
                )
            }
        }
    }
}

