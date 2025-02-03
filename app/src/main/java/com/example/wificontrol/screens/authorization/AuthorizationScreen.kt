@file:OptIn(ExperimentalFoundationApi::class)

package com.example.wificontrol.screens.authorization

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.compose.primaryLight
import com.example.wificontrol.R
import com.example.wificontrol.components.TextFieldAuth
import com.example.wificontrol.navigation.Graph
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthorizationScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authorizationViewModel: AuthorizationViewModel = koinViewModel(),
    onLoginVk: () -> Unit,
) {
    val mail by authorizationViewModel.mail.collectAsStateWithLifecycle()
    val password by authorizationViewModel.password.collectAsStateWithLifecycle()
    var screenState by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    val authFirebase = Firebase.auth
    val context = LocalContext.current
    LaunchedEffect(mail, password) {
        authorizationViewModel.mailChange(mail)
        authorizationViewModel.passwordChange(password)
    }
    Scaffold(
        modifier =
        modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        topBar = {},
        content = { padding ->
            LazyColumn(
                modifier =
                Modifier
                    .padding(padding)
                    .padding(horizontal = 20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    TextFieldAuth(
                        placeholder = R.string.placeholder_email,
                        text = mail,
                        textChange = { authorizationViewModel.mailChange(it) })
                    Spacer(modifier = Modifier.height(20.dp))
                    TextFieldAuth(
                        placeholder = R.string.placeholder_password,
                        text = password,
                        textChange = { authorizationViewModel.passwordChange(it) },
                        isTextFieldForPassword = true
                    )
                    Spacer(modifier = Modifier.height(20.dp))
//                    Box(modifier = Modifier
//                        .clip(shape = RoundedCornerShape(14.dp))
//                        .background(primaryLight)
//                        .clickable {
//                            signInAccountFirebase(
//                                authFirebase,
//                                mail,
//                                password,
//                                context,
//                                onSignInFailure = {},
//                                onSignInSuccess = {
//                                    navController.navigate(Graph.Home.route)
//                                }
//                            )
//                        }
//                        .padding(all = 10.dp)) {
//                        Text(
//                            text = stringResource(id = R.string.sign_in_system),
//                            fontWeight = FontWeight.Bold,
//                            color = Color.White
//                        )
//                    }
//                    Spacer(modifier = Modifier.height(20.dp))
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//                        Text(text = "зарегистрироваться ", modifier = Modifier.clickable {
//                            !screenState
//                            signUpAccountFirebase(
//                                authFirebase,
//                                mail,
//                                password,
//                                onSignUpFailure = {},
//                                onSignUpSuccess = {
//                                    navController.navigate(Graph.Home.route)
//                                }
//                            )
//                        }, color = primaryLight)
//                        Text(text = "или войти с помощью")
//                    }
//                    Spacer(modifier = Modifier.height(15.dp))
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Center,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .clip(shape = RoundedCornerShape(16.dp))
//                                .clickable { }
//                                .size(48.dp),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_logo_yandex),
//                                contentDescription = null,
//                                tint = Color.Unspecified,
//                            )
//                        }
//                        Spacer(modifier = Modifier.width(15.dp))
//                        Box(
//                            modifier = Modifier
//                                .clip(shape = RoundedCornerShape(16.dp))
//                                .clickable {
//                                    onLoginVk()
//                                }
//                                .size(48.dp),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_logo_vk),
//                                contentDescription = null,
//                                tint = Color.Unspecified,
//                            )
//                        }
//                    }

                    if (screenState) {
                        SignUpStateScreen(
                            authFirebase,
                            context,
                            mail = mail,
                            password = password,
                            onSignUpSuccess = { /* ... */ },
                            onSwitchToSignUp = { screenState = false },
                        )
                    } else {
                        SignInStateScreen(
                            authFirebase,
                            context,
                            mail = mail,
                            password = password,
                            onSignInSuccess = { navController.navigate(Graph.Home.route) },
                            onLoginVk = onLoginVk,
                            onSwitchToSignIn = { screenState = true }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun SignInStateScreen(
    firebaseAuth: FirebaseAuth,
    context: Context,
    mail: String,
    password: String,
    onSignInSuccess: () -> Unit,
    onLoginVk: () -> Unit,
    onSwitchToSignIn: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier
            .clip(shape = RoundedCornerShape(14.dp))
            .background(primaryLight)
            .clickable {
                signInAccountFirebase(
                    firebaseAuth,
                    email = mail,
                    password = password,
                    context,
                    onSignInFailure = {},
                    onSignInSuccess = {
                        onSignInSuccess()
                    }
                )
            }
            .padding(all = 10.dp)) {
            Text(
                text = stringResource(id = R.string.sign_in_system),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

        }
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "зарегистрироваться ", modifier = Modifier
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onSwitchToSignIn() }
                ), color = primaryLight,
                fontSize = 14.sp)
            Text(text = stringResource(id = R.string.or_log_in_using), fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(15.dp))
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

@Composable
fun SignUpStateScreen(
    firebaseAuth: FirebaseAuth,
    context: Context,
    mail: String,
    password: String,
    onSignUpSuccess: () -> Unit,
    onSwitchToSignUp: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight()
    ) {
        Box(modifier = Modifier
            .clip(shape = RoundedCornerShape(14.dp))
            .background(primaryLight)
            .clickable {
                signUpAccountFirebase(
                    firebaseAuth,
                    mail,
                    password,
                    onSignUpFailure = {},
                    onSignUpSuccess = {
                        onSignUpSuccess()
                    }
                )
            }
            .padding(all = 10.dp)) {
            Text(
                text = stringResource(id = R.string.sign_up_system),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = stringResource(id = R.string.i_already_have_an_account),
            modifier = Modifier
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onSwitchToSignUp() }
                )
        )
    }
}


