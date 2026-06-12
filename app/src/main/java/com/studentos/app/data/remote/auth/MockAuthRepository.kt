package com.studentos.app.data.remote.auth

import com.studentos.app.domain.model.User
import com.studentos.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MockAuthRepository : AuthRepository {
    private val _user = MutableStateFlow<User?>(User("mock_user_id", "test@studentos.app"))
    override val currentUser: Flow<User?> = _user

    override suspend fun signUp(email: String, password: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        return Result.success(Unit)
    }

    override fun logout() {
        _user.value = null
    }

    override fun isUserLoggedIn(): Boolean = _user.value != null

    override fun getUserId(): String? = _user.value?.uid
}
