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
    onSignUpSuccess: (AccountData) -> Unit,
    onSignUpFailure: (String) -> Unit
) {
    if (email.isBlank() || password.isBlank()) {
        onSignUpFailure("email или пароль не может быть пустым")
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
    onSignInSuccess: (AccountData) -> Unit,
    onSignInFailure: (String) -> Unit,
) {
    if (email.isBlank() || password.isBlank()) {
        onSignInFailure("email или пароль не может быть пустым")
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
                "ERROR_INVALID_EMAIL" -> "Некорректный адрес электронной почты"
                "ERROR_WRONG_PASSWORD" -> "Неверный пароль"
                else -> "Ошибка авторизации. Проверьте введенные данные"
            }
        }

        is FirebaseAuthInvalidUserException -> {
            when (error.errorCode) {
                "ERROR_USER_NOT_FOUND" -> "Пользователь не найден"
                "ERROR_USER_DISABLED" -> "Аккаунт отключен"
                else -> "Ошибка авторизации. Пользователь не найден"
            }
        }

        is FirebaseAuthWeakPasswordException -> "Пароль слишком слабый. Используйте более сложный пароль"
        is FirebaseNetworkException -> "Ошибка сети. Проверьте подключение к интернету"
        else -> error?.message ?: "Произошла неизвестная ошибка"
    }
}

private fun translateFirebaseSignUpError(error: Exception?): String {
    return when (error) {
        is FirebaseAuthInvalidCredentialsException -> {
            when (error.errorCode) {
                "ERROR_INVALID_EMAIL" -> "Некорректный адрес электронной почты"
                else -> "Ошибка авторизации. Проверьте введенные данные"
            }
        }
        is FirebaseAuthUserCollisionException -> {
            when (error.errorCode) {
                "ERROR_EMAIL_ALREADY_IN_USE" -> "Этот email уже используется"
                else -> "Ошибка регистрации. Пользователь уже существует"
            }
        }
        is FirebaseAuthWeakPasswordException -> "Пароль слишком слабый. Используйте более сложный пароль"
        is FirebaseNetworkException -> "Ошибка сети. Проверьте подключение к интернету"
        else -> error?.message ?: "Произошла неизвестная ошибка"
    }
}