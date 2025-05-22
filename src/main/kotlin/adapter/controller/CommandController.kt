package org.personal.adapter.controller

import org.personal.application.usecase.createuser.CreateUserInputPort
import org.personal.application.usecase.createuser.CreateUserRequestModel

class CommandController(
    private val createUserInteractor: CreateUserInputPort
) {
    fun createUser(username: String?) = createUserInteractor.createUser(
        CreateUserRequestModel(username)
    )
}