package com.example.wificontrol.screens.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.compose.primaryLight
import com.example.wificontrol.R
import com.example.wificontrol.components.Switch
import com.example.wificontrol.navigation.Graph
import com.example.wificontrol.screens.authorization.signOut
import com.example.wificontrol.screens.support.ChatSupportViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    chatSupportViewModel: ChatSupportViewModel = koinViewModel(),
    viewModelProfile: ProfileScreenViewModel = koinViewModel(),
) {
    val chatId by chatSupportViewModel.chatId.collectAsStateWithLifecycle()
    val profile by viewModelProfile.profile.observeAsState()
    val email = viewModelProfile.userEmail
    val tooltipState = rememberTooltipState()
    val scope = rememberCoroutineScope()
    val isDarkTheme = isSystemInDarkTheme()
    val avatarTintColor = if (isDarkTheme) Color.LightGray else Color.DarkGray
    val authFirebase = Firebase.auth
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(18.dp))
                .fillMaxWidth()
                .background(color = Color.Gray.copy(alpha = 0.2f))
                .padding(5.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(shape = RoundedCornerShape(14.dp))
                        .background(Color.Gray.copy(0.2f))
                ) {
                    AsyncImage(
                        model = profile?.photo,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(shape = RoundedCornerShape(14.dp)),
                        placeholder = painterResource(id = R.drawable.ic_avatar_default),
                        error = painterResource(id = R.drawable.ic_avatar_default),
                        contentScale = ContentScale.FillBounds,
                        colorFilter = ColorFilter.tint(avatarTintColor),
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip {
                                Text(
                                    text = email ?: "Аккаунт не найден",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        },
                        state = tooltipState
                    ) {
                        Text(
                            text = email ?: "Аккаунт не найден",
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.Start)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onLongPress = {
                                            scope.launch { tooltipState.show() }
                                        }
                                    )
                                },
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier
                    .clip(shape = RoundedCornerShape(14.dp))
                    .background(primaryLight)
                    .clickable {
                        signOut(authFirebase)
                        navController.navigate(Graph.Auth.route)
                    }
                    .padding(all = 10.dp)) {
                    Text(
                        text = stringResource(id = R.string.log_out),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        modifier = Modifier.width(IntrinsicSize.Min)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .clickable {  },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.dark_theme),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch()
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .clickable {
                    navController.navigate("${Graph.ChatSupport.route}/$chatId")
                    Log.d("ProfileScreen", "Текущий chatId: $chatId")
                },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.chat_support),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_support),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}