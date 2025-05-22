package org.personal.adapter.presenter

import org.personal.adapter.view.ResponseView
import org.personal.adapter.view.ResponseViewModel.*
import org.personal.adapter.view.TweetDto
import org.personal.application.usecase.createtweet.CreateTweetOutputPort
import org.personal.application.usecase.createtweet.CreateTweetResponseModel
import org.personal.application.usecase.createtweet.CreateTweetResponseModel.Failure
import org.personal.application.usecase.createtweet.CreateTweetResponseModel.Success

class CreateTweetPresenter(
    private val view: ResponseView<Any>
) : CreateTweetOutputPort {
    override fun presentTweetCreation(responseModel: CreateTweetResponseModel) {
        val viewModel = when (responseModel) {
            is Success -> SuccessCreate(mapToTweetDto(responseModel))
            is Failure.ValidationError -> BadRequest(responseModel.message)
            is Failure.NotFoundError -> NotFound(responseModel.message)
            is Failure.UnexpectedError -> InternalServerError(responseModel.message)
        }
        view.displayResult(viewModel)
    }

    private fun mapToTweetDto(responseModel: Success) = with(responseModel.tweet) {
        TweetDto(
            id = id.toString(),
            authorId = authorId.toString(),
            content = content,
            createdAt = createdAt.toString().substringBeforeLast(".")
        )
    }
}