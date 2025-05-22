package org.personal.application.usecase.viewtimeline

import org.personal.domain.entity.Tweet
import org.personal.domain.exception.UserNotFoundException

sealed class ViewTimelineResponseModel {
    data class Success(val tweets: List<Tweet>) : ViewTimelineResponseModel()
    sealed class Failure : ViewTimelineResponseModel() {
        data class ValidationError(val message: String) : Failure()
        data class NotFoundError(val message: String) : Failure()
        data class UnexpectedError(val message: String) : Failure()
    }

    companion object {
        fun mapFailure(error: Throwable): Failure = when (error) {
            is IllegalArgumentException -> Failure.ValidationError("Follower and followee IDs must be valid numeric values")
            is UserNotFoundException -> Failure.NotFoundError(error.message!!)
            else -> Failure.UnexpectedError("Unexpected error")
        }
    }
}