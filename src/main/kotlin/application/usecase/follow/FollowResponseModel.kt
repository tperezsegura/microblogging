package org.personal.application.usecase.follow

import org.personal.domain.exception.AlreadyFollowingException
import org.personal.domain.exception.SelfFollowNotAllowedException
import org.personal.domain.exception.UserNotFoundException


sealed class FollowResponseModel {
    object Success : FollowResponseModel()
    sealed class Failure : FollowResponseModel() {
        data class ValidationError(val message: String) : Failure()
        data class SelfFollowError(val message: String) : Failure()
        data class NotFoundError(val message: String) : Failure()
        data class AlreadyFollowingError(val message: String) : Failure()
        data class UnexpectedError(val message: String) : Failure()
    }

    companion object {
        fun mapFailure(error: Throwable): Failure = when (error) {
            is IllegalArgumentException -> Failure.ValidationError("Follower and followee IDs must be valid numeric values")
            is SelfFollowNotAllowedException -> Failure.SelfFollowError(error.message!!)
            is UserNotFoundException -> Failure.NotFoundError(error.message!!)
            is AlreadyFollowingException -> Failure.AlreadyFollowingError(error.message!!)
            else -> Failure.UnexpectedError("Unexpected error")
        }
    }
}