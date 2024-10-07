package com.example.wificontrol.screens.home

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.compose.secondaryLight
import com.example.wificontrol.R
import com.example.wificontrol.components.DialogAlert
import com.example.wificontrol.components.SpeedManager
import com.example.wificontrol.components.Switch
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.placeholder
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel = koinViewModel()
) {
    val isLoading by homeScreenViewModel.isLoading.collectAsStateWithLifecycle()
    val devicesNotFound by homeScreenViewModel.devicesNotFound.collectAsStateWithLifecycle()
    val showDialog by homeScreenViewModel.showDialog.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        delay(3000)
        homeScreenViewModel.toggleIsLoading()
    }
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
                    contentDescription = null
                )
                Text(
                    text = stringResource(id = R.string.my_devices),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_notifications),
                    contentDescription = null
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
                            text = "${stringResource(id = R.string.total_devices)} 8",
                            color = Color.Gray,
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(16.dp))
                                .placeholder(
                                    visible = isLoading,
                                    highlight = PlaceholderHighlight.fade(),
                                    color = secondaryLight.copy(alpha = 0.1f),
                                    shape = RectangleShape
                                )
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (isLoading) {
                            Text(
                                text = stringResource(id = devicesNotFound),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_question),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(25.dp)
                                    .clickable { homeScreenViewModel.toggleDialog() }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(300.dp)
                                .clip(shape = RoundedCornerShape(16.dp))
                                .placeholder(
                                    visible = isLoading,
                                    highlight = PlaceholderHighlight.fade(),
                                    color = secondaryLight.copy(alpha = 0.1f),
                                    shape = RectangleShape
                                )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_router),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(16.dp))
                            .placeholder(
                                visible = isLoading,
                                highlight = PlaceholderHighlight.fade(),
                                color = secondaryLight.copy(alpha = 0.1f),
                                shape = RectangleShape
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.padding(start = 5.dp)) {
                            Text(
                                text = stringResource(id = R.string.name_device),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "${stringResource(id = R.string.password_device)} 2154f@4",
                                color = Color.Gray,
                            )
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
                            modifier = Modifier
                                .weight(0.5f)
                                .clip(shape = RoundedCornerShape(16.dp))
                                .placeholder(
                                    visible = isLoading,
                                    highlight = PlaceholderHighlight.fade(),
                                    color = secondaryLight.copy(alpha = 0.1f),
                                    shape = RectangleShape
                                )
                        )
                        Spacer(modifier = Modifier.weight(0.04f))
                        SpeedManager(
                            speedMps = "12",
                            downloadOrUpload = R.string.upload, chart = R.drawable.ic_upload,
                            modifier = Modifier
                                .weight(0.5f)
                                .clip(shape = RoundedCornerShape(16.dp))
                                .placeholder(
                                    visible = isLoading,
                                    highlight = PlaceholderHighlight.fade(),
                                    color = secondaryLight.copy(alpha = 0.1f),
                                    shape = RectangleShape
                                )
                        )
                    }
                }
            }
        }
    )
    if (showDialog) {
        DialogAlert()
    }
}