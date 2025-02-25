@file:OptIn(ExperimentalLayoutApi::class)

package com.example.wificontrol.screens.support

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.primaryLight
import com.example.wificontrol.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatSupportScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    chatId: String,
) {
    val chatSupportViewModel: ChatSupportViewModel = koinViewModel()
    val messageText = remember { mutableStateOf("") }
    val scrollState = rememberLazyListState()
    val message by chatSupportViewModel.messages.collectAsState()
    val reversedMessages = remember(message) { message.reversed() }
    Log.d("ChatUI", "Messages in UI: ${message.size}")

    LaunchedEffect(chatId) {
        Log.d("ChatSupport", "Полученный chatId в UI: '$chatId'")
        if (chatId.isNotEmpty()) {
            chatSupportViewModel.setChatId(chatId)
        } else {
            Log.e("ChatSupport", "Пустой chatId не устанавливаем")
        }
    }
    LaunchedEffect(message.size) {
        if (reversedMessages.isNotEmpty()) {
            scrollState.scrollToItem(0)
        }
    }
    LaunchedEffect(Unit) {
        chatSupportViewModel.setChatScreenActive()
        chatSupportViewModel.markMessagesAsRead()
    }
    DisposableEffect(Unit) {
        onDispose {
            chatSupportViewModel.setChatScreenActive(true)
        }
    }

    Scaffold(
        modifier =
        modifier
            .fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(indication = null, interactionSource = remember {
                            MutableInteractionSource()
                        }) { navController.popBackStack() },
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = stringResource(id = R.string.chat_support),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(padding)
                    .padding(padding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    reverseLayout = true,
                    state = scrollState
                ) {
                    items(reversedMessages) { message ->
                        MessageBubble(
                            message,
                            isOutgoing = message.senderId == Firebase.auth.currentUser?.uid
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .imePadding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = messageText.value,
                        onValueChange = { messageText.value = it },
                        placeholder = { Text("Введите сообщение...") },
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            chatSupportViewModel.sendMessage(messageText.value)
                            messageText.value = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Отправить"
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ShimmerLoadingPlaceholder(isOutgoing: Boolean) {
    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = if (isOutgoing) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .shimmer(shimmerInstance)
                .fillMaxWidth(0.6f)
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(16.dp)
                )
                .height(74.dp)
        )
    }
}

@Composable
fun MessageBubble(message: MessageData, isOutgoing: Boolean) {
    val chatSupportViewModel: ChatSupportViewModel = koinViewModel()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        horizontalArrangement = if (isOutgoing) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(12.dp))
                .background(if (isOutgoing) Color.LightGray else primaryLight)
        ) {
            Card(
                modifier = Modifier.widthIn(max = 250.dp),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = message.content,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    )
                    Row(
                        modifier = Modifier.align(Alignment.End),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = chatSupportViewModel.formatTimestamp(message.time!!),
                            fontSize = 12.sp,
                            lineHeight = 10.sp,
                            color = Color.Gray,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        if (isOutgoing) {
                            when (message.status) {
                                MessageStatus.DELIVERED -> {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_message_check),
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                        tint = Color.Gray
                                    )
                                }

                                MessageStatus.READ -> {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_message_check_all),
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                        tint = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
