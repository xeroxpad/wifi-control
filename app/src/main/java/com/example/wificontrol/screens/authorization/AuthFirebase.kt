package com.example.wificontrol.screens.authorization

import android.util.Log
import com.example.wificontrol.screens.profile.AccountData
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

fun signUpAccountFirebase(
    authFirebase: FirebaseAuth,
    email: String,
    password: String,
    storage: AuthTokenStorage,
    onSignUpSuccess: (AccountData) -> Unit,
    onSignUpFailure: (String) -> Unit
) {
    if (email.isBlank() || password.isBlank()) {
        onSignUpFailure("email и password не может быть пустым!")
        return
    }
    authFirebase.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result.user?.getIdToken(false)?.addOnSuccessListener { tokenResult ->
                    tokenResult.token?.let { token ->
                        storage.saveToken(token)
                        onSignUpSuccess(
                            AccountData(
                                task.result.user?.uid!!,
                                task.result.user?.email!!
                            )
                        )
                    }
                }
            }
        }
        .addOnFailureListener {
            val errorMessage = translateFirebaseSignUpError(it)
            onSignUpFailure(errorMessage)
        }
}

fun signInAccountFirebase(
    authFirebase: FirebaseAuth,
    email: String,
    password: String,
    storage: AuthTokenStorage,
    onSignInSuccess: (AccountData) -> Unit,
    onSignInFailure: (String) -> Unit,
) {
    if (email.isBlank() || password.isBlank()) {
        onSignInFailure("email и password не может быть пустым!")
        return
    }
    authFirebase.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result.user?.getIdToken(false)?.addOnSuccessListener { tokenResult ->
                    tokenResult.token?.let { token ->
                        storage.saveToken(token)
                        onSignInSuccess(
                            AccountData(
                                task.result.user?.uid!!,
                                task.result.user?.email!!
                            )
                        )
                    }
                }
            }
        }
        .addOnFailureListener {
            val errorMessage = translateFirebaseSignInError(it)
            onSignInFailure(errorMessage)
        }
}

fun accountDelete(authFirebase: FirebaseAuth, email: String, password: String) {
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

fun signOut(authFirebase: FirebaseAuth) {
    authFirebase.signOut()
}

private fun translateFirebaseSignInError(error: Exception?): String {
    return when (error) {
        is FirebaseAuthInvalidCredentialsException -> {
            when (error.errorCode) {
                "ERROR_INVALID_EMAIL" -> "некорректный адрес электронной почты"
                "ERROR_WRONG_PASSWORD" -> "неверный пароль"
                else -> "ошибка авторизации, проверьте введенные данные"
            }
        }

        is FirebaseAuthInvalidUserException -> {
            when (error.errorCode) {
                "ERROR_USER_NOT_FOUND" -> "пользователь не найден"
                "ERROR_USER_DISABLED" -> "аккаунт отключен"
                else -> "ошибка авторизации, пользователь не найден"
            }
        }

        is FirebaseAuthWeakPasswordException -> "пароль слишком слабый, используйте более сложный пароль"
        is FirebaseNetworkException -> "ошибка сети, проверьте подключение к интернету"
        else -> error?.message ?: "произошла неизвестная ошибка"
    }
}

private fun translateFirebaseSignUpError(error: Exception?): String {
    return when (error) {
        is FirebaseAuthInvalidCredentialsException -> {
            when (error.errorCode) {
                "ERROR_INVALID_EMAIL" -> "некорректный адрес электронной почты"
                else -> "ошибка регистрации, проверьте введенные данные"
            }
        }

        is FirebaseAuthUserCollisionException -> {
            when (error.errorCode) {
                "ERROR_EMAIL_ALREADY_IN_USE" -> "этот email уже используется"
                else -> "ошибка регистрации, пользователь уже существует"
            }
        }

        is FirebaseAuthWeakPasswordException -> "пароль слишком слабый, используйте более сложный пароль"
        is FirebaseNetworkException -> "ошибка сети, проверьте подключение к интернету"
        else -> error?.message ?: "произошла неизвестная ошибка"
    }
}

fun checkCachedAuth(
    authFirebase: FirebaseAuth,
    storage: AuthTokenStorage,
    onSuccess: (AccountData) -> Unit,
    onFailure: () -> Unit
) {
    val token = storage.getToken()
    if (token != null && authFirebase.currentUser != null) {
        authFirebase.currentUser?.getIdToken(true)?.addOnSuccessListener { tokenResult ->
            storage.saveToken(tokenResult.token ?: return@addOnSuccessListener)
            onSuccess(
                AccountData(
                    authFirebase.currentUser?.uid!!,
                    authFirebase.currentUser?.email!!
                )
            )
        }?.addOnFailureListener {
            storage.clearTokens()
            onFailure()
        }
    } else {
        onFailure()
    }
}