package org.personal.application.usecase.createuser

import org.personal.application.gateway.UserGateway
import org.personal.domain.entity.buildUser

class CreateUserInteractor(
    private val userGateway: UserGateway,
    private val presenter: CreateUserOutputPort
) : CreateUserInputPort {
    override fun createUser(requestModel: CreateUserRequestModel) {
        val result = runCatching {
            val savedUser = userGateway.save(buildUser(requestModel.username))
            CreateUserResponseModel.Success(savedUser)
        }.getOrElse { throwable -> CreateUserResponseModel.mapFailure(throwable) }
        presenter.presentUserCreation(result)
    }
}