package com.example.wificontrol.screens.scannerresult

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.wificontrol.R
import com.example.wificontrol.navigation.Graph
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScannerResultScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    scannerResultScreenViewModel: ScannerResultScreenViewModel = koinViewModel()
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val wiFiNetworks by scannerResultScreenViewModel.wiFiNetworks.collectAsStateWithLifecycle()
    LaunchedEffect(isRefreshing) {
        scannerResultScreenViewModel.scanWiFiNetworks()
        if (isRefreshing) {
            delay(2000)
            isRefreshing = false
        }
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(indication = null, interactionSource = remember {
                            MutableInteractionSource()
                        }) { navController.navigate(Graph.Search.route) },
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = stringResource(id = R.string.found_devices),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
            }
        },
        content = { padding ->
            SwipeRefresh(
                state = SwipeRefreshState(isRefreshing),
                onRefresh = { isRefreshing = true }
            ) {
                LazyColumn(
                    modifier = modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    items(wiFiNetworks) { network ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(16.dp))
                                .fillMaxWidth()
                                .background(Color.Gray.copy(0.2f))
                        ) {
                            Text(
                                text = "SSID: ${network.ssid}\nЧастота: ${network.frequency}\nMAC: ${network.macId.toUpperCase()}\nИнформация о DHCP:\n${network.ip}",
                                fontWeight = FontWeight.W300,
                                fontSize = 18.sp,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
                                lineHeight = 30.sp
                            )
                        }
                    }
                }
            }
        }
    )
}