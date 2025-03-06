package com.example.wificontrol.components

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CustomSpeedometer(modifier: Modifier = Modifier) {
    var showWebView by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (showWebView) {
            SpeedTestWebView(onClose = { showWebView = false })
        } else {
            Button(onClick = { showWebView = true }) {
                Text("Запустить тест скорости в браузере")
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SpeedTestWebView(onClose: () -> Unit) {
    TextButton(onClick = onClose) {
        Text("Закрыть")
    }
    Spacer(modifier = Modifier.height(10.dp))
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl("https://www.speedtest.net")
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}