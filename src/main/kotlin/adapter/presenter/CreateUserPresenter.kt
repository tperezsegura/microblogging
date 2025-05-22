package org.personal.adapter.presenter

import org.personal.adapter.view.ResponseView
import org.personal.adapter.view.ResponseViewModel.*
import org.personal.adapter.view.UserDto
import org.personal.application.usecase.createuser.CreateUserOutputPort
import org.personal.application.usecase.createuser.CreateUserResponseModel
import org.personal.application.usecase.createuser.CreateUserResponseModel.Failure
import org.personal.application.usecase.createuser.CreateUserResponseModel.Success

class CreateUserPresenter(
    private val view: ResponseView<Any>
) : CreateUserOutputPort {
    override fun presentUserCreation(responseModel: CreateUserResponseModel) {
        val viewModel = when (responseModel) {
            is Success -> SuccessCreate(mapToUserDto(responseModel))
            is Failure.ValidationError -> BadRequest(responseModel.message)
            is Failure.UnexpectedError -> InternalServerError(responseModel.message)
        }
        view.displayResult(viewModel)
    }

    private fun mapToUserDto(responseModel: Success) = with(responseModel.user) {
        UserDto(
            id = responseModel.user.id.toString(),
            username = responseModel.user.username
        )
    }
}