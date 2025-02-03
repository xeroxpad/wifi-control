package com.example.wificontrol.screens.authorization

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.wificontrol.screens.profile.AccountData
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

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