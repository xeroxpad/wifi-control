package com.example.wificontrol.screens.authorization

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthenticationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthorizationViewModel(application: Application) : AndroidViewModel(application) {

    private val _mail = MutableStateFlow("")
    val mail: StateFlow<String> = _mail

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _authVkState = MutableLiveData<AuthState>(AuthState.Initial)
    val authVkState: LiveData<AuthState> = _authVkState

    private val _errorStateAuthorization = MutableStateFlow("")
    val errorStateAuthorization: StateFlow<String> = _errorStateAuthorization

    private val authStorage = AuthTokenStorage(application)
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _firebaseAuthState = MutableStateFlow<FirebaseAuthState>(FirebaseAuthState.Initial)
    val firebaseAuthState: StateFlow<FirebaseAuthState> = _firebaseAuthState

    init {
        val storage = VKPreferencesKeyValueStorage(application)
        val token = VKAccessToken.restore(storage)
        val isLoggedIn = token != null && token.isValid
        _authVkState.value = if (isLoggedIn) AuthState.Authorized else AuthState.NotAuthorized

        checkFirebaseAuthState()
    }

    private fun checkFirebaseAuthState() {
        viewModelScope.launch {
            _firebaseAuthState.value = try {
                val token = authStorage.getToken()
                if (token != null && firebaseAuth.currentUser != null) {
                    firebaseAuth.currentUser?.getIdToken(true)?.await()
                    FirebaseAuthState.Authorized
                } else {
                    FirebaseAuthState.Initial
                }
            } catch (e: Exception) {
                FirebaseAuthState.Error("Ошибка проверки авторизации")
            }
        }
    }
    


    fun mailChange(newMail: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                _mail.value = newMail
            }
        }
    }

    fun setEmail(email: String) {
        _mail.value = email
    }

    fun resetFields() {
        _mail.value = ""
        _password.value = ""
    }

    fun passwordChange(newPassword: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                _password.value = newPassword
            }
        }
    }

    fun performAuthResult(result: VKAuthenticationResult) {
        if (result is VKAuthenticationResult.Success) {
            _authVkState.value = AuthState.Authorized
        } else {
            _authVkState.value = AuthState.NotAuthorized
        }
    }
}