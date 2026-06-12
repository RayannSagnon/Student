package com.studentos.app.domain.model

enum class SyncState {
    SYNCED, PENDING, ERROR
}

data class SyncMetadata(
    val userId: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val syncState: SyncState = SyncState.PENDING,
    val isDeleted: Boolean = false
)
