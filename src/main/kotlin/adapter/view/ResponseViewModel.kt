package org.personal.adapter.view

sealed class ResponseViewModel<out T> {
    data class SuccessCreate<T>(val data: T) : ResponseViewModel<T>()
    data class BadRequest(val message: String) : ResponseViewModel<Nothing>()
    data class InternalServerError(val message: String) : ResponseViewModel<Nothing>()
}

data class UserDto(
    val id: String,
    val username: String
)