package com.example.wificontrol.screens.authorization

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.compose.primaryLight
import com.example.wificontrol.R
import com.example.wificontrol.components.TextFieldAuth
import com.example.wificontrol.navigation.Graph
import com.example.wificontrol.screens.profile.AccountData
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
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
                    Box(modifier = Modifier
                        .clip(shape = RoundedCornerShape(14.dp))
                        .background(primaryLight)
                        .clickable {
                            signInAccountFirebase(
                                authFirebase,
                                mail,
                                password,
                                context,
                                onSignInFailure = {},
                                onSignInSuccess = {
                                    navController.navigate(Graph.Home.route)
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
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "зарегистрироваться ", modifier = Modifier.clickable {
                            signUpAccountFirebase(
                                authFirebase,
                                mail,
                                password,
                                onSignUpFailure = {},
                                onSignUpSuccess = {
                                    navController.navigate(Graph.Home.route)
                                }
                            )
                        }, color = primaryLight)
                        Text(text = "или войти с помощью")
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
        }
    )
}

fun signUpAccountFirebase(
    authFirebase: FirebaseAuth,
    email: String,
    password: String,
    onSignUpSuccess: (AccountData) -> Unit,
    onSignUpFailure: (String) -> Unit
) {
    if (email.isBlank() || password.isBlank()) {
        onSignUpFailure("Email и пароль не может быть пустым")
        return
    }
    authFirebase.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSignUpSuccess(
                    AccountData(
                        task.result.user?.uid!!,
                        task.result.user?.email!!
                    )
                )
            } else {
                Log.d("Firebase", "Регистрация завершилась с ошибкой")
            }
        }
}

fun signInAccountFirebase(
    authFirebase: FirebaseAuth,
    email: String,
    password: String,
    context: Context,
    onSignInSuccess: (AccountData) -> Unit,
    onSignInFailure: (String) -> Unit,
) {
    if (email.isBlank() || password.isBlank()) {
        onSignInFailure("Email и пароль не может быть пустым")
        return
    }
    authFirebase.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSignInSuccess(
                    AccountData(
                        task.result.user?.uid!!,
                        task.result.user?.email!!
                    )
                )
            } else {
                Toast.makeText(context, "Такого аккаунта не существует", Toast.LENGTH_SHORT).show()
            }
        }
}

private fun accountDelete(authFirebase: FirebaseAuth, email: String, password: String) {
    val credential = EmailAuthProvider.getCredential(email, password)
    authFirebase.currentUser?.reauthenticate(credential)?.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            authFirebase.currentUser?.delete()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Firebase", "Аккаунт удален")
                } else {
                    Log.d("Firebase", "Удаление с ошибкой")
                }
            }
        }
    }
}

private fun signOut(authFirebase: FirebaseAuth) {
    authFirebase.signOut()
}

