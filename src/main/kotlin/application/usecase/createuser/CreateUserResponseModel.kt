package org.personal.application.usecase.createuser

import org.personal.domain.entity.User

sealed class CreateUserResponseModel {
    data class Success(val user: User) : CreateUserResponseModel()
    sealed class Failure : CreateUserResponseModel() {
        data class ValidationError(val message: String) : Failure()
        data class UnexpectedError(val message: String) : Failure()
    }

    companion object {
        fun mapFailure(error: Throwable): Failure = when (error) {
            is IllegalArgumentException -> Failure.ValidationError(error.message!!)
            else -> Failure.UnexpectedError("Unexpected error")
        }
    }
}