package com.example.wificontrol.screens.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.secondaryLight
import com.example.wificontrol.R
import com.example.wificontrol.components.CustomGraphStatistics
import com.example.wificontrol.components.CustomSpeedometer
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.placeholder

@Composable
fun StatisticsScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier =
        modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.wifi_statistics),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 3.dp)
                )
            }
        },
        content = { padding ->
            LazyColumn(
                modifier =
                Modifier
                    .padding(padding),
                verticalArrangement = Arrangement.Top
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Vir-Link",
                            color = Color.Gray,
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(16.dp))
                                .placeholder(
                                    visible = false,
                                    highlight = PlaceholderHighlight.fade(),
                                    color = secondaryLight.copy(alpha = 0.1f),
                                    shape = RectangleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check),
                            contentDescription = null,
                            modifier = Modifier.size(10.dp),
                            tint = Color.Gray,
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    CustomSpeedometer()
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_speed),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Column(
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(text = "43 Mps", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = stringResource(id = R.string.download),
                                fontSize = 14.sp,
                            )
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        Column(
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(text = "12 Mps", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = stringResource(id = R.string.upload),
                                fontSize = 14.sp,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_line),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.usage_statistics),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                        )
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "1 Неделя",
                                color = Color.Gray,
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(16.dp))
                                    .placeholder(
                                        visible = false,
                                        highlight = PlaceholderHighlight.fade(),
                                        color = secondaryLight.copy(alpha = 0.1f),
                                        shape = RectangleShape
                                    )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    CustomGraphStatistics(
                        barValue = listOf(0.55f, 0.6f, 1f, 0.32f, 0.86f, 0.73f, 0.14f),
                        xAxisScale = listOf("ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС"),
                    )
                }
            }
        }
    )
}