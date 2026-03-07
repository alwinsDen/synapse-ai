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

/*LOGIN STATE*/
sealed class ApiState<out T> {
    object Idle : ApiState<Nothing>()
    object Loading: ApiState<Nothing>()
    data class Success<T>(val response: T) : ApiState<T>()
    data class Error(val message: String) : ApiState<Nothing>()
}

class StartUpLaunchViewModel : ViewModel() {
    private val _nonce = MutableStateFlow<ApiState<String>>(ApiState.Idle)
    private val _nonceManual = MutableStateFlow<ApiState<String>>(ApiState.Idle)
    val nonce: StateFlow<ApiState<String>> = _nonce.asStateFlow()
    val nonceManual: StateFlow<ApiState<String>> = _nonceManual.asStateFlow()

    fun initiateAuto(){
        viewModelScope.launch(Dispatchers.IO) {
            _nonce.value = ApiState.Loading
            try {
                val apiResponse = RequestManager().createNonce();
                _nonce.value = ApiState.Success(response = apiResponse)
            } catch (e  : Exception){
                _nonce.value = ApiState.Error(e.message ?: "Unknown error occurred.")
            }
        }
    }

    fun initiateManual(){
        viewModelScope.launch(Dispatchers.IO) {
            _nonceManual.value = ApiState.Loading
            try {
                val apiResponse = RequestManager().createNonce();
                _nonceManual.value = ApiState.Success(response = apiResponse)
            } catch (e  : Exception){
                _nonceManual.value = ApiState.Error(e.message ?: "Unknown error occurred.")
            }
        }
    }

    fun resetStateAuto(){
        _nonce.value = ApiState.Idle
    }

    fun resetStateManual(){
        _nonceManual.value = ApiState.Idle
    }

    init {
        initiateAuto()
    }
}

class SubmitGoogleLoginViewModel: ViewModel() {
    private val _response = MutableStateFlow<ApiState<LoginResponse>>(ApiState.Idle)
    val googleAuthResponse : StateFlow<ApiState<LoginResponse>> = _response.asStateFlow()

    fun login(googleToken: String, nonce: String){
        viewModelScope.launch(Dispatchers.IO){
            _response.value = ApiState.Loading
            try {
                val responseState = RequestManager().googleLogin(googleToken = googleToken, nonce = nonce)
                _response.value = ApiState.Success(response = responseState)
            } catch (e: Exception){
                _response.value = ApiState.Error(e.message ?: "Unknown error occurred.")
            }
        }
    }

    fun resetState(){
        _response.value = ApiState.Idle
    }
}
