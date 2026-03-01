package com.alwinsden.dino.requestManager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alwinsden.dino.requestManager.Ktor.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StartUpLaunchViewModel : ViewModel() {
    private val _nonce = MutableStateFlow("")
    val nonce: StateFlow<String> = _nonce.asStateFlow()

    init {
        viewModelScope.launch {
            _nonce.value = RequestManager().createNonce()
        }
    }
}

/*LOGIN STATE*/
sealed class LoginState {
    object Idle : LoginState()
    object Loading: LoginState()
    data class Success(val response: LoginResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}

class SubmitGoogleLoginViewModel: ViewModel() {
    private val _response = MutableStateFlow<LoginState>(LoginState.Idle)
    val googleAuthResponse : StateFlow<LoginState> = _response.asStateFlow()

    fun login(googleToken: String, nonce: String){
        viewModelScope.launch(Dispatchers.IO){
            _response.value = LoginState.Loading
            try {
                val responseState = RequestManager().googleLogin(googleToken = googleToken, nonce = nonce)
                _response.value = LoginState.Success(response = responseState)
            }catch (e: Exception){
                _response.value = LoginState.Error(e.message ?: "Unknown error occurred.")
            }
        }
    }
}
