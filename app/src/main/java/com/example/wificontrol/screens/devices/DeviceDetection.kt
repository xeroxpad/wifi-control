package com.example.wificontrol.screens.devices

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.wificontrol.R
import com.example.wificontrol.components.LocationPermissionDialog
import com.example.wificontrol.navigation.Graph
import com.example.wificontrol.preferences.PermissionPreferences
import com.example.wificontrol.screens.scannerresult.ScannerResultScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun DeviceDetection(
    modifier: Modifier = Modifier,
    navController: NavController,
    scannerResultScreenViewModel: ScannerResultScreenViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val permissionPreferences by remember { mutableStateOf(PermissionPreferences(context)) }
    var showDialog by remember { mutableStateOf(!permissionPreferences.hasLocationPermission()) }
    var locationPermissionGranted by remember { mutableStateOf(permissionPreferences.hasLocationPermission()) }
    val scope = rememberCoroutineScope()
    var isSearching by remember { mutableStateOf(false) }
    val wiFiNetworks by scannerResultScreenViewModel.wiFiNetworks.collectAsStateWithLifecycle()
    val router by scannerResultScreenViewModel.router.collectAsStateWithLifecycle()
    val locationPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                locationPermissionGranted = true
                permissionPreferences.setLocationPermissionGranted(true)
                showDialog = false
                scope.launch {
//                    scannerResultScreenViewModel.scanWiFiNetworks()
                    scannerResultScreenViewModel.scanRouter()
                }
            }

            else -> {
                locationPermissionGranted = false
                permissionPreferences.setLocationPermissionGranted(false)
            }
        }
    }

    LaunchedEffect(wiFiNetworks, router) {
        if (wiFiNetworks.isNotEmpty() || router.isNotEmpty()) {
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
//            if (!showDialog) {
                SearchAnimation()
                Text(
                    text = stringResource(id = R.string.search_device),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
//            }
        }
        if (!locationPermissionGranted) {
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
                    permissionPreferences.setLocationPermissionGranted(false)
                    navController.popBackStack()
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 15.dp)
                    .padding(bottom = 15.dp)
            )
        } else {
            LaunchedEffect(Unit) {
//                scannerResultScreenViewModel.scanWiFiNetworks()
                scannerResultScreenViewModel.scanRouter()
            }
        }
    }
}