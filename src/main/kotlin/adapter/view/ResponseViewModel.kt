package org.personal.adapter.view

sealed class ResponseViewModel<out T> {
    data class SuccessCreate<T>(val data: T) : ResponseViewModel<T>()
    object NoContent : ResponseViewModel<Nothing>()
    data class BadRequest(val message: String) : ResponseViewModel<Nothing>()
    data class NotFound(val message: String) : ResponseViewModel<Nothing>()
    data class Conflict(val message: String) : ResponseViewModel<Nothing>()
    data class InternalServerError(val message: String) : ResponseViewModel<Nothing>()
}

data class UserDto(
    val id: String,
    val username: String
)

data class TweetDto(
    val id: String,
    val authorId: String,
    val content: String,
    val createdAt: String
)