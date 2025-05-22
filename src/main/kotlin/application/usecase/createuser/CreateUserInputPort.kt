package org.personal.application.usecase.createuser

interface CreateUserInputPort {
    fun createUser(requestModel: CreateUserRequestModel)
}