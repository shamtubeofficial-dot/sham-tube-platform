package com.shamtube.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shamtube.app.ShamTubeApplication
import com.shamtube.app.data.AuthRepository
import com.shamtube.app.data.model.User
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AuthRepository = (application as ShamTubeApplication).authRepository
    val currentUser: StateFlow<User?> = repository.currentUser

    fun login(email: String, password: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            onComplete(repository.login(email, password).isSuccess)
        }
    }

    fun register(name: String, email: String, password: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            onComplete(repository.register(name, email, password).isSuccess)
        }
    }
}