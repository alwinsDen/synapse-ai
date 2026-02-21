package com.alwinsden.dino.requestManager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StartUpLaunchViewModel : ViewModel() {
    private val _nonce = MutableStateFlow("")
    val nonce: StateFlow<String> = _nonce.asStateFlow()

    init {
        viewModelScope.launch {
//            _nonce.value = RequestManager(ClientKtorConfiguration()).createNonce()
        }
    }
}