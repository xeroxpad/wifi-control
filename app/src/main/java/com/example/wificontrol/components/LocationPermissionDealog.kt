package com.example.wificontrol.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wificontrol.R

@Composable
fun LocationPermissionDialog(
    modifier: Modifier = Modifier,
    onAllow: () -> Unit,
    onProhibit: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .size(36.dp),
                tint = Color.Blue,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                stringResource(id = R.string.do_allow_the_WiFi_control_application_to_access_the_geodata),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                color = Color.Blue,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 15.dp, vertical = 7.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(24.dp))
                            .background(Color.White)
                            .padding(horizontal = 10.dp)
                    ) {
                        Text(
                            stringResource(id = R.string.accurate_geodata_enabled),
                            color = Color.Blue,
                            fontSize = 12.sp,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TextButton(
                    onClick = { onAllow() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Text(
                        stringResource(id = R.string.allow_during_use),
                        color = Color.Blue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400
                    )
                }
                TextButton(
                    onClick = { onAllow() }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Text(
                        stringResource(id = R.string.allow_only_now),
                        color = Color.Blue,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400
                    )
                }
            }
            TextButton(
                onClick = { onProhibit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    stringResource(id = R.string.prohibit),
                    color = Color.Blue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400
                )
            }
        }
    }
}