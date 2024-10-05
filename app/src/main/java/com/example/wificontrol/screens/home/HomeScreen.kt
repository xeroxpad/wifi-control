package com.example.wificontrol.screens.home

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wificontrol.R
import com.example.wificontrol.components.SpeedManager
import com.example.wificontrol.components.Switch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel = koinViewModel()
) {
    Scaffold(
        modifier =
        modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = "меню"
                )
                Text(text = "My Routers", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Icon(
                    painter = painterResource(id = R.drawable.ic_notifications),
                    contentDescription = "уведомления"
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
                        Text(text = "Total: 8", color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_router),
                            contentDescription = "router",
                            modifier = Modifier
                                .size(350.dp),
                            tint = Color.Unspecified
                        )
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Vir-Link", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Text(text = "Password: 2154f@4", color = Color.Gray)
                        }
                        Switch()
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        SpeedManager(
                            speedMps = "42",
                            downloadOrUpload = R.string.download, chart = R.drawable.ic_download,
                            modifier = Modifier.weight(0.5f)
                        )
                        Spacer(modifier = Modifier.weight(0.04f))
                        SpeedManager(
                            speedMps = "12",
                            downloadOrUpload = R.string.upload, chart = R.drawable.ic_upload,
                            modifier = Modifier.weight(0.5f)
                        )
                    }
                }
            }
        }
    )
}