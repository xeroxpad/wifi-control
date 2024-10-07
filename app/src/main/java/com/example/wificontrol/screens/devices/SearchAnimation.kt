package com.example.wificontrol.screens.devices

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wificontrol.R

@Composable
fun SearchAnimation(modifier: Modifier = Modifier) {
    val searchAnimation by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.search_animation))
    val progress by animateLottieCompositionAsState(
        composition = searchAnimation,
        iterations = LottieConstants.IterateForever,
        speed = 2f
    )
    LottieAnimation(composition = searchAnimation, progress = { progress }, modifier = modifier)
}