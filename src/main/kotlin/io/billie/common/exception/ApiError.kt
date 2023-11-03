package io.billie.common.exception

import java.util.UUID

data class ApiError(
    val id: UUID = UUID.randomUUID(),
    val status: String,
    val code: Int,
    val detail: String
)
