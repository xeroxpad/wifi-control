package com.example.wificontrol.screens.authorization

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.compose.primaryLight
import com.example.compose.secondaryLight
import com.example.wificontrol.R
import com.example.wificontrol.components.EnterPhoneNumber
import com.example.wificontrol.components.PrefixNumberPhone
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthorizationScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
//    authorizationViewModel: AuthorizationViewModel = koinViewModel(),
    onLoginVk: () -> Unit,
) {
    var currentPhoneNumber by remember { mutableStateOf("") }
    val textColor =
        if (currentPhoneNumber.isEmpty()) primaryLight.copy(alpha = 0.5f) else Color.Black
    Scaffold(
        modifier =
        modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { navController.popBackStack() },
                )
            }
        },
        content = { padding ->
            LazyColumn(
                modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = stringResource(id = R.string.specify_phone_number),
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(id = R.string.description_for_authorization),
                        fontWeight = FontWeight.W400,
                        fontSize = 18.sp,
                        color = secondaryLight
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier =
                            Modifier
                                .border(
                                    width = 1.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.Black
                                )
                                .clip(shape = RoundedCornerShape(16.dp))
                                .width(86.dp)
                                .background(Color.Gray.copy(alpha = 0.2f))
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center,
                        ) {
                            PrefixNumberPhone(
                                textColor = textColor,
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        EnterPhoneNumber(placeholder = R.string.placeholder_number) { newPhoneNumber ->
                            currentPhoneNumber = newPhoneNumber
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Войти с помощью")
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(16.dp))
                                .clickable { }
                                .size(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_logo_yandex),
                                contentDescription = null,
                                tint = Color.Unspecified,
                            )
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        Box(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(16.dp))
                                .clickable {
                                    onLoginVk()
                                }
                                .size(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_logo_vk),
                                contentDescription = null,
                                tint = Color.Unspecified,
                            )
                        }
                    }
                }
            }
        }
    )
}