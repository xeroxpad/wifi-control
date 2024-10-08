package com.example.wificontrol.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wificontrol.R
import com.example.wificontrol.components.TypeDevice
import com.example.wificontrol.navigation.Graph

@Composable
fun SearchScreen(modifier: Modifier = Modifier, navController: NavController) {
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
                    text = stringResource(id = R.string.type_device),
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
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.select_device),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    TypeDevice(
                        painterTypeDevice = R.drawable.ic_router,
                        titleTypeDevice = R.string.wifi_router,
                        onClick = { navController.navigate(Graph.DeviceDetection.route) }
                    )
                    TypeDevice(
                        painterTypeDevice = R.drawable.ic_modem,
                        titleTypeDevice = R.string.modem,
                        onClick = { navController.navigate(Graph.DeviceDetection.route) }
                    )
                    TypeDevice(
                        painterTypeDevice = R.drawable.ic_switch,
                        titleTypeDevice = R.string.switch_device,
                        onClick = { navController.navigate(Graph.DeviceDetection.route) }
                    )
                    TypeDevice(
                        painterTypeDevice = R.drawable.ic_access_pointer,
                        titleTypeDevice = R.string.access_point,
                        onClick = { navController.navigate(Graph.DeviceDetection.route) }
                    )
                    TypeDevice(
                        painterTypeDevice = R.drawable.ic_repeater,
                        titleTypeDevice = R.string.signal_amplifier,
                        onClick = { navController.navigate(Graph.DeviceDetection.route) }
                    )
                    TypeDevice(
                        painterTypeDevice = R.drawable.ic_network_storage,
                        titleTypeDevice = R.string.network_storage,
                        onClick = { navController.navigate(Graph.DeviceDetection.route) }
                    )
                }
            }
        }
    )
}