package com.example.wificontrol.screens.devices

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.wificontrol.R
import com.example.wificontrol.navigation.Graph
import com.example.wificontrol.screens.scannerresult.ScannerResultScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import kotlin.coroutines.coroutineContext

@Composable
fun DeviceDetection(
    modifier: Modifier = Modifier,
    navController: NavController,
    scannerResultScreenViewModel: ScannerResultScreenViewModel = koinViewModel()
) {
    var showDialog by remember { mutableStateOf(true) }
    var locationPermissionGranted by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var isSearching by remember { mutableStateOf(false) }
    val wiFiNetworks by scannerResultScreenViewModel.wiFiNetworks.collectAsStateWithLifecycle()
    val locationPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                locationPermissionGranted = true
                showDialog = false
                scope.launch {
                    scannerResultScreenViewModel.scanWiFiNetworks()
                }
            }
            else -> {
                locationPermissionGranted = false
            }
        }
    }

    LaunchedEffect(wiFiNetworks) {
        if (wiFiNetworks.isNotEmpty()) {
            isSearching = false
            navController.navigate(Graph.ScannerResult.route)
        }
    }

    Box(
        modifier =
        modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (!showDialog) {
                SearchAnimation()
                Text(
                    text = stringResource(id = R.string.search_device),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
            }
        }
        if (showDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Unspecified)
            )
            LocationPermissionDialog(
                onAllow = {
                    locationPermissionRequest.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                },
                onProhibit = {
                    showDialog = false
                    navController.popBackStack()
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 15.dp)
                    .padding(bottom = 15.dp)
            )
        }
    }
}

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
                "Разрешить приложению WiFi Control доступ к геоданным?",
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
                            "ТОЧНЫЕ ГЕОДАННЫЕ: ВКЛЮЧЕНО",
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
                        "РАЗРЕШИТЬ ВО ВРЕМЯ ИСПОЛЬЗОВАНИЯ",
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
                        "РАЗРЕШИТЬ ТОЛЬКО СЕЙЧАС",
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
                    "ЗАПРЕТИТЬ",
                    color = Color.Blue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400
                )
            }
        }
    }
}