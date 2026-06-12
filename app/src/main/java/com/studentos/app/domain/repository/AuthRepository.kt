package com.studentos.app.domain.repository

import com.studentos.app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    suspend fun signUp(email: String, password: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<Unit>
    fun logout()
    fun isUserLoggedIn(): Boolean
    fun getUserId(): String?
}
