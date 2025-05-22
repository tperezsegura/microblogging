package org.personal.application.usecase.createtweet

import org.personal.domain.entity.Tweet
import org.personal.domain.exception.UserNotFoundException

sealed class CreateTweetResponseModel {
    data class Success(val tweet: Tweet) : CreateTweetResponseModel()
    sealed class Failure : CreateTweetResponseModel() {
        data class ValidationError(val message: String) : Failure()
        data class NotFoundError(val message: String) : Failure()
        data class UnexpectedError(val message: String) : Failure()
    }

    companion object {
        fun mapFailure(error: Throwable): Failure = when (error) {
            is IllegalArgumentException -> Failure.ValidationError(error.message!!)
            is UserNotFoundException -> Failure.NotFoundError(error.message!!)
            else -> Failure.UnexpectedError("Unexpected error")
        }
    }
}