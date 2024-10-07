package com.example.wificontrol.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wificontrol.R

@Composable
fun SpeedManager(
    modifier: Modifier = Modifier,
    speedMps: String,
    downloadOrUpload: Int,
    chart: Int
) {
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(16.dp))
            .height(98.dp)
            .background(Color.Gray.copy(0.2f))
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(top = 10.dp)
                .fillMaxWidth()
                .height(43.dp),
        ) {

            Icon(
                painter = painterResource(id = R.drawable.ic_speed),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .weight(0.3f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1.3f)
            ) {
                Text(text = "$speedMps Mps", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = stringResource(id = downloadOrUpload),
                    fontSize = 10.sp,
                    modifier = Modifier.offset(y = (-8).dp)
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(0.7f)
            ) {
                Text(text = "+23 %", fontSize = 12.sp)
                Icon(
                    painter = painterResource(id = R.drawable.ic_vector),
                    contentDescription = null
                )
            }
        }
        Icon(
            painter = painterResource(id = chart),
            contentDescription = null,
            modifier = Modifier
                .align(
                    Alignment.BottomCenter
                )
        )
    }
}