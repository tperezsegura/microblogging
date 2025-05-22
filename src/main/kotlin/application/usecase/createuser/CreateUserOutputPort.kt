package org.personal.application.usecase.createuser

interface CreateUserOutputPort {
    fun presentUserCreation(responseModel: CreateUserResponseModel)
}