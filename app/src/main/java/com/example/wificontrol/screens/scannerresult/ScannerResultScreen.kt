package com.example.wificontrol.screens.scannerresult

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wificontrol.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScannerResultScreen(
    modifier: Modifier = Modifier,
    scannerResultScreenViewModel: ScannerResultScreenViewModel = koinViewModel()
) {
    var isRefreshing by remember { mutableStateOf(true) }
    val wiFiNetworks by scannerResultScreenViewModel.wiFiNetworks.collectAsStateWithLifecycle()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            scannerResultScreenViewModel.scanWiFiNetworks()
            isRefreshing = false
        }
    }
    SwipeRefresh(
        state = SwipeRefreshState(isRefreshing),
        onRefresh = { isRefreshing = true }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(id = R.string.found_devices),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
                    )
                }
            }
            items(wiFiNetworks) { network ->
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(16.dp))
                        .fillMaxWidth()
                        .background(Color.Gray.copy(0.2f))
                ) {
                    Text(
                        text = "SSID: ${network.ssid}\nЧастота: ${network.frequency}\nMAC: ${network.macId}",
                        fontWeight = FontWeight.W300,
                        fontSize = 18.sp,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                    )
                }
            }
        }
    }
}