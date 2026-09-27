package com.shamtube.app.data

import com.shamtube.app.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface AuthRepository {
    val currentUser: StateFlow<User?>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(name: String, email: String, password: String): Result<User>
    suspend fun logout()
}

class LocalAuthRepository : AuthRepository {
    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    override suspend fun login(email: String, password: String): Result<User> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("بيانات الدخول مطلوبة"))
        }
        val user = User(
            id = "local-user",
            name = email.substringBefore("@").ifBlank { "مستخدم شام تيوب" },
            email = email,
            avatarUrl = "https://i.pravatar.cc/150?img=11",
        )
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun register(name: String, email: String, password: String): Result<User> {
        if (name.isBlank() || email.isBlank() || password.length < 6) {
            return Result.failure(IllegalArgumentException("أدخل اسماً وبريداً وكلمة مرور من 6 أحرف على الأقل"))
        }
        val user = User(
            id = "local-user",
            name = name,
            email = email,
            avatarUrl = "https://i.pravatar.cc/150?img=11",
        )
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun logout() {
        _currentUser.value = null
    }
}