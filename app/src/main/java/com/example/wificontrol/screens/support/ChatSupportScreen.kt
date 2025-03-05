package com.example.wificontrol.screens.support

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.compose.primaryLight
import com.example.wificontrol.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatSupportScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    chatId: String,
) {
    val chatSupportViewModel: ChatSupportViewModel = koinViewModel()
    val messageText = remember { mutableStateOf("") }
    val scrollState = rememberLazyListState()
    val message by chatSupportViewModel.messages.collectAsStateWithLifecycle()
    val reversedMessages = remember(message) { message }
    val chatItems = remember(reversedMessages) { reversedMessages.toChatItems() }
    var selectedMessage by remember { mutableStateOf(setOf<String>()) }
    val isSelectionMode = selectedMessage.isNotEmpty()
    var showDeleteDialog by remember { mutableStateOf(false) }
    val chatTitle by chatSupportViewModel.chatTitle.collectAsStateWithLifecycle()
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
            chatSupportViewModel.setChatScreenActive(false)
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
                    .padding(horizontal = 20.dp)
                    .background(MaterialTheme.colorScheme.background),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if (selectedMessage.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Отмена",
                        modifier = Modifier
                            .size(32.dp)
                            .padding(vertical = 5.dp)
                            .clickable {
                                Log.d("ChatSupport", "До сброса: $selectedMessage")
                                selectedMessage = emptySet()
                                Log.d("ChatSupport", "После сброса: $selectedMessage")
                            }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить",
                        modifier = Modifier
                            .size(32.dp)
                            .padding(vertical = 5.dp)
                            .clickable {
                                showDeleteDialog = true
                            }
                    )
                } else {
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
                        text = chatTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(padding)
            ) {
                if (showDeleteDialog && selectedMessage.isNotEmpty()) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        title = { Text("Удалить сообщение") },
                        text = { Text("Вы уверены, что хотите удалить это сообщение?") },
                        confirmButton = {
                            TextButton(onClick = {
                                chatSupportViewModel.deleteMessages(selectedMessage.toList())
                                selectedMessage != selectedMessage
                                showDeleteDialog = false
                            }) {
                                Text("Удалить")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showDeleteDialog = false
                            }) {
                                Text("Отмена")
                            }
                        }
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    reverseLayout = true,
                    state = scrollState
                ) {
                    if (chatItems.isEmpty()) {
                        items(5) { index ->
                            ShimmerLoadingPlaceholder(isOutgoing = index % 2 == 0)
                        }
                    } else {
                        items(chatItems) { message ->
                            when (message) {
                                is ChatItem.Message -> MessageBubble(
                                    message.message,
                                    isOutgoing = message.message.senderId == Firebase.auth.currentUser?.uid,
                                    selectedMessages = selectedMessage,
                                    isSelectionMode = isSelectionMode,
                                    onLongPress = {
                                        selectedMessage = selectedMessage + message.message.msgId
                                    },
                                    onClick = {
                                        if (isSelectionMode) {
                                            selectedMessage =
                                                if (selectedMessage.contains(message.message.msgId)) {
                                                    selectedMessage - message.message.msgId
                                                } else {
                                                    selectedMessage + message.message.msgId
                                                }
                                        }
                                    }
                                )

                                is ChatItem.DateSeparator -> DateSeparator(message.date)
                            }
                        }
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
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                            .background(Color.Transparent, RoundedCornerShape(20.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    IconButton(
                        onClick = {
                            chatSupportViewModel.sendMessage(messageText.value)
                            messageText.value = ""
                        },
                        enabled = messageText.value.isNotBlank()
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
            .padding(bottom = 4.dp),
        contentAlignment = if (isOutgoing) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .shimmer(shimmerInstance)
                .fillMaxWidth(0.6f)
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(12.dp)
                )
                .height(64.dp)
        )
    }
}

@Composable
fun DateSeparator(date: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date,
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageBubble(
    message: MessageData,
    isOutgoing: Boolean,
    selectedMessages: Set<String>,
    isSelectionMode: Boolean,
    onLongPress: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val chatSupportViewModel: ChatSupportViewModel = koinViewModel()
    val isSelected = selectedMessages.contains(message.msgId)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .combinedClickable(
                enabled = isOutgoing,
                onClick = { onClick() },
                onLongClick = { onLongPress() }
            )
            .background(
                color = if (isSelected) Color.Gray.copy(alpha = 0.2f)
                else Color.Transparent
            ),
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
                        lineHeight = 12.sp,
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

private fun List<MessageData>.toChatItems(): List<ChatItem> {
    val chatItems = mutableListOf<ChatItem>()
    var previousDate: String? = null

    for (message in this) {
        val messageDate = message.time?.toDate()?.toDateString()

        if (messageDate != previousDate) {
            chatItems.add(ChatItem.DateSeparator(messageDate!!))
            previousDate = messageDate
        }
        chatItems.add(ChatItem.Message(message))
    }
    return chatItems.reversed()
}

private fun Date.toDateString(): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return sdf.format(this)
}
