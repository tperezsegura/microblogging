package org.personal.adapter.presenter

import org.personal.adapter.view.ResponseView
import org.personal.adapter.view.ResponseViewModel.*
import org.personal.application.usecase.follow.FollowOutputPort
import org.personal.application.usecase.follow.FollowResponseModel
import org.personal.application.usecase.follow.FollowResponseModel.Failure
import org.personal.application.usecase.follow.FollowResponseModel.Success

class FollowPresenter(
    private val view: ResponseView<Any>
) : FollowOutputPort {
    override fun presentFollow(responseModel: FollowResponseModel) {
        val viewModel = when (responseModel) {
            is Success -> NoContent
            is Failure.ValidationError -> BadRequest(responseModel.message)
            is Failure.SelfFollowError -> BadRequest(responseModel.message)
            is Failure.NotFoundError -> NotFound(responseModel.message)
            is Failure.AlreadyFollowingError -> Conflict(responseModel.message)
            is Failure.UnexpectedError -> InternalServerError(responseModel.message)
        }
        view.displayResult(viewModel)
    }
}